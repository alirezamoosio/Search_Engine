package ir.nimbo.searchengine.crawler;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {
    private FinishedRequest request;

    public Crawler(FinishedRequest request, Communicable communicable) {
        this.request = request;
        this.communicable = communicable;
    }

    private Communicable communicable;
    private ExecutorService executor = Executors.newFixedThreadPool(5);
    private boolean isWorking = true;

    public void setFinishedRequest(FinishedRequest request) {
        this.request = request;
    }

    public void setCommunicable(Communicable communicable) {
        this.communicable = communicable;
    }

    public void start() {
        while (isWorking) {
            String newURL = communicable.pullNewURL();
            executor.execute(() -> {
                Parser parser = new Parser(newURL);
                WebDocument document = null;
                try {
                    document = parser.parse();
                    request.accept(document);
                    communicable.pushNewURL(document.links);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            });
        }
    }

    public void shutDown() {
        isWorking = false;
    }
}
