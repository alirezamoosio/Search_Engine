package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.language.LangDetector;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static java.lang.Thread.sleep;

public class Parser {
    private static int numberOfUrlGetted = 0;
    private static int numberOfNull = 0;
    private static int numberOfDuplicate = 0;
    private static int numberOfDomainError = 0;
    private static long lastTime = System.currentTimeMillis();
    private static int numberOFCrawledPage = 0;
    private static int lastNumberOfCrawledPage = 0;
    private static int lastNumberOfDuplicate = 0;
    private static int lastNumberOfDomainError = 0;
    private static int lastNumberOfUrlGetted = 0;
    private static Logger errorLogger = Logger.getLogger("error");
    private static Logger infoLogger = Logger.getLogger("info");
    private static LangDetector langDetector;
    private static Parser parser;
    private static DuplicateLinkHandler duplicateLinkHandler = DuplicateLinkHandler.getInstance();
//    private static DomainIgnore domainIgnore=DomainIgnore.getInstance();
    private static DomainFrequencyHandler domainTimeHandler = DomainFrequencyHandler.getInstance();
    private static int numberOfLanguagePassed = 0;

    private static int lastNumberOfLanguagePassed = 0;
    public static void stat(PrintStream out){
        int delta = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        infoLogger.info("num/rate of getted url     " + numberOfUrlGetted + "\t" + (double) (numberOfUrlGetted - lastNumberOfUrlGetted) / delta);
        infoLogger.info("num/rate of passed lang    " + numberOfLanguagePassed + "\t" + (double) (numberOfLanguagePassed - lastNumberOfLanguagePassed) / delta);
        infoLogger.info("num/rate of duplicate      " + numberOfDuplicate + "\t" + (double) (numberOfDuplicate - lastNumberOfDuplicate) / delta);
        infoLogger.info("num/rate of domain Error   " + numberOfDomainError + "\t" + (double) (numberOfDomainError - lastNumberOfDomainError) / delta);
        infoLogger.info("num/rate of crawl=         " + numberOFCrawledPage + "\t" + (double) (numberOFCrawledPage - lastNumberOfCrawledPage) / delta);
//        infoLogger.info("rate of crawl=" + (double) (numberOFCrawledPage - lastNumberOfCrawledPage) / delta);
        lastNumberOfUrlGetted = numberOfUrlGetted;
        lastNumberOfDuplicate = numberOfDuplicate;
        lastNumberOfDomainError = numberOfDomainError;
        lastNumberOfLanguagePassed = numberOfLanguagePassed;
        lastNumberOfCrawledPage = numberOFCrawledPage;
        lastTime = System.currentTimeMillis();
        infoLogger.info("number of crawled pages "+numberOFCrawledPage);

    }
    static {
        new Thread(() -> {
            while (true) {
                try {
                    sleep(20000);
                    stat(System.out);
                } catch (InterruptedException ignored) {
                }
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
            errorLogger.error("number of null" + numberOfNull++);
            throw new URLException();
        } else if (!domainTimeHandler.isAllow(url)) {
            errorLogger.error("take less than 30s to request to "+url);
            numberOfDomainError++;
            throw new DomainFrequencyException();
        }
        String text = null;
        try {
            if (duplicateLinkHandler.isDuplicate(url)) {
                errorLogger.error(url+" is duplicate");
                numberOfDuplicate++;
                throw new DuplicateLinkException();
            }
            duplicateLinkHandler.confirm(url);

            Document document = Jsoup.connect(url).validateTLSCertificates(false).get();
            numberOfUrlGetted++;
            WebDocument webDocument = new WebDocument();
            text = document.text();
            checkLanguage(document, text);
            numberOfLanguagePassed++;
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
            errorLogger.error("Couldn't recognize url language!" + url);
            throw e;
        }
    }

    private void checkLanguage(Document document, String text) throws IllegalLanguageException {
        try {
            String lang = document.getElementsByAttribute("lang").get(0).attr("lang").toLowerCase();
            if (lang.equals("en") || lang.startsWith("en-")) {
                return;
            }
            throw new IllegalLanguageException();
        } catch (RuntimeException e) {
            langDetector.languageCheck(text);
        }
    }
}

