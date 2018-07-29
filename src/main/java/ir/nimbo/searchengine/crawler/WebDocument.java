package ir.nimbo.searchengine.crawler;

import org.jsoup.nodes.Document;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebDocument {

    private String textDoc;
    private List<String> links;
    private String title;
    private String pagelink;
    public WebDocument(Document document,String pagelink) {
        this.pagelink=pagelink;
        links = Arrays.asList(document.getElementsByTag("a").stream()
                .map(element -> element.attr("href")).filter(e->e.startsWith("http")||e.contains("www."))
                .toArray(String[]::new));
        links.forEach(System.out::println);
        title = document.title();
        textDoc = document.body().text();

    }

    public List<String> getLinks() {
        return links;
    }

    public void setLinks(List<String> links) {
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
