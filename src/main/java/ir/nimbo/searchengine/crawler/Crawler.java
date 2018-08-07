package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.database.ElasticWebDaoImp;
import ir.nimbo.searchengine.database.HbaseWebDaoImp;
import ir.nimbo.searchengine.database.WebDoa;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class Crawler implements Runnable {
    private static Logger logger = Logger.getLogger(Crawler.class);
    private URLQueue urlQueue;
    private List<String> inputUrls;
    private ExecutorService hbasepool;
    private ExecutorService elasticpool;
    private ScheduledExecutorService taskPool;
    private ExecutorService parserPool;
    private LangDetector langDetector;
//    private WebDoa elasticDao;
//    private WebDoa hbaseDoa;
    public Crawler(URLQueue urlQueue) {
//        WebDoa elasticDao = new ElasticWebDaoImp();
        WebDoa hbaseDoa = new HbaseWebDaoImp();
        hbaseDoa.createTable();
        langDetector = new LangDetector();
        langDetector.profileLoad();
        taskPool = Executors.newScheduledThreadPool(1);
        parserPool = Executors.newFixedThreadPool(200);
        hbasepool = Executors.newFixedThreadPool(100);
        elasticpool = Executors.newFixedThreadPool(100);
        this.urlQueue = urlQueue;
        inputUrls = new ArrayList<>();
    }


    public void addPage(WebDocument page) {
        hbasepool.execute(new Thread(()->{
            WebDoa hbase = new HbaseWebDaoImp();
            hbase.put(page);
        }));
        elasticpool.execute(new Thread(()->{
            WebDoa elasticpool = new HbaseWebDaoImp();
            elasticpool.put(page);
        }));
        System.out.println("done");
//        hbaseDoa.put(page);
//        elasticDao.put(page);

    }

    @Override
    public void run() {
        Thread inputThread = new Thread(() -> {
            inputUrls = urlQueue.getUrls();
            for (String url : inputUrls) {
                parserPool.execute(new Parser(url, this,langDetector));
            }
            inputUrls.clear();
        });
        inputThread.setPriority(MAX_PRIORITY - 2);
        taskPool.scheduleAtFixedRate(inputThread, 0, 50, TimeUnit.MILLISECONDS);
    }
}

