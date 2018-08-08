package ir.nimbo.searchengine.kafka;

import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class KafkaManagerTest {
    @Test
    public void testKafka() {
        KafkaManager kafkaManager = new KafkaManager();
        ArrayList<String> results = new ArrayList<>();
        WebDocument webDocument =new WebDocument();
        List<String> links = new ArrayList<>();
        for(int i = 6; i < 11; i++){
            ArrayList<WebDocument> docs = new ArrayList<>();
            for(int j = 0; j < 100; j++){
                links.add("testMessage" + i + "_" + j);
            }
            kafkaManager.pushNewURL(links.toArray(new String[0]));
            for(String link: kafkaManager.getUrls()){
                System.out.println(link);
                results.add(link);
            }
            System.out.println("First poll! " + i);
            for(String link: kafkaManager.getUrls()){
                System.out.println(link);
                results.add(link);
            }
            System.out.println("Second poll! " + i);
        }
    }
}