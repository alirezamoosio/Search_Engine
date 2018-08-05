package ir.nimbo.searchengine.kafka;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class KafkaManagerTest {
    @Test
    public void testKafka() {
        KafkaManager kafkaManager = new KafkaManager("test","master-node:9092,worker-node:9092");

        ArrayList<String> results = new ArrayList<>();
        for(int i = 6; i < 11; i++){
            ArrayList<String> testMessages = new ArrayList<>();
            for(int j = 0; j < 100; j++){
                testMessages.add("testMessage" + i + "_" + j);
            }
            kafkaManager.pushNewURL(testMessages.toArray(new String[0]));
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