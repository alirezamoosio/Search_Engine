package ir.nimbo.searchengine.fetcher;

import java.util.List;

public interface URLQueue<S> {
    List<S> getUrls();

    void addUrls(List<S> url);

    int size();
}
