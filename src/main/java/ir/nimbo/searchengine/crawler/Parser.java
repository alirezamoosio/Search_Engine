package ir.nimbo.searchengine.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Parser {
    private String url;

    public Parser(String url) {
        this.url = url;
    }

    public WebDocument parse() throws IOException {
        Document document= Jsoup.connect(url).validateTLSCertificates(false).get();
        return new WebDocument(document);

    }


}
