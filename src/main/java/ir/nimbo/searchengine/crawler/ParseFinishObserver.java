package ir.nimbo.searchengine.crawler;

@FunctionalInterface
public interface ParseFinishObserver {
    void put(WebDocument webDocument);
}
