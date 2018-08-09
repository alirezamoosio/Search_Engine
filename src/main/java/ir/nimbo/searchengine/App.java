package ir.nimbo.searchengine;

import ir.nimbo.searchengine.kafka.KafkaManager;

/**
 * Hello world!
 */
import ir.nimbo.searchengine.crawler.Crawler;

import java.util.Scanner;

public class App {
    public static long timeOFStart = System.currentTimeMillis();
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.println("main");
        Intiaizer.intialize();
        Thread crawl = new Thread(new Crawler(new KafkaManager( "links","master-node:9092,worker-node:9092","tes456787654t",90)));
        crawl.start();
    }
}