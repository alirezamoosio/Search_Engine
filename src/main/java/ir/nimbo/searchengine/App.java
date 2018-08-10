package ir.nimbo.searchengine;

import ir.nimbo.searchengine.kafka.KafkaManager;

/**
 * Hello world!
 */
import ir.nimbo.searchengine.crawler.Crawler;

import java.util.Scanner;

public class App {
    private static final String LOCAL_IP = "localhost:9092,localhost:9093";
    private static final String SERVER_IP = "master-node:9092,worker-node:9092";
    public static long timeOFStart = System.currentTimeMillis();
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("main");
        Intiaizer.intialize();
        Thread crawl = new Thread(new Crawler(new KafkaManager( "links",  SERVER_IP,"tes456787654t",90)));
        crawl.start();
    }
}