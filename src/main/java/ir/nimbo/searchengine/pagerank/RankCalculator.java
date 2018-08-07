package ir.nimbo.searchengine.pagerank;

import com.google.gson.Gson;
import ir.nimbo.searchengine.util.ConfigManager;
import ir.nimbo.searchengine.util.PropertyType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RankCalculator {
    private static final double DAMPING_FACTOR = 0.85;
    private static final double ITERATION_NUMBER = 50;
    private static String familyName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_FAMILY);
    private static String outLinksName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_OUTLINKS);
    private static String pageRankName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_PAGERANK);
    private SparkConf conf;
    private JavaSparkContext sparkContext;
    private static Integer totalPages;

    public RankCalculator(String appName, String master, int totalPages) {
//        conf = new SparkConf().setAppName(appName).setMaster(master);
//        sparkContext = new JavaSparkContext(conf);
        RankCalculator.totalPages = totalPages;
    }

    private void setConfig(Configuration config) {
        String configPath = getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        config.addResource(new Path(configPath));
    }

    public void calculate() {
        JavaPairRDD<String, Value> input = getFromHBase();
        JavaPairRDD<String, Value> result = getResult(input);
        writeToHBase(result);

    }

    private JavaPairRDD<String, Value> getFromHBase() {
        Configuration config = HBaseConfiguration.create();
        config.addResource(new Path(getClass().getClassLoader().getResource("hbase-site.xml").getPath()));
        JavaPairRDD<ImmutableBytesWritable, Result> read =
                sparkContext.newAPIHadoopRDD(config, TableInputFormat.class, ImmutableBytesWritable.class, Result.class);
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
                result.add(new Tuple2<>(key._1, new Value(key._2.outLinks, (1 - DAMPING_FACTOR) / totalPages)));
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

    }

    public void close() {
        if (sparkContext != null)
            sparkContext.close();
    }

}
