package ir.nimbo.searchengine.pagerank;

import com.google.gson.Gson;
import ir.nimbo.searchengine.util.ConfigManager;
import ir.nimbo.searchengine.util.PropertyType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RankCalculator {
    private static final double DAMPING_FACTOR = 0.85;
    private static final double ITERATION_NUMBER = 50;
    private static String familyName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_FAMILY_1);
    private static String outLinksName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_OUTLINKS);
    private static String pageRankName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_PAGERANK);
    private SparkConf sparkConf;
    private Configuration hbaseConf;
    private JavaSparkContext sparkContext;

    public RankCalculator(String appName, String master, int totalPages) {
//        sparkConf = new SparkConf().setAppName(appName).setMaster(master);
//        sparkContext = new JavaSparkContext(sparkConf);
        hbaseConf = HBaseConfiguration.create();
        hbaseConf.addResource(new Path(getClass().getClassLoader().getResource("hbase-site.xml").getPath()));
        hbaseConf.set(TableInputFormat.INPUT_TABLE, ConfigManager.getInstance().getProperty(PropertyType.HBASE_TABLE));
    }

    public void calculate() {
        JavaPairRDD<String, Value> input = getFromHBase();
        JavaPairRDD<String, Value> result = getResult(input);
        writeToHBase(result);

    }

    private JavaPairRDD<String, Value> getFromHBase() {
        JavaPairRDD<ImmutableBytesWritable, Result> read =
                sparkContext.newAPIHadoopRDD(hbaseConf, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);
        return read.mapToPair(pair -> {
            String key = Bytes.toString(pair._1.get());
            String serializedList = Bytes.toString(pair._2.getColumnLatestCell(familyName.getBytes(), outLinksName.getBytes()).getValueArray());
            String serializedRank = Bytes.toString(pair._2.getColumnLatestCell(familyName.getBytes(), outLinksName.getBytes()).getValueArray());
            Gson gson = new Gson();
            List<String> outLinks = gson.fromJson(serializedList, List.class);
            Double pageRank = gson.fromJson(serializedRank, Double.class);
            return new Tuple2<>(key, new Value(outLinks, pageRank));
        });
    }

    JavaPairRDD<String, Value> getResult(JavaPairRDD<String, Value> input) {
        JavaPairRDD<String, Value> mapped;
        for (int i = 0; i < ITERATION_NUMBER; i++) {
            mapped = input.flatMapToPair(key -> {
                List<Tuple2<String, Value>> result = new ArrayList<>();
                for (String url : key._2.outLinks) {
                    result.add(new Tuple2<>(url, new Value(null, DAMPING_FACTOR * key._2.pageRank / key._2.outLinks.size())));
                }
                result.add(new Tuple2<>(key._1, new Value(key._2.outLinks, 1 - DAMPING_FACTOR)));
                return result.iterator();
            });
            input = mapped.reduceByKey((value1, value2) -> {
                List<String> finalList = value1.outLinks == null ? value2.outLinks : value1.outLinks;
                double finalRank = value1.pageRank + value2.pageRank;
                return new Value(finalList, finalRank);
            });
        }
        return input;
    }

    private void writeToHBase(JavaPairRDD<String, Value> toWrite) {
        try {
            final Gson gson = new Gson();
            Job jobConfig = new Job(hbaseConf);
            jobConfig.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE,
                    ConfigManager.getInstance().getProperty(PropertyType.HBASE_TABLE));
            jobConfig.setOutputFormatClass(TableOutputFormat.class);
            JavaPairRDD<ImmutableBytesWritable, Put> hbasePuts = toWrite.mapToPair(pair -> {
                Put put = new Put(Bytes.toBytes(pair._1));
                put.addColumn(familyName.getBytes(), outLinksName.getBytes(), gson.toJson(pair._2.outLinks).getBytes());
                put.addColumn(familyName.getBytes(), pageRankName.getBytes(), Bytes.toBytes(pair._2.pageRank));
                return new Tuple2<>(new ImmutableBytesWritable(), put);
            });
            hbasePuts.saveAsNewAPIHadoopDataset(hbaseConf);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (sparkContext != null)
            sparkContext.close();
    }

}
