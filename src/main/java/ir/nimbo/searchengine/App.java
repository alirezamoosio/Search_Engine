package ir.nimbo.searchengine;

import ir.nimbo.searchengine.kafka.KafkaManager;
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
import org.jsoup.Jsoup;

import javax.swing.table.TableRowSorter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class App {
    public static long timeOFStart = System.currentTimeMillis();

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Thread crawl = new Thread(new Crawler());
        crawl.start();
    }

}