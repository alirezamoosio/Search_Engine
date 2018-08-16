package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.DuplicateLinkHandler;
import ir.nimbo.searchengine.crawler.Parser;
import ir.nimbo.searchengine.kafka.KafkaManager;

/**
 * Hello world!
 */
import ir.nimbo.searchengine.crawler.Crawler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class App {
    private static final String LOCAL_IP = "localhost:9092,localhost:9093";
    private static final String SERVER_IP = "master-node:9092,worker-node:9092";
//    public static long timeOFStart = System.currentTimeMillis();
//    public static Scanner scanner = new Scanner(System.in);
    private static int shuffleSize = 200000;
    public static void main(String[] args) throws InterruptedException, IOException {
        DuplicateLinkHandler.getInstance().loadHashTable();
        System.out.println("main");
        Intiaizer.intialize();
        KafkaManager main = new KafkaManager("links", SERVER_IP, "tes4654t", 90);
        KafkaManager helper = new KafkaManager("helper", SERVER_IP, "test", 2000);
        Thread crawl = new Thread(new Crawler(main, helper));
        crawl.start();
        new Thread(Listener::listen).start();
        manageKafkaHelper(main, helper);

    }

    private static void manageKafkaHelper(KafkaManager main, KafkaManager helper) throws InterruptedException {
        LinkedList<String> linkedList = new LinkedList<>();
        while (true) {
            sleep(2000);
            linkedList.addAll(helper.getUrls());
            if (linkedList.size() > shuffleSize) {
                Collections.shuffle(linkedList);
                main.pushNewURL(linkedList.toArray(new String[0]));
                linkedList.clear();
            }
        }

    }
}