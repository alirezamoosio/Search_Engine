package ir.nimbo.searchengine;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import java.io.IOException;

/**
 * Hello world!
 */
import ir.nimbo.searchengine.crawler.Crawler;
import ir.nimbo.searchengine.crawler.Parser;

import javax.swing.table.TableRowSorter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Scanner;

public class App {
    public static long timeOFStart=System.currentTimeMillis();

    public static Scanner scanner=new Scanner(System.in);
    public static void main( String[] args ) {
//        Crawler crawler = new Crawler("links","master-node:9092,worker-node:9092",200);
        Thread crawl = new Thread(new Crawler("links","master-node:9092,worker-node:9092"));
        crawl.start();
//        while (true){
//            String input=scanner.nextLine();
//            String methodName=input[0];
//            for (int i = 1; i < input.length; i++) {
//                methodName.concat(input[i].)
//            }
//            try {
//                Method method=App.class.getMethod(input);
//                method.invoke(null);
//            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
//                System.out.println(e.getMessage());
//            }
//        }
    }
//    public static void startFirstMustRun(){
//        FirstMustRun.initializer("links","master-node:9092,worker-node:9092");
//    }
//    public static void start(){
//        System.out.println("start");
//        new Thread(()->new Crawler("links","master-node:9092,worker-node:9092",200).start()).start();
//    }
//    public static void runForUpdateKafka(){
//        FirstMustRun.updateKafka();
//    }
//    public static void timeOfSystemUp(){
//        System.out.println(System.currentTimeMillis()-timeOFStart);
//    }
//    public static void countOfCrawledPage(){
//        System.out.println(Parser.i);
//    }
}

