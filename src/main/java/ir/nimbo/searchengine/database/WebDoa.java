package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;

import java.util.List;

public interface WebDoa {
    void put(List<WebDocument> documents);
}
