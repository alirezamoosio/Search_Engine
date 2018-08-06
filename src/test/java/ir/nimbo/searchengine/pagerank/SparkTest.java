//package ir.nimbo.searchengine.pagerank;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.junit.After;
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.Test;
//import scala.Int;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class SparkTest {
//
//    JavaSparkContext sparkContext;
//
//    @Before
//    public void setUp() {
<<<<<<< Updated upstream
//        SparkConf conf = new SparkConf().setAppName("Test").setMaster("master-node");
=======
//        SparkConf conf = new SparkConf().setAppName("Test").setMaster("local[2]");
>>>>>>> Stashed changes
//        sparkContext = new JavaSparkContext(conf);
//    }
//
//    @Test
//    public void test() {
//        List<Integer> list = Arrays.asList(1, 2, 3, 4);
//        JavaRDD<Integer> set = sparkContext.parallelize(list);
//        JavaRDD<Integer> mapped = set.map(key -> key * 2);
//        int total = mapped.reduce((a, b) -> a + b);
//        Assert.assertEquals(20, total);
//    }
//
//    @After
//    public void tearDown() {
//    }
//}
