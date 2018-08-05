package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Crawler;
import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.Parser;
import ir.nimbo.searchengine.crawler.WebDocument;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;


public class kafkaTest {
    private static Logger logger = Logger.getLogger(Crawler.class);
    private ExecutorService executor;

    public static void main(String[] args) {
        KafkaManager kafkaManager = new KafkaManager("links", "localhost:9092,localhost:9093");
         {
            ArrayList<String> temp = kafkaManager.getUrls();
            temp.parallelStream().forEach(e -> {
                try {
                    WebDocument webDocument = Parser.parse(e);
                    webDocument.getLinks().forEach(link -> kafkaManager.pushNewURLInTempQueue(link.getUrl()));
                } catch (RuntimeException e1) {
                    logger.error("error while pushing links of " + e);
                }
            });
            System.out.println();
            System.out.println();
            kafkaManager.getUrlTempList().forEach(System.out::println);
            System.out.println();
            kafkaManager.shuffle();
            kafkaManager.getUrlTempList().forEach(System.out::println);
            System.out.println();
            System.out.println();

            kafkaManager.addTempListToQueue();
        }

    }

}



