package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class Crawler {
    private FinishedRequestHandler request;
    private QueueCommunicable queueCommunicable;
    private ExecutorService executor;
    private boolean isWorking = true;
    private int numberOfCrawled = 0;
    private static Logger logger = Logger.getLogger(Crawler.class);

    public Crawler(FinishedRequestHandler request, QueueCommunicable queueCommunicable, int threadPoolSize) {
        this.request = request;
        this.queueCommunicable = queueCommunicable;
    }

    public void start() {
        KafkaManager kafkaManager = new KafkaManager("links", "localhost:9092,localhost:9093");
        for (int i = 0; i < 500; i++) {
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

