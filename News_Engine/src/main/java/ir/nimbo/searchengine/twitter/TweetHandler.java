package ir.nimbo.searchengine.twitter;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.twitter.TwitterUtils;
import twitter4j.Status;

public class TweetHandler {
    private static final String consumerKey = "f0ElnjMLf4thM4aRiU1a2dDI9";
    private static final String consumerSecret = "drymZ0vVbKiy537uZLF86MLi4XLBi5sr5HJSqaF1Ow3juV3TQQ";
    private static final String accessToken = "1745610144-rXoFWacSFPVE9WvWq6Fw4W3rJZ0SK0sVmItNiNH";
    private static final String accessTokenSecret = "N1acTyPpCmVFml8MRGqmDPvnxHViPSttxq5tVnIyo0l5f";

    private JavaStreamingContext jssc;

    public TweetHandler(String name, String master) {
        SparkConf conf = new SparkConf().setAppName(name).setMaster(master);
        jssc = new JavaStreamingContext(conf, new Duration(3000));
    }

    public void run() {
        System.setProperty("twitter4j.oauth.consumerKey", consumerKey);
        System.setProperty("twitter4j.oauth.consumerSecret", consumerSecret);
        System.setProperty("twitter4j.oauth.accessToken", accessToken);
        System.setProperty("twitter4j.oauth.accessTokenSecret", accessTokenSecret);
        System.setProperty("twitter4j.http.proxyHost", "127.0.0.1");
        System.setProperty("twitter4j.http.proxyPort", "41709");
        JavaReceiverInputDStream<Status> twitterStream = TwitterUtils.createStream(jssc);
        JavaDStream<String> tweets = twitterStream.map(Status::getText);
        tweets.print();
        jssc.start();
    }

}
