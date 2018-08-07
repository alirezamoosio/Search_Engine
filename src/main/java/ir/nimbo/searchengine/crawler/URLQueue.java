package ir.nimbo.searchengine.crawler;

import java.util.ArrayList;
import java.util.List;

public interface URLQueue {
    List<String> getUrls();

    void pushNewURL(WebDocument pages);
}
