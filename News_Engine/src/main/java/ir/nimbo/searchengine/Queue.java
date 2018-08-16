package ir.nimbo.searchengine;

import ir.nimbo.searchengine.fetcher.RSSLink;
import ir.nimbo.searchengine.fetcher.URLQueue;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Queue<T> implements URLQueue<T> {
    private ArrayBlockingQueue<T> queue;

    @Override
    public List<T> getUrls() {
        return Collections.singletonList(queue.poll());
    }

    @Override
    public void addUrls(List<T> urls) {
        urls.forEach(url -> {
            try {
                queue.put(url);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public int size() {
        return queue.size();
    }
}
