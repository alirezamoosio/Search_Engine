package ir.nimbo.searchengine.crawler;

import java.util.List;

public interface QueueCommunicable {
    List<String> getUrls();

    void pushNewURL(String... url);
}
