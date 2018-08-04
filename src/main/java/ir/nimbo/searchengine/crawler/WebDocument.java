package ir.nimbo.searchengine.crawler;

import org.jsoup.nodes.Document;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebDocument {
    private static final int NUMBER_OF_OWN_LINK_READ = 5;
    private String textDoc;
    private List<Link> links;
    private String title;
    private String pagelink;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextDoc() {
        return textDoc;
    }

    public void setTextDoc(String textDoc) {
        this.textDoc = textDoc;
    }

    public String getPagelink() {
        return pagelink;
    }

    public void setPagelink(String pagelink) {
        this.pagelink = pagelink;
    }


}
