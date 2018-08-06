package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LanguageDetector;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Thread.sleep;

public class Parser implements Runnable {
    private static Logger logger = Logger.getLogger(Crawler.class);
    public static int numberOFCrawledPage = 0;
    private String url;
    private Crawler observer;
    private static long lastTime=System.currentTimeMillis();
    public Parser(String url, Crawler observer) {
        this.url = url;
        this.observer = observer;
    }

    static {
        new Thread(() -> {
            while (true)
                try {
                    System.out.println("crawledPage="+numberOFCrawledPage);
                    System.out.println(numberOFCrawledPage/(System.currentTimeMillis()-lastTime));
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }).start();
    }

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
            LanguageDetector.languageCheck(text);
            Link[] links = UrlHandler.getLinks(document.getElementsByTag("a"), new URL(url).getHost());
            webDocument.setTextDoc(text);
            webDocument.setTitle(document.title());
            webDocument.setPagelink(url);
            webDocument.setLinks(Arrays.asList(links));
            notify(webDocument);
        } catch (MalformedURLException e) {
            logger.error(url + " is malformatted!");
        } catch (IOException e) {
            logger.error("Jsoup connection to " + url + " failed");
        } catch (IllegalLanguageException e) {
            logger.error("Couldn't recognize url language!");
        }
    }
}
