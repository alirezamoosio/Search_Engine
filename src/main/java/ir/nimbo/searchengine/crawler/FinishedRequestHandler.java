package ir.nimbo.searchengine.crawler;

@FunctionalInterface
public interface FinishedRequestHandler {
    void accept(WebDocument webDocument);
}
