package ir.nimbo.searchengine.kafka;

import ir.nimbo.searchengine.crawler.DuplicateLinkHandler;
import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.URLQueue;
import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.*;

public class KafkaManager implements URLQueue {
    private final String topic;
    private final LinkedList<String> tempList = new LinkedList<>();
    private KafkaConsumer<Integer, String> consumer;
    private Producer<Integer, String> producer;
    private DuplicateLinkHandler duplicateLinkHandler;

    public KafkaManager() {
        this.topic = "links";
        Properties props = new Properties();
        props.put("bootstrap.servers", "master-node:9092,worker-node:9092");
        props.put("group.id", "firstrun");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "10000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("max.poll.records", "80");
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
        ConsumerRecords<Integer, String> records = consumer.poll(10000);
        consumer.commitSync();
        for (ConsumerRecord<Integer, String> record : records) {
            result.add(record.value());
        }
        return result;
    }

    @Override
    public void pushNewURL(WebDocument page) {
//        tempList.addAll(Arrays.asList(page.getLinks().stream().map(Link::getUrl).toArray(String[]::new)));
//        shuffle();
        for (String url : page.getLinks().stream().map(Link::getUrl).toArray(String[]::new)) {
            if (!duplicateLinkHandler.isDuplicate(url))
                producer.send(new ProducerRecord<>(topic, url.hashCode(), url));
        }
//        tempList.clear();
    }

    public void shuffle() {
        Collections.shuffle(tempList);
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
