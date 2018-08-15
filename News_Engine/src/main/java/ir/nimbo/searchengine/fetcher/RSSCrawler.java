package ir.nimbo.searchengine.fetcher;

import com.sun.xml.internal.bind.v2.TODO;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.Thread.MAX_PRIORITY;

public class RSSCrawler implements Runnable{
    private static final int NUMBER_OF_THREAD = 20;
    private static final int READER_THREAD_PRIORITY = MAX_PRIORITY-2;
    private URLQueue<String> urlQueue;

    public RSSCrawler(URLQueue<String> urlQueue) {
        this.urlQueue = urlQueue;
    }


    @Override
    public void run() {
        for (int i = 0; i < NUMBER_OF_THREAD; i++) {
            Thread thread = new Thread(() -> {
                LinkedList<String> list=new LinkedList<>();
                while (true){
                    // FIXME: 8/15/18
                }
            });
            thread.setPriority(READER_THREAD_PRIORITY);
            thread.start();
        }
    }
}
