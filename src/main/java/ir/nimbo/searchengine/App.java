package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Crawler;
import ir.nimbo.searchengine.crawler.FinishedRequestHandler;
import ir.nimbo.searchengine.crawler.QueueCommunicable;
import ir.nimbo.searchengine.database.HbaseWebDaoImp;
import ir.nimbo.searchengine.hbaseistead.WebPageHandler;
import ir.nimbo.searchengine.kafkainstead.URLQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws InterruptedException, IOException {

        HbaseWebDaoImp hb = new HbaseWebDaoImp();
//        Document document=Jsoup.connect("https://en.wikipedia.org/wiki/Main_Page").validateTLSCertificates(false).get();
//        String []firstUrls=document.getElementsByTag("a").stream()
//                .map(element -> element.attr("href"))
//                .filter(e->e.startsWith("http")||e.contains("www.")).toArray(String[]::new);
//
//        QueueCommunicable queueCommunicable=new URLQueue(543451,4231,firstUrls);
//        FinishedRequestHandler webPageHandler=new WebPageHandler();
//        Crawler crawler =new Crawler(webPageHandler,queueCommunicable,100);
//        crawler.start();
    }
}

