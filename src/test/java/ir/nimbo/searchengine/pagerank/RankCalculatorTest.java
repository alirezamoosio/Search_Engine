//package ir.nimbo.searchengine.pagerank;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import scala.Int;
//import scala.Tuple2;
//
//import java.io.IOException;
//import java.io.ObjectOutputStream;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.List;
//import java.util.Map;
//
//public class RankCalculatorTest {
//
//    private RankCalculator calculator;
//
//    @Before
//    public void setUp() {
<<<<<<< Updated upstream
//        calculator = new RankCalculator("Test", "master-node", 3);
=======
//        calculator = new RankCalculator("Test", "local", 3);
>>>>>>> Stashed changes
//    }
//
//    @Test
//    public void getResult() {
//        String site1 = "site1";
//        Value value1 = new Value(Arrays.asList("site2", "site3"), 1.0 / 3);
//        String site2 = "site2";
//        Value value2 = new Value(Collections.singletonList("site3"), 1.0 / 3);
//        String site3 = "site3";
//        Value value3 = new Value(Collections.singletonList("site2"), 1.0 / 3);
<<<<<<< Updated upstream
//        SparkConf conf = new SparkConf().setAppName("Test").setMaster("master-node");
=======
//        SparkConf conf = new SparkConf().setAppName("Test").setMaster("local");
>>>>>>> Stashed changes
//        JavaSparkContext sparkContext = new JavaSparkContext(conf);
//        List<Tuple2<String, Value>> list = Arrays.asList(new Tuple2<>(site1, value1),
//                new Tuple2<>(site2, value2),
//                new Tuple2<>(site3, value3));
//        JavaRDD<Tuple2<String, Value>> helpSet = sparkContext.parallelize(list);
//        JavaPairRDD<String, Value> input = JavaPairRDD.fromJavaRDD(helpSet);
//        JavaPairRDD<String, Value> result = calculator.getResult(input);
//        Map<String, Value> resultMap = result.collectAsMap();
//        System.out.println(resultMap);
//    }
//
//    @After
//    public void tearDown() {
//        calculator.close();
//    }
//}