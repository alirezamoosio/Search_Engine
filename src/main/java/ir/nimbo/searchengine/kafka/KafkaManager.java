package ir.nimbo.searchengine.kafka;

import ir.nimbo.searchengine.crawler.DuplicateLinkHandler;
import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.URLQueue;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class KafkaManager implements URLQueue {
    private final String topic;

    private final LinkedList<List<String>> tempArrayLists = new LinkedList<>();
    private KafkaConsumer<String, String> consumer;
    private Producer<String, String> producer;
    private DuplicateLinkHandler duplicateLinkHandler;

    public KafkaManager(String topic, String portsWithIp, String groupID, int maxPoll) {
        this.topic = topic;
        Properties props = new Properties();
        props.put("bootstrap.servers", portsWithIp);
        props.put("group.id", groupID);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "10000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("max.poll.records", maxPoll);
        props.put("auto.offset.reset", "earliest");
        System.out.println(props.toString());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        producer = new KafkaProducer<>(props);
        duplicateLinkHandler = DuplicateLinkHandler.getInstance();
        try {
            duplicateLinkHandler.loadHashTable();
        } catch (IOException e) {
            System.exit(0);
        }
    }

    @Override
    public synchronized ArrayList<String> getUrls() {
        ArrayList<String> result = new ArrayList<>();
        ConsumerRecords<String, String> records = consumer.poll(10000);
        consumer.commitSync();
        for (ConsumerRecord<String, String> record : records) {
            result.add(record.value());
        }
        return result;
    }

    public void pushNewURL(Link... links) {
    }

    @Override
    public void pushNewURL(String... links) {
        for (String url : links) {
//            if (!duplicateLinkHandler.isDuplicate(url)) {
            try {
                String key = new URL(url).getHost();
                producer.send(new ProducerRecord<>(topic, key, url));
            } catch (MalformedURLException e) {
                System.out.println("Wrong Exception");
            }
//            }
        }

    }


    @Override
    protected void finalize() {
        flush();
        producer.close();
        consumer.close();
        duplicateLinkHandler.saveHashTable();
    }

    public void flush() {
        producer.flush();
    }
}
