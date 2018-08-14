package ir.nimbo.searchengine.pagerank;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SparkTest {

    JavaSparkContext sparkContext;

    @Before
    public void setUp() {
        SparkConf conf = new SparkConf().setAppName("Test").setMaster("spark://94.23.214.93:7077");
        sparkContext = new JavaSparkContext(conf);
    }

    @Test
    public void primaryTest() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        JavaRDD<Integer> set = sparkContext.parallelize(list);
        JavaRDD<Integer> mapped = set.map(key -> key * 2);
        int total = mapped.reduce((a, b) -> a + b);
        Assert.assertEquals(20, total);
    }

    @Test
    public void connectionToHBaseTest() {
        System.out.println("line 1");
        Configuration config = HBaseConfiguration.create();
        System.out.println("line 2");
        config.addResource(new Path(getClass().getClassLoader().getResource("hbase-site.xml").getPath()));
        System.out.println("line 3");
        config.set(TableInputFormat.INPUT_TABLE, "test");
        System.out.println("line 4");
        JavaPairRDD<ImmutableBytesWritable, Result> result = sparkContext.newAPIHadoopRDD(config, TableInputFormat.class,
                ImmutableBytesWritable.class, Result.class);
        System.out.println("line 5");
        Map<ImmutableBytesWritable, Result> mapResult = result.collectAsMap();
        System.out.println("line 5");
        System.out.println(mapResult);
    }

    @After
    public void tearDown() {
        sparkContext.close();
    }
}
