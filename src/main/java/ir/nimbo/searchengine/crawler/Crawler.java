package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.database.ElasticWebDaoImp;
import ir.nimbo.searchengine.database.HbaseWebDaoImp;
import ir.nimbo.searchengine.database.WebDoa;
import ir.nimbo.searchengine.kafka.KafkaManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.MAX_PRIORITY;
import static java.lang.Thread.sleep;

public class Crawler implements Runnable {
    private static Logger logger = Logger.getLogger(Crawler.class);
    private Parser parser;
    private URLQueue urlQueue;
    private List<String> inputUrls;
    private ExecutorService hbasepool;
    private ExecutorService elasticpool;
    private ScheduledExecutorService taskPool;
    private ExecutorService parserPool;
    private LangDetector langDetector;
    private ExecutorService kafkaout;
    private WebDoa elasticDao;
//    private WebDoa hbaseDoa;
    public Crawler() {
        elasticDao = new ElasticWebDaoImp();
//        hbaseDoa = new HbaseWebDaoImp();
//        hbaseDoa.createTable();
        langDetector = new LangDetector();
        langDetector.profileLoad();
        taskPool = Executors.newScheduledThreadPool(1);
        parserPool = Executors.newFixedThreadPool(100);
        hbasepool = Executors.newFixedThreadPool(1);
        kafkaout = Executors.newFixedThreadPool(1);
        elasticpool = Executors.newFixedThreadPool(1);
        urlQueue = new KafkaManager();
        inputUrls = new ArrayList<>();
        parser=Parser.getInstance();
        Parser.setLangDetector(langDetector);
        System.out.println("end of crawler constructor");
    }


    public void addPage(WebDocument page) {
//        hbasepool.execute(new Thread(()->{
//            hbaseDoa.put(page);
//        }));
        kafkaout.execute(new Thread(()->{
             urlQueue.pushNewURL(page);
        }));
        elasticpool.execute(new Thread(()->{
            elasticDao.put(page);
        }));
//        urlQueue.pushNewURL(page);
//        hbaseDoa.put(page);
       // elasticDao.put(page);

    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            Thread thread= new Thread(() -> {
                LinkedList<String> urlsOfThisThread=new LinkedList<>();
                while (true){
                    if(urlsOfThisThread.size()<10)
                        urlsOfThisThread.addAll(urlQueue.getUrls());
                    WebDocument webDocument=parser.parse(urlsOfThisThread.pop());

                }
            });
            thread.setPriority(MAX_PRIORITY-1);
            thread.start();
        }
//        Thread inputThread = new Thread(() -> {
//            inputUrls = urlQueue.getUrls();
//            for (String url : inputUrls) {
//                parserPool.execute(new Parser(url, this,langDetector));
//            }
//            inputUrls.clear();
//        });
//        inputThread.setPriority(MAX_PRIORITY - 1);
//        taskPool.scheduleAtFixedRate(inputThread, 0, 100, TimeUnit.MILLISECONDS);
    }
}

