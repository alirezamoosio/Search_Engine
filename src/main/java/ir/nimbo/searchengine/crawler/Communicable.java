package ir.nimbo.searchengine.crawler;

public interface Communicable {
    String pullNewURL();

    void pushNewURL(String... url);
}
