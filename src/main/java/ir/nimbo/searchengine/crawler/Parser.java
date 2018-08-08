package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class Parser {
    private static Logger logger = Logger.getLogger("error");
    private static Logger infoLogger = Logger.getLogger("info");
    private static int numberOFCrawledPage = 0;
    private static int lastNumberOfCrawledPage = 0;
    private static long lastTime = System.currentTimeMillis();
    private static LangDetector langDetector;
    private static Parser parser;
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
//    public  Parser(String url, Crawler observer, LangDetector langDetector) {
//        this.langDetector = langDetector;
//        this.url = url;
//        this.observer = observer;
//    }

//    private void notify(WebDocument webDocument) {
//        observer.addPage(webDocument);
//    }

    public WebDocument parse(String url) throws DomainFrequencyException {
        if (url == null || domainTimeHandler.isAllow(url)) {
            logger.error("null url");
            throw new DomainFrequencyException();
        }
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
            logger.error(url + " is malformatted!");
        } catch (IOException e) {
            logger.error("Jsoup connection to " + url + " failed");
        } catch (IllegalLanguageException e) {
            logger.error("Couldn't recognize url language!");
        }
        return null;
    }
}
