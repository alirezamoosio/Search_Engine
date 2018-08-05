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

public class Parser implements Runnable {
    private static Logger logger = Logger.getLogger(Crawler.class);
    public static int i = 0;
    private String url;
    private Crawler observer;

    public Parser(String url, Crawler observer) {
        this.url = url;
        this.observer = observer;
    }

//    public WebDocument parse(String url) {
//
//        i++;
//        return webDocument;
//    }

    private void notify(WebDocument webDocument) {
        observer.addPage(webDocument);
    }

    @Override
    public void run() {
        System.out.println(url);
        if (url == null)
            logger.error("null url");
        Document document = null;
        try {
            document = Jsoup.connect(url).validateTLSCertificates(false).get();
        } catch (IOException e) {
            logger.error("Jsoup connection to " + url + " failed");
        }
        WebDocument webDocument = new WebDocument();
        String text = document.text();
        try {
            LanguageDetector.languageCheck(text);
        } catch (IllegalLanguageException e) {
            logger.error("Couldn't recognize url language!");
        }
        Link[] links = new Link[0];
        try {
            links = UrlHandler.getLinks(document.getElementsByTag("a"), new URL(url).getHost());
        } catch (MalformedURLException e) {
            logger.error(url + " is malformatted!");
        }
        webDocument.setTextDoc(text);
        webDocument.setTitle(document.title());
        webDocument.setPagelink(url);
        webDocument.setLinks(Arrays.asList(links));
        notify(webDocument);
    }
}
