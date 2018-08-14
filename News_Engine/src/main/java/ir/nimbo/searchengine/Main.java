package ir.nimbo.searchengine;


import ir.nimbo.searchengine.trend.WordCounter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Hello world!
 */
public class Main {
    public static void main(String[] args) throws IOException {
        WordCounter counter = new WordCounter();
        Document document = Jsoup.connect("https://bleacherreport.com/articles/2790825-indians-leonys-martin-remains-in-hospital-with-undisclosed-illness?utm_source=cnn.com&utm_medium=referral&utm_campaign=editorial")
                .validateTLSCertificates(false).get();
        counter.setDocument(document.text());
        System.out.println(counter.tokenize());
    }
}
