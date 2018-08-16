package ir.nimbo.searchengine;


import ir.nimbo.searchengine.template.SiteTemplates;
import scala.util.parsing.combinator.testing.Str;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Listener {
    private static final int LISTEN_PORT = 2719;

    public static void listen() {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(LISTEN_PORT)) {
                while (true) {
                    try (Socket socket = serverSocket.accept()) {
                        sleep(4000);
                        Scanner scanner = new Scanner(socket.getInputStream());
                        String[] strings = scanner.nextLine().split("[ ]+");
                        String funcName = "";
                        for (String string : strings) {
                            ;
                            funcName = funcName.concat(string.substring(0, 1).toUpperCase()).concat(string.substring(1).toLowerCase());
                        }
                        funcName = funcName.substring(0, 1).toLowerCase().concat(funcName.substring(1));
                        Method method = Listener.class.getMethod(funcName, PrintStream.class, Scanner.class);
                        method.invoke(null, new PrintStream(socket.getOutputStream()), scanner);
                    } catch (IOException | InterruptedException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @CLI(help = "give you last news of each site")
    public static void lastNews(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
        endMethod(out, scanner);
    }

    @CLI(help = "give you trend words in last Hour(for news)")
    public static void newsTrendWordInLastHour(PrintStream out, Scanner scanner) {
// TODO: 8/16/18
        endMethod(out, scanner);
    }

    @CLI(help = "give you trend words in last Day(for news)")
    public static void newsTrendWordInLastDay(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
        endMethod(out, scanner);
    }

    @CLI(help = "give you trend words in last Week(for news)")
    public static void newsTrendWordInLastWeek(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
        endMethod(out, scanner);
    }

    @CLI(help = "you can add a site with manual config")
    public static void addSite(PrintStream out, Scanner scanner) {
        // TODO: 8/16/18
        endMethod(out, scanner);
    }

    @CLI(help = "add a rss to your feeder")
    public static void addRss(PrintStream out, Scanner scanner) {
        out.print("please enter a rss address");
        String rss = scanner.next();
        out.print("please enter tag of link in this rss");
        String linkAdress = scanner.next();
        out.print("please enter domain of this site(it will be used for finding and if its template )");
        String rssDoamin=scanner.next();
        endMethod(out, scanner);
    }
    @CLI(help = "show you all template list")
    public static void showTemplates(PrintStream out, Scanner scanner) {
        SiteTemplates.getInstance().getSiteTemplates().forEach((key,value)->System.out.println(key+"   "+value));
        endMethod(out, scanner);
    }

    @CLI(help = "show all rss")
    public static void showRss(PrintStream out, Scanner scanner) {
        endMethod(out, scanner);
    }

    @CLI(help = "refresh ...")
    public static void refresh(PrintStream out, Scanner scanner) {
        endMethod(out, scanner);
    }

    @CLI(help = "save Templates")
    public static void saveTemplates(PrintStream out, Scanner scanner) {

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
        endMethod(out, scanner);
    }

    @CLI
    public static void help(PrintStream out, Scanner scanner) {
        for (Method method : Listener.class.getMethods()) {
            out.print(method.getName());
            try {
                out.println("    :    " + method.getAnnotations()[0].annotationType().getField("help"));
            } catch (NoSuchFieldException e) {
                e.printStackTrace(out);

            }
        }
    }

    private static void endMethod(PrintStream out, Scanner scanner) {
        out.println("done");
        out.flush();
        out.close();
        scanner.close();
    }
}