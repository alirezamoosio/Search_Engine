package ir.nimbo.searchengine.crawler;

@FunctionalInterface
public interface ParseFinishObserver {
    void accept(WebDocument webDocument);
}
