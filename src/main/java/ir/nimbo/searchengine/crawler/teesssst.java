package ir.nimbo.searchengine.crawler;

import java.util.Scanner;


public class teesssst {
    public static void main(String[] args) {
        new Thread(()->{
            new Crawler(null,null,0).start();
        }).start();
        new Scanner(System.in).nextLine();
    }
}
