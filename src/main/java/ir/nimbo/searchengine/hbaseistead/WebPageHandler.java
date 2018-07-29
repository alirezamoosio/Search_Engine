package ir.nimbo.searchengine.hbaseistead;

import ir.nimbo.searchengine.crawler.FinishedRequestHandler;
import ir.nimbo.searchengine.crawler.WebDocument;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class WebPageHandler  implements FinishedRequestHandler {
    Queue<WebDocument> webDocuments=new ArrayBlockingQueue<>(10000);
    @Override
    public void accept(WebDocument webDocument) {
        webDocuments.add(webDocument);

    }
}
