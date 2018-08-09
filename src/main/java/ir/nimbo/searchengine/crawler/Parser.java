package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Parser {
    private static Logger errorLogger = Logger.getLogger("error");
    private static Logger infoLogger = Logger.getLogger("info");
    private static int numberOFCrawledPage = 0;
    private static int lastNumberOfCrawledPage = 0;
    private static long lastTime = System.currentTimeMillis();
    private static LangDetector langDetector;
    private static Parser parser;
    private static DuplicateLinkHandler duplicateLinkHandler = DuplicateLinkHandler.getInstance();
    private static DomainFrequencyHandler domainTimeHandler = DomainFrequencyHandler.getInstance();

    static {
        new Thread(() -> {
            while (true) {
                try {
                    sleep(1000);
                } catch (InterruptedException ignored) {
                }
                System.out.println("crawled page" + numberOFCrawledPage);
                System.out.println("rate of crawl=" + (double) (numberOFCrawledPage - lastNumberOfCrawledPage) / (System.currentTimeMillis() - lastTime) * 1000);
                infoLogger.info("rate of crawl=" + (double) (numberOFCrawledPage - lastNumberOfCrawledPage) / (System.currentTimeMillis() - lastTime) * 1000);
                lastNumberOfCrawledPage = numberOFCrawledPage;
                lastTime = System.currentTimeMillis();
                infoLogger.info(numberOFCrawledPage);
            }
        }).start();

    }


    public static Parser getInstance() {
        if (parser == null)
            parser = new Parser();
        return parser;
    }

    public static void setLangDetector(LangDetector langDetector) {
        Parser.langDetector = langDetector;
    }

    public WebDocument parse(String url) throws IllegalLanguageException, IOException, URLException, DuplicateLinkException, DomainFrequencyException {
        if (url == null) {
            errorLogger.error("url Error");
            throw new URLException();
        } else if (!domainTimeHandler.isAllow(url)) {
            errorLogger.error("url Error");
            throw new DomainFrequencyException();
        } else if (duplicateLinkHandler.isDuplicate(url)) {
            errorLogger.error("url Error");
            throw new DuplicateLinkException();
        }
        duplicateLinkHandler.confirm(url);

        try {
            Document document = Jsoup.connect(url).validateTLSCertificates(false).ignoreHttpErrors(true).get();
            WebDocument webDocument = new WebDocument();
            String text = document.text();
            langDetector.languageCheck(text);
            Link[] links = UrlHandler.getLinks(document.getElementsByTag("a"), new URL(url).getHost());
            webDocument.setTextDoc(text);
            webDocument.setTitle(document.title());
            webDocument.setPagelink(url);
            webDocument.setLinks(Arrays.asList(links));
            numberOFCrawledPage++;
            return webDocument;
        } catch (MalformedURLException e) {
            errorLogger.error(url + " is malformatted!");
            throw e;
        } catch (IOException e) {
            errorLogger.error("Jsoup connection to " + url + " failed");
            throw e;
        } catch (IllegalLanguageException e) {
            errorLogger.error("Couldn't recognize url language!");
            throw e;
        }
    }
}
