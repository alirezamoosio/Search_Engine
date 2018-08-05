package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Crawler;
import ir.nimbo.searchengine.crawler.Parser;
import ir.nimbo.searchengine.crawler.WebDocument;
import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirstMustRun {
    private static Logger logger = Logger.getLogger(FirstMustRun.class);
    private static String topic;
    private static String portsWithId;

    public static void initializer(String topic,String portsWithId) {
        FirstMustRun.topic = topic;
        FirstMustRun.portsWithId = portsWithId;
        KafkaManager kafkaManager = new KafkaManager(topic, portsWithId);
        LinkedList<String> linkedList =new LinkedList<>();
        linkedList.add("https://www.alexa.com/");
        linkedList.add("https://en.wikipedia.org/wiki/Main_Page");
        linkedList.add("http://docs.google.com/");
        linkedList.add("http://softwaregarden.com/");
        linkedList.add("https://basecamp.com/");
        linkedList.add("https://www.synacor.com/");
        linkedList.add("https://newsgator.com/");
        linkedList.add("https://alternativeto.net/");
        linkedList.add("https://en-maktoob.yahoo.com/");
        linkedList.add("https://eurekalert.org/");
        linkedList.add("https://www.space.com/");
        linkedList.add("http://www.realclimate.org/");
        linkedList.add("https://www.alphagalileo.org/en-gb/");
        linkedList.add("https://www.nasa.gov/");
        linkedList.add("https://www.encyclopedia.com/");
        linkedList.add("https://www.theyworkforyou.com/");
        kafkaManager.pushNewURL(linkedList.toArray(new String [0]));
        kafkaManager.flush();
    }

    public static void updateKafka() {
        KafkaManager kafkaManager = new KafkaManager(topic,portsWithId);
        ArrayList<String> temp = kafkaManager.getUrls();
        temp.stream().parallel().forEach(e -> {
            try {
                WebDocument webDocument = Parser.parse(e);
                webDocument.getLinks().forEach(link -> kafkaManager.pushNewURLInTempQueue(link.getUrl()));
            } catch (RuntimeException e1) {
                logger.error("error while pushing links of " + e);
            }
        });
        kafkaManager.shuffle();
        kafkaManager.addTempListToQueue();}
}

