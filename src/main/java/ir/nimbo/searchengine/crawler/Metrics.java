package ir.nimbo.searchengine.crawler;

import org.apache.log4j.Logger;

import java.io.PrintStream;

import static java.lang.Thread.sleep;

public class Metrics {
    static int numberOfUrlGetted = 0;
    static int numberOfNull = 0;
    static int numberOfDuplicate = 0;
    static int numberOfDomainError = 0;
    static long byteCounter=0;
    static int numberOFCrawledPage = 0;
    static int numberOfLanguagePassed = 0;
    private static long lastTime = System.currentTimeMillis();
    private static Logger infoLogger = Logger.getLogger("info");
    private static int lastNumberOfLanguagePassed = 0;
    private static int lastNumberOfCrawledPage = 0;
    private static int lastNumberOfDuplicate = 0;
    private static int lastNumberOfDomainError = 0;
    private static int lastNumberOfUrlGetted = 0;

    public static void stat(PrintStream out) {
        int delta = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        out.println("received MB     " + (byteCounter >> 20));
        out.println("num/rate of getted url     " + numberOfUrlGetted + "\t" + (double) (numberOfUrlGetted - lastNumberOfUrlGetted) / delta);
        out.println("num/rate of passed lang    " + numberOfLanguagePassed + "\t" + (double) (numberOfLanguagePassed - lastNumberOfLanguagePassed) / delta);
        out.println("num/rate of duplicate      " + numberOfDuplicate + "\t" + (double) (numberOfDuplicate - lastNumberOfDuplicate) / delta);
        out.println("num/rate of domain Error   " + numberOfDomainError + "\t" + (double) (numberOfDomainError - lastNumberOfDomainError) / delta);
        out.println("num/rate of crawl=         " + numberOFCrawledPage + "\t" + (double) (numberOFCrawledPage - lastNumberOfCrawledPage) / delta);
        infoLogger.info("rate of crawl=" + (double) (numberOFCrawledPage - lastNumberOfCrawledPage) / delta);
        lastNumberOfUrlGetted = numberOfUrlGetted;
        lastNumberOfDuplicate = numberOfDuplicate;
        lastNumberOfDomainError = numberOfDomainError;
        lastNumberOfLanguagePassed = numberOfLanguagePassed;
        lastNumberOfCrawledPage = numberOFCrawledPage;
        lastTime = System.currentTimeMillis();
        infoLogger.info(numberOFCrawledPage);

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


}
