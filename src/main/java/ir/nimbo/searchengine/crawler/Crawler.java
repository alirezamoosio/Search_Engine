package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Thread.sleep;

public class Crawler {
    private FinishedRequestHandler request;
    private QueueCommunicable queueCommunicable;
    private ExecutorService executor;
    private boolean isWorking = true;
    private int numberOfCrawled=0;
    private static Logger logger = Logger.getLogger(Crawler.class);

    public Crawler(FinishedRequestHandler request, QueueCommunicable queueCommunicable, int threadPoolSize) {
        this.request = request;
        this.queueCommunicable = queueCommunicable;
        executor = Executors.newFixedThreadPool(threadPoolSize);
    }
    public void start() {
        WebDocument documento = null;
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread( () -> {
                final int id= finalI;
                while (isWorking) {
                    String url = null;
                    WebDocument webDocument;
                    try {
                        url = queueCommunicable.pollNewURL();
                        System.out.println("started"+id);
                        webDocument = Parser.parse(url);
                        request.accept(webDocument);
                        queueCommunicable.pushNewURL(webDocument.getLinks().toArray(new String[0]));
                        System.out.println("parsed "+url);
                        numberOfCrawled++;
                    } catch (IOException e) {
                        logger.error("cant parse "+url);
                    } catch (IllegalLanguageException ignored) {
                    }
                }

            }).start();

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

