package ir.nimbo.searchengine.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {
    private FinishedRequest request;
    private Communicable communicable;
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public void setFinishedRequest(FinishedRequest request) {
        this.request = request;
    }

    public void setCommunicable(Communicable communicable) {
        this.communicable = communicable;
    }

}
