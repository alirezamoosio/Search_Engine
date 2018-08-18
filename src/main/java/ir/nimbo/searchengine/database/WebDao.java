package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.database.webdocumet.WebDocument;

public interface WebDao {
    boolean createTable();

    void put(WebDocument document);
}
