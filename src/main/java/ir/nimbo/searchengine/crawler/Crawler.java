package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.domainvalidation.UrlHandler;
import ir.nimbo.searchengine.database.ElasticWebDaoImp;
import ir.nimbo.searchengine.database.HbaseWebDaoImp;
import ir.nimbo.searchengine.database.WebDao;

import ir.nimbo.searchengine.database.webdocumet.WebDocument;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import ir.nimbo.searchengine.metrics.Metrics;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class Crawler implements Runnable {
    private static Logger errorLogger = Logger.getLogger("error");
    private Parser parser;
    private URLQueue urlQueue;
    private URLQueue tempUrlQueue;
    private List<String> inputUrls;
    private WebDao elasticDao;
    private WebDao hbaseDoa;

    public Crawler(URLQueue urlQueue, URLQueue tempUrlQueue) {
        this.urlQueue = urlQueue;
        this.tempUrlQueue = tempUrlQueue;
        parser = Parser.getInstance();
        inputUrls = new ArrayList<>();
        System.out.println("end of crawler constructor");
        hbaseDoa = new HbaseWebDaoImp();
        hbaseDoa.createTable();
        elasticDao = new ElasticWebDaoImp();
    }

    @Override
    public void run() {
        for (int i = 0; i < 400; i++) {
            try {
                sleep(35);
            } catch (InterruptedException ignored) {
            }
            int finalI = i;
            Thread thread = new Thread(() -> {
                LinkedList<String> urlsOfThisThread = new LinkedList<>(urlQueue.getUrls());
                while (true) {
                    if (urlsOfThisThread.size() < 10) {
                        List<String> list = urlQueue.getUrls();
                        urlsOfThisThread.addAll(list);
                    } else {
                        WebDocument webDocument;
                        String url = urlsOfThisThread.pop();
                        try {
                            webDocument = parser.parse(url);
                            Metrics.byteCounter += webDocument.getTextDoc().getBytes().length;
                            tempUrlQueue.pushNewURL(giveGoodLink(webDocument));
                            hbaseDoa.put(webDocument);
                            elasticDao.put(webDocument);
                        } catch (RuntimeException e) {
                            errorLogger.error("important" + e.getMessage());
                        } catch (URLException | DuplicateLinkException | IOException | IllegalLanguageException | DomainFrequencyException ignored) {
                        }
                    }
                }
            });
            thread.setPriority(MAX_PRIORITY - 1);
            thread.start();
        }
    }

    private String[] giveGoodLink(WebDocument webDocument) throws MalformedURLException {
        ArrayList<String> externalLink = new ArrayList<>();
        ArrayList<String> internalLink = new ArrayList<>();
        UrlHandler.splitter(webDocument.getLinks(), internalLink, externalLink, new URL(webDocument.getPagelink()).getHost());
        if (internalLink.size() > 10) {
            Collections.shuffle(internalLink);
            externalLink.addAll(internalLink.subList(0, 10));
        } else
            externalLink.addAll(internalLink);
        return externalLink.toArray(new String[0]);
    }
}

