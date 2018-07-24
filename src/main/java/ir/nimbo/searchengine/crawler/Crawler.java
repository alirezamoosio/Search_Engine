package ir.nimbo.searchengine.crawler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Crawler {
    private FinishedRequest request;
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
                WebDocument document = parser.parse();
                request.accept(document);
            });
        }
    }

    public void shutDown() {
        isWorking = false;
    }
}
