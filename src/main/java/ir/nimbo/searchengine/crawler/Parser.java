package ir.nimbo.searchengine.crawler;
import ir.nimbo.searchengine.exception.IllegalLangaugeException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;

public class Parser {
    public static synchronized WebDocument parse(String url) throws IOException, IllegalLangaugeException {
        if (url == null)
            return null;
        Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
        WebDocument webDocument=new WebDocument(document,url);
        LanguageDetector.languageCheck(webDocument.getTextDoc());
        return webDocument;
    }


}
