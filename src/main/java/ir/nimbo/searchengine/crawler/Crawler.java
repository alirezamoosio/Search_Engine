package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.database.HbaseWebDaoImp;
import ir.nimbo.searchengine.database.WebDao;

import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class Crawler implements Runnable {
    private static Logger logger = Logger.getLogger(Crawler.class);
    private Parser parser;
    private URLQueue urlQueue;
    private List<String> inputUrls;
    private WebDao elasticDao;
    private WebDao hbaseDoa;

    public Crawler(URLQueue urlQueue) {
        hbaseDoa = new HbaseWebDaoImp();
        this.urlQueue = urlQueue;
        parser = Parser.getInstance();
        inputUrls = new ArrayList<>();
        System.out.println("end of crawler constructor");
    }

    @Override
    public void run() {
        for (int i = 0; i < 200; i++) {
            try {
                sleep(35);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Thread thread = new Thread(() -> {
                LinkedList<String> urlsOfThisThread = new LinkedList<>();
                while (true) {
                    if (urlsOfThisThread.size() < 10)
                        urlsOfThisThread.addAll(urlQueue.getUrls());
                    WebDocument webDocument;
                    String url = urlsOfThisThread.pop();
                    try {
                        webDocument = parser.parse(url);
                        urlQueue.pushNewURL(webDocument.getLinks().stream().map(Link::getUrl).toArray(String[]::new));
                        hbaseDoa.put(webDocument);
                    } catch (URLException | DuplicateLinkException | IllegalLanguageException | IOException ignored) {
                    } catch (DomainFrequencyException e) {
                        System.out.println("duplicate Domain");
                    }
                }
            });
            thread.setPriority(MAX_PRIORITY - 1);
            thread.start();
        }
    }
}

