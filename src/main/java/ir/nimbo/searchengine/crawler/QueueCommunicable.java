package ir.nimbo.searchengine.crawler;

public interface QueueCommunicable {
    String pollNewURL();

    void pushNewURL(String... url);
}
