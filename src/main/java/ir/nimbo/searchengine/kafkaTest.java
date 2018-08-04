package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.Parser;
import ir.nimbo.searchengine.crawler.WebDocument;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.kafka.KafkaManager;

import java.io.IOException;
import java.util.ArrayList;


public class kafkaTest {
    public static void main(String[] args) throws IOException {
        KafkaManager kafkaManager = new KafkaManager("links", "localhost:9092");
        while (true) {
            ArrayList<String> temp = kafkaManager.getUrls();
            for (String e : temp) {
                try {
                    WebDocument webDocument = Parser.parse(e);
                    webDocument.getLinks().forEach(System.out::println);
                    webDocument.getLinks().forEach(link->kafkaManager.pushNewURL(link.getUrl()));
                } catch (RuntimeException | IllegalLanguageException | IOException exception) {
                    exception.printStackTrace();
                }
            }
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



