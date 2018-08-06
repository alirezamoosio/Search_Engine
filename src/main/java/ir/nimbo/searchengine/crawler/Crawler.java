package ir.nimbo.searchengine.crawler;

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
        kafkaExecutor = Executors.newScheduledThreadPool(2);
        parserPool = Executors.newFixedThreadPool(200);
        this.urlQueue = urlQueue;
        inputUrls = new ArrayList<>();
        newPages = new ArrayList<>();
    }

    public void start() {
//        for (int i = 0; i < threadPoolSize; i++) {
//            System.out.println("thread "+ i + "started");
//            Thread thread = new Thread(() -> {
//                while (isWorking) {
//                    ArrayList<String> temp = urlQueue.getUrls();
//                    temp.forEach(e -> {
//                        try {
//                            WebDocument webDocument = Parser.parse(e);
//                            webDocument.getLinks().forEach(link -> urlQueue.pushNewURLInTempQueue(link.getUrl()));
//                        } catch (RuntimeException e1) {
//                            logger.error("error while pushing links of " + e);
//                        }
//                    });
//
//                    System.out.println(Parser.i);
//
//                }
//            });
//            thread.setPriority(MAX_PRIORITY-2);
//            thread.start();
//        }
    }

    public void addPage(WebDocument page) {
        newPages.add(page);
    }

    @Override
    public void run() {
        Thread thread = new Thread(() -> {
            inputUrls = urlQueue.getUrls();
            for (String url : inputUrls) {
                System.out.println(url);
                parserPool.execute(new Parser(url, this));
            }
            inputUrls.clear();
        });
        thread.setPriority(MAX_PRIORITY - 2);
        kafkaExecutor.scheduleAtFixedRate(thread, 0, 100, TimeUnit.MILLISECONDS);
        kafkaExecutor.scheduleAtFixedRate(new Thread(() -> {
            urlQueue.pushNewURL(newPages);

            newPages.clear();
        }), 0, 1000, TimeUnit.MILLISECONDS);

    }
}

