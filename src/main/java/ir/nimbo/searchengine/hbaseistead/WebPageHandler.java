package ir.nimbo.searchengine.hbaseistead;

import ir.nimbo.searchengine.crawler.FinishedRequest;
import ir.nimbo.searchengine.crawler.WebDocument;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class WebPageHandler  implements FinishedRequest {
    Queue<WebDocument> webDocuments=new SynchronousQueue<>();
    @Override
    public void accept(WebDocument webDocument) {
        webDocuments.add(webDocument);
    }
}
