package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LanguageDetector;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class Crawler implements Runnable {
    private static Logger logger = Logger.getLogger(Crawler.class);
    private URLQueue urlQueue;
    private List<String> inputUrls;
    private List<WebDocument> newPages;
    private ScheduledExecutorService kafkaExecutor;
    private ExecutorService parserPool;

    public Crawler(URLQueue urlQueue) {
        LanguageDetector.profileLoad();
        kafkaExecutor = Executors.newScheduledThreadPool(2);
        parserPool = Executors.newFixedThreadPool(200);
        this.urlQueue = urlQueue;
        inputUrls = new ArrayList<>();
        newPages = new ArrayList<>();
    }


    public void addPage(WebDocument page) {
        newPages.add(page);
    }

    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            inputUrls = urlQueue.getUrls();
            for (String url : inputUrls) {
                parserPool.execute(new Parser(url, this));
            }
            inputUrls.clear();
        });
        thread.setPriority(MAX_PRIORITY - 2);
        kafkaExecutor.scheduleAtFixedRate(thread, 0, 50, TimeUnit.MILLISECONDS);
        kafkaExecutor.scheduleAtFixedRate(new Thread(() -> {
            urlQueue.pushNewURL(newPages);
            newPages.clear();
        }), 0, 1000, TimeUnit.MILLISECONDS);

    }
}

