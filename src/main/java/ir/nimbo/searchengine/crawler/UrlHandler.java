package ir.nimbo.searchengine.crawler;

import org.jsoup.select.Elements;

import java.util.ArrayList;

public class UrlHandler {

    static String normalizeLink(String link, String mainUrl) {
        if (link.startsWith("/")) {
            link = mainUrl + link;
        }
        if (link.startsWith("www")) {
            link = "http://" + link;
        }
        return link;
    }

    static Link[] getLinks(Elements links, String mainUrl) {
        Link[] finalLinks = links.stream().filter(element -> !element.attr("href").contains("#"))
                .map(element -> new Link(element, mainUrl))
                .filter(e -> !e.getDomain().equals("ERROR"))
                .toArray(Link[]::new);
        return finalLinks;
    }

    public static void splitter(ArrayList<Link> links, ArrayList<Link> internalLinks, ArrayList<Link> externalLinks, String mainDomain) {
        for (Link link : links) {
            if (link.getDomain().equals(mainDomain)) {
                internalLinks.add(link);
            } else {
                externalLinks.add(link);
            }
        }
    }
}

