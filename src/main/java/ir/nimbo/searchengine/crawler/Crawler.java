package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class Crawler implements Runnable {
    //    private FinishedRequestHandler request;
//    private QueueCommunicable queueCommunicable;
//    private ExecutorService executor;
//    private boolean isWorking = true;
//    private int numberOfCrawled = 0;
    private static Logger logger = Logger.getLogger(Crawler.class);
    private KafkaManager kafkaManager;
    private ArrayList<String> inputUrls;
    private ArrayList<WebDocument> newPages;
    private ScheduledExecutorService kafkaExecuter;
    private ExecutorService parserPool;
//    private String topic;
//    private String portsWithId;
//    private int threadPoolSize;

    public Crawler(String topic, String portsWithId, int threadPoolSize) {
        kafkaExecuter = Executors.newScheduledThreadPool(2);
        parserPool = Executors.newFixedThreadPool(200);
        inputUrls = new ArrayList<>();
        newPages = new ArrayList<>();
//        this.topic = topic;
//        this.portsWithId = portsWithId;
//        this.threadPoolSize = threadPoolSize;
//        System.out.println("crawler initialized");
    }

    public void start() {
//        KafkaManager kafkaManager = new KafkaManager(topic, portsWithId);
//        new Thread(()->{
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            kafkaManager.shuffle();
//            kafkaManager.addTempListToQueue();
//            kafkaManager.flush();
//
//        }).start();
//        for (int i = 0; i < threadPoolSize; i++) {
//            System.out.println("thread "+ i + "started");
//            Thread thread = new Thread(() -> {
//                while (isWorking) {
//                    ArrayList<String> temp = kafkaManager.getUrls();
//                    temp.forEach(e -> {
//                        try {
//                            WebDocument webDocument = Parser.parse(e);
//                            webDocument.getLinks().forEach(link -> kafkaManager.pushNewURLInTempQueue(link.getUrl()));
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

//    public void setFinishedRequest(FinishedRequestHandler request) {
//        this.request = request;
//    }
//
//    public void setQueueCommunicable(QueueCommunicable queueCommunicable) {
//        this.queueCommunicable = queueCommunicable;
//    }
//
//    public void shutDown() {
//        isWorking = false;
//    }
//
//    public int getNumberOfCrawled() {
//        return numberOfCrawled;
//    }
//
//    public void setNumberOfCrawled(int numberOfCrawled) {
//        this.numberOfCrawled = numberOfCrawled;
//    }
    public void addPage(WebDocument page){
        newPages.add(page);
    }
    @Override
    public void run() {
        kafkaExecuter.scheduleAtFixedRate(new Thread(() -> {
            inputUrls = kafkaManager.getUrls();
            for (String url:inputUrls) {
                parserPool.execute(new Parser(url,this));
            }
            inputUrls.clear();
        }), 0, 100, TimeUnit.MILLISECONDS);

        kafkaExecuter.scheduleAtFixedRate(new Thread(() -> {
            kafkaManager.pushNewURL(newPages);
            newPages.clear();
        }), 0, 1000, TimeUnit.MILLISECONDS);

    }
}

