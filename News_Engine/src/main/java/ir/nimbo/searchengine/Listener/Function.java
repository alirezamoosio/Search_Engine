package ir.nimbo.searchengine.Listener;

import ir.nimbo.searchengine.template.SiteTemplates;

import java.io.PrintStream;
import java.util.Scanner;

public class Function {

    @CLI(help = "give you last news of each site")
    public static void lastNews(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "give you trend words in last Hour(for news)")
    public static void newsTrendWordInLastHour(PrintStream out, Scanner scanner) {
// TODO: 8/16/18
    }

    @CLI(help = "give you trend words in last Day(for news)")
    public static void newsTrendWordInLastDay(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "give you trend words in last Week(for news)")
    public static void newsTrendWordInLastWeek(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "you can add a site with manual config")
    public static void addSite(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "add a rss to your feeder")
    public static void addRss(PrintStream out, Scanner scanner) {
        out.print("please enter a rss address");
        String rss = scanner.next();
        out.print("please enter domain of this site(it will be used for finding and if its template )");
        String rssDoamin = scanner.next();
        SiteTemplates.getInstance().getSiteTemplates().
        if (!SiteTemplates.getInstance().getSiteTemplates().containsKey(rssDoamin))
        out.print("please enter tag of news link in this rss");
        String newsTag = scanner.next();


    }

    @CLI(help = "show you all template list")
    public static void showTemplates(PrintStream out, Scanner scanner) {
        SiteTemplates.getInstance().getSiteTemplates().forEach((key, value) -> System.out.println(key + "   " + value));
    }

    @CLI(help = "show all rss")
    public static void showRss(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "refresh ...")
    public static void refresh(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "you can delete a rss from rss reader ")
    public static void deleteRss(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "save Templates")
    public static void saveTemplates(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
    }

    @CLI(help = "load Templates ")
    public static void loadTemplates(PrintStream out, Scanner scanner) {
    }

    @CLI(help = "save Templates")
    public static void saveRss(PrintStream out, Scanner scanner) {

    }

    @CLI(help = "load Templates ")
    public static void loadRss(PrintStream out, Scanner scanner) {

    }

    @CLI(help = "save all thing will be needed and exit")
    public static void exit(PrintStream out, Scanner scanner) {
        saveTemplates(out, scanner);
        System.exit(0);
    }

    @CLI
    public static void hostRate(PrintStream out, Scanner scanner) {
        out.print("please type ");
    }
}
