//package ir.nimbo.searchengine.pagerank;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.api.java.JavaPairRDD;
//import org.apache.spark.api.java.JavaSparkContext;
//import scala.Tuple2;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RankCalculator {
//    private static final double DAMPING_FACTOR = 0.85;
//    private static final double ITERATION_NUMBER = 1;
//    private SparkConf conf;
//    private JavaSparkContext sparkContext;
//    private static Integer totalPages;
//
//    public RankCalculator(String appName, String master, int totalPages) {
////        conf = new SparkConf().setAppName(appName).setMaster(master);
////        sparkContext = new JavaSparkContext(conf);
//        RankCalculator.totalPages = totalPages;
//    }
//
//    public void calculate() {
//        JavaPairRDD<String, Value> input = getFromHBase();
//        JavaPairRDD<String, Value> result = getResult(input);
//        writeToHBase(result);
//    }
//
//    private JavaPairRDD<String, Value> getFromHBase() {
//        return null;
//    }
//
//    JavaPairRDD<String, Value> getResult(JavaPairRDD<String, Value> input) {
//        JavaPairRDD<String, Value> mapped;
//        for (int i = 0; i < ITERATION_NUMBER; i++) {
//            mapped = input.flatMapToPair(key -> {
//                List<Tuple2<String, Value>> result = new ArrayList<>();
//                for (String url : key._2.outLinks) {
//                    result.add(new Tuple2<>(url, new Value(null, DAMPING_FACTOR * key._2.pageRank / key._2.outLinks.size())));
//                }
//                result.add(new Tuple2<>(key._1, new Value(key._2.outLinks, (1 - DAMPING_FACTOR) / totalPages)));
//                return result.iterator();
//            });
//            input = mapped.reduceByKey((value1, value2) -> {
//                List<String> finalList = value1.outLinks == null ? value2.outLinks : value1.outLinks;
//                double finalRank = value1.pageRank + value2.pageRank;
//                return new Value(finalList, finalRank);
//            });
//        }
//        return input;
//    }
//
//    private void writeToHBase(JavaPairRDD<String, Value> toWrite) {
//
//    }
//
//    public void close() {
//        if (sparkContext != null)
//            sparkContext.close();
//    }
//
//}
