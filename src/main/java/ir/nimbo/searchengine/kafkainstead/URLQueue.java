package ir.nimbo.searchengine.kafkainstead;

import ir.nimbo.searchengine.crawler.Communicable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.SplittableRandom;
import java.util.concurrent.SynchronousQueue;

public class URLQueue implements Communicable {
    private Queue<String> URLQueue = new SynchronousQueue<>();

    @Override
    public String pullNewURL() {
        return URLQueue.poll();
    }

    @Override
    public void pushNewURL(String... url) {
        URLQueue.addAll(Arrays.asList(url));
    }
}
