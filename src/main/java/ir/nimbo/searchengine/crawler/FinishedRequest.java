package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.WebDocument;

@FunctionalInterface
public interface FinishedRequest {
    void accept(WebDocument webDocument);
}
