package ir.nimbo.searchengine.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class UrlHandlerTest {

    @Test
    public void getLinks() {
        System.out.println(new File(this.getClass().getClassLoader().getResource("profiles").getFile()).getAbsolutePath());
//        UrlHandler urlHandler = new UrlHandler();
//        try {
//            Document document = Jsoup.connect("https://piazza.com/").get();
//            Elements links = document.getElementsByTag("a");
//            urlHandler.getLinks(links, "https://piazza.com/");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Test
    public void normalizeLink() {
    }

    @Test
    public void getLinks1() {
    }
}