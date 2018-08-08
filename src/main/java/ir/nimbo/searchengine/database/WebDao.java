package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.ParseFinishObserver;
import ir.nimbo.searchengine.crawler.WebDocument;

import java.util.List;

public interface WebDao extends ParseFinishObserver {
    boolean createTable();
    void put(WebDocument document);
}
