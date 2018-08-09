
package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Parser;
import ir.nimbo.searchengine.crawler.UrlHandler;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FirstMustRun {
    private static Logger logger = Logger.getLogger(FirstMustRun.class);
    private static String topic;
    private static String portsWithId;

    public static void main(String[] args) {
        Intiaizer.intialize();
        initializer2("firstTest");
    }

    private static void initializer2(String topic) {
        FirstMustRun.topic = topic;
        KafkaManager kafkaManager = new KafkaManager(topic);
        List<String> list=kafkaManager.getUrls();
        ArrayList<String > urls=new ArrayList<>();
        list.forEach(e-> {
            try {
                UrlHandler.splitter(Parser.getInstance().parse(e).getLinks(),new ArrayList<>(),urls,new URL(e).getHost());
            } catch (IllegalLanguageException | IOException | URLException | DomainFrequencyException | DuplicateLinkException e1) {
                e1.printStackTrace();
            }
        });
        kafkaManager.pushNewURL(urls.toArray(new String[0]));
        kafkaManager.flush();

    }

    public static void initializer(String topic) {
        FirstMustRun.topic = topic;
        KafkaManager kafkaManager = new KafkaManager(topic);
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
        kafkaManager.pushNewURL(linkedList.toArray(new String[0]));
        kafkaManager.flush();
    }

}