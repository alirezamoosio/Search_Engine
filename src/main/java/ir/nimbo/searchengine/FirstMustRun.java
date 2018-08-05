package ir.nimbo.searchengine;

import ir.nimbo.searchengine.kafka.KafkaManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class FirstMustRun {
    public static void main(String[] args) {
        KafkaManager kafkaManager = new KafkaManager("links", "localhost:9092,localhost:9093");
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
    }}

