package ir.nimbo.searchengine.fetcher;

import com.sun.xml.internal.bind.v2.TODO;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Thread.MAX_PRIORITY;

public class RSSFetcher implements Runnable {
    private static final int NUMBER_OF_THREAD = 20;
    private static final int READER_THREAD_PRIORITY = MAX_PRIORITY - 2;
    private URLQueue<RSSLink> urlQueue;

    public RSSFetcher(URLQueue<RSSLink> urlQueue) {
    }


    @Override
    public void run() {
        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            Thread thread = new Thread(() -> {
                LinkedList<RSSLink> list = new LinkedList<>();
                while (true) {
                    if (list.size() < 1) {
                        list.addAll(urlQueue.getUrls());
                    }
                    try {
                        RSSLink rssLink = list.removeFirst();
                        String text=NewsParser.Parse(rssLink.getDomain(),rssLink.getUrl());
                        News news =new News(rssLink,text);
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
