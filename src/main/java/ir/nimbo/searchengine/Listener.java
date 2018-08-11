package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.DuplicateLinkHandler;
import ir.nimbo.searchengine.crawler.Parser;

import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import static java.lang.System.out;
import static java.lang.System.setOut;
import static java.lang.Thread.sleep;

public class Listener {
    private static final int LISTEN_PORT = 2719;

    public static void main(String[] args) {
        listen();
    }

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
                            funcName = funcName.concat(string.substring(0, 1).toUpperCase()).concat(string.substring(1).toLowerCase());
                        }
                        funcName = funcName.substring(0, 1).toLowerCase().concat(funcName.substring(1));
                        Method method = Listener.class.getMethod(funcName, PrintStream.class);
                        method.invoke(null, new PrintStream(socket.getOutputStream()));
                    } catch (IOException | InterruptedException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void loadDuplicate(PrintStream out) {
        System.out.println("salaamasaaav");
        out.println("salaamasaaav");
        try {
            DuplicateLinkHandler.getInstance().loadHashTable();
            out.println("done");
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace(out);
            out.flush();
            out.close();

        }

    }

    public static void saveDuplicate(PrintStream out) {
        System.out.println("salaamasaaav");
        out.println("salaamasaaav");
        DuplicateLinkHandler.getInstance().saveHashTable();
        out.println("done");
        out.flush();
        out.close();
    }

    public static void stat(PrintStream out) {
        System.out.println("salaamasaaav");;
        Parser.stat(out);
        out.flush();
        out.close();
    }
}

