package ir.nimbo.searchengine.crawler;

@FunctionalInterface
public interface FinishedRequest {
    void accept(WebDocument webDocument);
}
