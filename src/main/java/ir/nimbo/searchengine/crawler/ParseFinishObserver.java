package ir.nimbo.searchengine.crawler;


public interface ParseFinishObserver{
    void put(WebDocument webDocument);
    boolean createTable();

}
