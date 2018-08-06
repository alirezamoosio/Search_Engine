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
    private URLQueue URLQueue;
    private List<String> inputUrls;
    private List<WebDocument> newPages;
    private ScheduledExecutorService kafkaExecutor;
    private ExecutorService parserPool;
    private String topic;
    private String portsWithId;
    private int threadPoolSize;

    public Crawler(URLQueue URLQueue, ParseFinishObserver observer, String topic, String portsWithId, int threadPoolSize) {
        kafkaExecutor = Executors.newScheduledThreadPool(2);
        parserPool = Executors.newFixedThreadPool(200);
        this.URLQueue=URLQueue;
        inputUrls = new ArrayList<>();
        newPages = new ArrayList<>();
        this.topic = topic;
        this.portsWithId = portsWithId;
        this.threadPoolSize = threadPoolSize;
        
    }

    public void start() {
//        KafkaManager URLQueue = new KafkaManager(topic, portsWithId);
//        new Thread(()->{
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            URLQueue.shuffle();
//            URLQueue.addTempListToQueue();
//            URLQueue.flush();
//
//        }).start();
//        for (int i = 0; i < threadPoolSize; i++) {
//            System.out.println("thread "+ i + "started");
//            Thread thread = new Thread(() -> {
//                while (isWorking) {
//                    ArrayList<String> temp = URLQueue.getUrls();
//                    temp.forEach(e -> {
//                        try {
//                            WebDocument webDocument = Parser.parse(e);
//                            webDocument.getLinks().forEach(link -> URLQueue.pushNewURLInTempQueue(link.getUrl()));
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
    public void addPage(WebDocument page){
        newPages.add(page);
    }
    @Override
    public void run() {
        Thread thread=new Thread(() -> {
            inputUrls = URLQueue.getUrls();
            for (String url:inputUrls) {
                parserPool.execute(new Parser(url,this));
            }
            inputUrls.clear();
        });
        thread.setPriority(MAX_PRIORITY-2);
        kafkaExecutor.scheduleAtFixedRate(thread, 0, 100, TimeUnit.MILLISECONDS);

        kafkaExecutor.scheduleAtFixedRate(new Thread(() -> {
            URLQueue.pushNewURL(newPages);
            newPages.clear();
        }), 0, 1000, TimeUnit.MILLISECONDS);

    }
}

