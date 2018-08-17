package ir.nimbo.searchengine.fetcher;


import ir.nimbo.searchengine.trend.WordCounter;

import java.io.IOException;
import java.util.LinkedList;

import static java.lang.Thread.MAX_PRIORITY;

public class RSSFetcher implements Runnable {
    private static final int NUMBER_OF_THREAD = 20;
    private static final int READER_THREAD_PRIORITY = MAX_PRIORITY - 2;
    private RSSQueue<RSSLink> rssQueue;

    public RSSFetcher(RSSQueue<RSSLink> rssQueue) {
        this.rssQueue = rssQueue;
    }

    @Override
    public void run() {
        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            Thread thread = new Thread(() -> {
                LinkedList<RSSLink> list = new LinkedList<>();
                while (true) {
                    if (list.size() < 1) {
                        list.addAll(rssQueue.getUrls());
                    }
                    try {
                        RSSLink rssLink = list.removeFirst();
                        String text = NewsParser.Parse(rssLink.getDomain(), rssLink.getUrl());
                        News news = new News(rssLink, text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // FIXME: 8/15/18
                }
            });
            thread.setPriority(READER_THREAD_PRIORITY);
            thread.start();
        }
    }
}
