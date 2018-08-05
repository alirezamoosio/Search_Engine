package ir.nimbo.searchengine.crawler;

import java.util.ArrayList;
import java.util.List;

public interface QueueCommunicable {
    List<String> getUrls();

//    void pushNewURLInTempQueue(String... urls);
}
