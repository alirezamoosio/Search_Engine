package ir.nimbo.searchengine.pagerank;

import java.io.Serializable;
import java.util.List;

public class Value implements Serializable {
    Double pageRank;
    List<String> outLinks;

    Value(List<String> outLinks, Double pageRank) {
        this.pageRank = pageRank;
        this.outLinks = outLinks;
    }

    @Override
    public String toString() {
        return pageRank.toString();
    }
}
