package ir.nimbo.searchengine;

import ir.nimbo.searchengine.kafka.KafkaManager;

public class FirstMustRun {
    public static void main(String[] args) {
        KafkaManager kafkaManager = new KafkaManager("links", "localhost:9092");
        kafkaManager.pushNewURL("https://www.alexa.com/");
        kafkaManager.pushNewURL("https://en.wikipedia.org/wiki/Main_Page");
        kafkaManager.pushNewURL("http://docs.google.com/");
        kafkaManager.pushNewURL("http://softwaregarden.com/");
        kafkaManager.pushNewURL("https://basecamp.com/");
        kafkaManager.pushNewURL("https://www.synacor.com/");
        kafkaManager.pushNewURL("https://newsgator.com/");
        kafkaManager.pushNewURL("https://alternativeto.net/");
        kafkaManager.pushNewURL("https://en-maktoob.yahoo.com/");
        kafkaManager.pushNewURL("https://eurekalert.org/");
        kafkaManager.pushNewURL("https://www.space.com/");
        kafkaManager.pushNewURL("http://www.realclimate.org/");
        kafkaManager.pushNewURL("https://www.alphagalileo.org/en-gb/");
        kafkaManager.pushNewURL("https://www.nasa.gov/");
        kafkaManager.pushNewURL("https://www.encyclopedia.com/");
        kafkaManager.pushNewURL("https://www.theyworkforyou.com/");
    }
}

