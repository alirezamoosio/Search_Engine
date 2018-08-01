package ir.nimbo.searchengine.kafka;

import ir.nimbo.searchengine.crawler.QueueCommunicable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class KafkaManager implements QueueCommunicable {
    private final String topic;
    private KafkaConsumer<Integer, String> consumer;
    private Producer<Integer, String> producer;
    public KafkaManager(String topic){
        this.topic = topic;
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.122.143:9092,192.168.122.138:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("max.poll.records", "50");
        props.put("auto.offset.reset", "earliest");
        System.out.println(props.toString());
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topic));
        producer = new KafkaProducer<>(props);
    }

    public synchronized ArrayList<String> getUrls(){
        ArrayList<String> result = new ArrayList<>();
        ConsumerRecords<Integer, String> records = consumer.poll(1000);
        consumer.commitSync();
        for (ConsumerRecord<Integer, String> record : records) {
            result.add(record.value());
        }
        return result;
    }
    @Override
    public String pollNewURL() {
        return null;
    }

    @Override
    public void pushNewURL(String... urls) {
        for (String url: urls) {
            producer.send(new ProducerRecord<>(topic, url.hashCode(), url));
        }
//        producer.close();
    }
}
