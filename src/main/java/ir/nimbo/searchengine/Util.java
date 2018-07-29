package ir.nimbo.searchengine;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Util {
    public static Connection getUrlConnection(String url){
        return Jsoup.connect(url).validateTLSCertificates(false);
    }
}
