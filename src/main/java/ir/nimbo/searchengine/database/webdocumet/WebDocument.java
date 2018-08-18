package ir.nimbo.searchengine.database.webdocumet;

import ir.nimbo.searchengine.database.webdocumet.Link;

import java.util.ArrayList;
import java.util.List;

public class WebDocument {
    private static final int NUMBER_OF_OWN_LINK_READ = 5;
    private String textDoc;
    private ArrayList<Link> links;
    private String title;
    private String pagelink;

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = (ArrayList<Link>) links;
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
