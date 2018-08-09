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
    private static Logger errorLogger = Logger.getLogger("error");
    private Parser parser;
    private URLQueue urlQueue;
    private List<String> inputUrls;
    private WebDao elasticDao;
    private WebDao hbaseDoa;
    private int counter;

    public Crawler(URLQueue urlQueue) {
//        hbaseDoa = new HbaseWebDaoImp();
        this.urlQueue = urlQueue;
        parser = Parser.getInstance();
        inputUrls = new ArrayList<>();
        System.out.println("end of crawler constructor");
    }

    @Override
    public void run() {
        System.out.println("runned");
        for (int i = 0; i < 200; i++) {
            System.out.println("thread "+i);
            try {
                sleep(35);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int finalI = i;
            Thread thread = new Thread(() -> {
                LinkedList<String> urlsOfThisThread = new LinkedList<>();

                System.out.println("while true start"+ finalI);
                while (true) {
                    System.out.println("in start"+ finalI);
                    if (urlsOfThisThread.size() < 10) {
                        System.out.println("cc" + counter);
                        List<String> list = urlQueue.getUrls();
                        System.out.println(list.size());
                        urlsOfThisThread.addAll(list);
                        System.out.println("finished");
                    } else {
                        WebDocument webDocument;
                        String url = urlsOfThisThread.pop();
                        try {
                            webDocument = parser.parse(url);
                            counter += webDocument.getTextDoc().getBytes().length;
//                        urlQueue.pushNewURL(webDocument.getLinks().stream().map(Link::getUrl).toArray(String[]::new));
//                        hbaseDoa.put(webDocument);
                        } catch (URLException | DuplicateLinkException | IllegalLanguageException | IOException ignored) {
                        } catch (DomainFrequencyException e) {
//                            System.out.println("duplicate Domain");
                        }
                    }
                }
            });
            thread.setPriority(MAX_PRIORITY - 1);
            thread.start();
        }
    }
}

