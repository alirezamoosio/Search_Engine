package ir.nimbo.searchengine;

import ir.nimbo.searchengine.kafka.KafkaManager;

/**
 * Hello world!
 */
import ir.nimbo.searchengine.crawler.Crawler;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class App {
    private static final String LOCAL_IP = "localhost:9092,localhost:9093";
    private static final String SERVER_IP = "master-node:9092,worker-node:9092";
    public static long timeOFStart = System.currentTimeMillis();
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws InterruptedException {
        System.out.println("main");
        Intiaizer.intialize();
        KafkaManager main=new KafkaManager( "links",  LOCAL_IP,"tes4569787654t",90);
        KafkaManager helper=new KafkaManager("helper",LOCAL_IP,"test",800);
        Thread crawl = new Thread(new Crawler(main,helper));
        crawl.start();
        manageKafkaHelper(main,helper);


    }

    private static void manageKafkaHelper(KafkaManager main, KafkaManager helper) throws InterruptedException {
        LinkedList<String> linkedList=new LinkedList<>();
        while (true){
            sleep(531);
            linkedList.addAll(helper.getUrls());
            System.out.println(linkedList.size());
            if (linkedList.size()>60000){
                System.out.println("shuffle and add");
                Collections.shuffle(linkedList);
                main.pushNewURL(linkedList.toArray(new String [0]));
                linkedList.clear();
            }
        }

    }
}