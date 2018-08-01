package ir.nimbo.searchengine.kafka;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class KafkaManagerTest {
    @Test
    public void testKafka() throws InterruptedException {
        KafkaManager kafkaManager = new KafkaManager("test");
        ArrayList<String> results = new ArrayList<>();
        for(int i = 1; i < 6; i++){
            ArrayList<String> testMessages = new ArrayList<>();
            for(int j = 0; j < 100; j++){
                testMessages.add("testMessage" + i + "_" + j);
            }
            kafkaManager.pushNewURL(testMessages.toArray(new String[0]));
            int lastSize = results.size();
            for(String link: kafkaManager.getUrls()){
                System.out.println(link);
                results.add(link);
            }
            assertEquals(lastSize + 50, results.size());
            lastSize = results.size();
            System.out.println("First poll! " + i);
            for(String link: kafkaManager.getUrls()){
                System.out.println(link);
                results.add(link);
            }
            assertEquals(lastSize + 50, results.size());
            lastSize = results.size();
            System.out.println("Second poll! " + i);
        }
        assertEquals(500, results.size());
    }
}