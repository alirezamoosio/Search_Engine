package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import static java.lang.Thread.MAX_PRIORITY;

public class Crawler {
    private FinishedRequestHandler request;
    private QueueCommunicable queueCommunicable;
    private ExecutorService executor;
    private boolean isWorking = true;
    private int numberOfCrawled = 0;
    private static Logger logger = Logger.getLogger(Crawler.class);
    private String topic;
    private String portsWithId;
    private int threadPoolSize;

    public Crawler(String topic, String portsWithId, int threadPoolSize) {
        this.topic = topic;
        this.portsWithId = portsWithId;
        this.threadPoolSize = threadPoolSize;
        System.out.println("crawler initialized");
    }
    public void start() {
        KafkaManager kafkaManager = new KafkaManager(topic, portsWithId);
        for (int i = 0; i < threadPoolSize; i++) {
            System.out.println("thread "+ i + "started");
            Thread thread = new Thread(() -> {
                while (isWorking) {
                    ArrayList<String> temp = kafkaManager.getUrls();
                    temp.forEach(e -> {
                        try {
                            WebDocument webDocument = Parser.parse(e);
                            webDocument.getLinks().forEach(link -> kafkaManager.pushNewURLInTempQueue(link.getUrl()));
                        } catch (RuntimeException e1) {
                            logger.error("error while pushing links of " + e);
                        }
                    });

                    System.out.println(Parser.i);
                    kafkaManager.shuffle();
                    kafkaManager.addTempListToQueue();
                }
            });
            thread.setPriority(MAX_PRIORITY);
            thread.start();
        }
    }

    public void setFinishedRequest(FinishedRequestHandler request) {
        this.request = request;
    }

    public void setQueueCommunicable(QueueCommunicable queueCommunicable) {
        this.queueCommunicable = queueCommunicable;
    }

    public void shutDown() {
        isWorking = false;
    }

    public int getNumberOfCrawled() {
        return numberOfCrawled;
    }

    public void setNumberOfCrawled(int numberOfCrawled) {
        this.numberOfCrawled = numberOfCrawled;
    }
}

