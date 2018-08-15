package ir.nimbo.searchengine;


import ir.nimbo.searchengine.twitter.TweetHandler;

import java.io.IOException;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws IOException {
        TweetHandler tweetHandler = new TweetHandler("Tweeet", "local[2]");
        tweetHandler.run();
    }
}
