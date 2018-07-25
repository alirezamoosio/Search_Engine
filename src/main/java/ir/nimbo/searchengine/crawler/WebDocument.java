package ir.nimbo.searchengine.crawler;

import org.jsoup.nodes.Document;

import java.util.List;

public class WebDocument {
    String[] links ;
    String title;
    String cashedHtml;
    public WebDocument(Document document) {
        links =(String[]) document.getElementsByTag("a").stream().map(element -> element.attr("href")).toArray();
        title=document.title();
        cashedHtml=document.outerHtml();

    }


}
