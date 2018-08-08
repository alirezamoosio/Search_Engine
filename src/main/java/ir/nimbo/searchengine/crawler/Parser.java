package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.log4j.Logger;
import org.jets3t.apps.synchronize.Synchronize;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class Parser implements Runnable {
    private static Logger logger = Logger.getLogger("error");
    private static Logger infoLogger = Logger.getLogger("info");
    public static int numberOFCrawledPage = 0;
    private String url;
    private Crawler observer;
    private LangDetector langDetector;
    private static long lastTime = System.currentTimeMillis();

    static {
        new Thread(() -> {
            try {
                sleep(1000);
            } catch (InterruptedException ignored) {
            }
            System.out.println("crawled page"+numberOFCrawledPage);
            System.out.println("rate of crawl="+numberOFCrawledPage/(System.currentTimeMillis()-lastTime)*1000);
            infoLogger.info("rate of crawl="+numberOFCrawledPage/(System.currentTimeMillis()-lastTime)*1000);
            lastTime=System.currentTimeMillis();
            infoLogger.info(numberOFCrawledPage);
        }).start();
    }

    public Parser(String url, Crawler observer, LangDetector langDetector) {
        this.langDetector = langDetector;
        this.url = url;
        this.observer = observer;

    }

//    static {
//        new Thread(() -> {
//            while (true)
//                try {
//                    System.out.println("crawledPage="+numberOFCrawledPage);
//                    System.out.println(numberOFCrawledPage/(System.currentTimeMillis()-lastTime));
//                    sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//        }).start();
//    }

    private void notify(WebDocument webDocument) {
        observer.addPage(webDocument);
    }

    @Override
    public void run() {
        if (url == null)
            logger.error("null url");
        try {
            Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
            WebDocument webDocument = new WebDocument();
            String text = document.text();
            langDetector.languageCheck(text);
            Link[] links = UrlHandler.getLinks(document.getElementsByTag("a"), new URL(url).getHost());
            webDocument.setTextDoc(text);
            webDocument.setTitle(document.title());
            webDocument.setPagelink(url);
            webDocument.setLinks(Arrays.asList(links));
            notify(webDocument);
            System.out.println(++numberOFCrawledPage);
        } catch (MalformedURLException e) {
            logger.error(url + " is malformatted!");
        } catch (IOException e) {
            logger.error("Jsoup connection to " + url + " failed");
        } catch (IllegalLanguageException e) {
            logger.error("Couldn't recognize url language!");
        } finally {
            observer = null;
            langDetector = null;
        }
    }
}
