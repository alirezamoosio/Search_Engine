package ir.nimbo.searchengine.kafka;

import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.URLQueue;
import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.*;

public class KafkaManager implements URLQueue {
    private final String topic;
    private final LinkedList<String> tempList = new LinkedList<>();
    private KafkaConsumer<Integer, String> consumer;
    private Producer<Integer, String> producer;

    public KafkaManager(String topic, String portsWithIp, String groupId) {
        this.topic = topic;
        Properties props = new Properties();
        props.put("bootstrap.servers", portsWithIp);
        props.put("group.id", groupId);
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
//        for (String url : page.getLinks().stream().map(Link::getUrl).toArray(String[]::new)) {
//            producer.send(new ProducerRecord<>(topic, url.hashCode(), url));
//        }
//        tempList.clear();
        flush();
    }

    public void shuffle() {
        Collections.shuffle(tempList);
    }

    @Override
    protected void finalize() {
        producer.close();
        consumer.close();
    }
    public void flush() {
        producer.flush();
    }
}
