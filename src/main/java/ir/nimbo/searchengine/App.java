package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Crawler;
import ir.nimbo.searchengine.hbaseistead.WebPageHandler;
import ir.nimbo.searchengine.kafkainstead.URLQueue;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import static java.lang.Thread.sleep;

/**
 * Hello world!
 */
public class App {
    private static URLQueue queueCommunicable;
    private static WebPageHandler webPageHandler;
    private static Crawler crawler;
    private static long autoMateTime;

    public static void main(String[] args) throws InterruptedException, IOException {
        Document document = Jsoup.connect("http://Wikipedia.org").validateTLSCertificates(false).get();
        String[] firstUrls = document.getElementsByTag("a").stream()
                .map(element -> element.attr("href"))
                .filter(e -> e.startsWith("http")
//                        || e.contains("www.")
                ).toArray(String[]::new);


        queueCommunicable = new URLQueue(543451, 4231, firstUrls);
        webPageHandler = new WebPageHandler();
        crawler = new Crawler(webPageHandler, queueCommunicable, 100);
        new Thread(() -> crawler.start()).start();
        cmd();
    }

    private static void cmd() {
        while (true) {
            try {
                Scanner scanner = new Scanner(System.in);
                String[] inputs = scanner.nextLine().split(" ");
                Object[] args = new String[inputs.length - 1];
                System.arraycopy(inputs, 1, args, 0, args.length);
                Class[] classes = new Class[args.length];
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = String.class;
                }
                if (inputs.length == 1) {

                    Method method = App.class.getMethod(inputs[0]);
                    method.invoke(null);
                } else {
                    Method method = App.class.getMethod(inputs[0], classes);
                    method.invoke(null, args);
                }
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static void addLinkToQueue(String url) {
        queueCommunicable.getURLQueue().offer(url);
    }

    public static void addLinkToTemp(String url) {
        queueCommunicable.pushNewURL(url);
    }

    public static void showTempList() {
        queueCommunicable.getUrlTempList().forEach(System.out::println);
    }

    public static void showUrlQueue() {
        queueCommunicable.getURLQueue().forEach(System.out::println);
    }

    public static void nTemp() {
        System.out.println(queueCommunicable.getUrlTempList().size());
    }

    public static void nQueue() {
        System.out.println(queueCommunicable.getURLQueue().size());
    }

    public static void shuffleTemp() {
        queueCommunicable.shuffle();
    }

    public static void appendTempToQueue() {
        queueCommunicable.appendTempToQueue();
    }

    public static void nCrawled() {
        System.out.println(crawler.getNumberOfCrawled());
    }
    public static void setRefreshTime(String inp){
        autoMateTime=Integer.parseInt(inp);
    }
    public static void automate() {
        while (true){
            try {
                sleep(autoMateTime);
                shuffleTemp();
                appendTempToQueue();
                System.out.println();
                System.out.println();
                System.out.println("EndedShuffeling");
                System.out.println();
                System.out.println();
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


