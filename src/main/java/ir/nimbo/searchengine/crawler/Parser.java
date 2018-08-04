package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LanguageDetector;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.apache.kafka.common.protocol.types.Field;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {
    public static  WebDocument parse(String url) throws IOException, IllegalLanguageException {
        if (url == null)
            return null;
        Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
        WebDocument webDocument = new WebDocument();
        String text=document.text();
        LanguageDetector.languageCheck(text);
        Link [] links=UrlHandler.getLinks(document.getElementsByTag("a"),new URL(url).getHost());
        webDocument.setTextDoc(text);
        webDocument.setTitle(document.title());
        webDocument.setPagelink(url);
        webDocument.setLinks(Arrays.asList(links));
        return webDocument;
    }
}
