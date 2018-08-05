package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.crawler.politecrawling.DomainFrequencyHandler;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class UrlHandler {
    static DomainFrequencyHandler domainTimeHandler = DomainFrequencyHandler.getInstance();

    static String normalizeLink(String link, String mainUrl) {
        if (link.startsWith("/")) {
            return mainUrl + link;
        }
        return link;
    }

    static Link[] getLinks(Elements links, String mainUrl) {
        Link[] finalLinks = links.stream().filter(element -> !element.attr("href").contains("#"))
                .map(element -> new Link(element, mainUrl))
                .filter(e -> !e.getDomain().equals("ERROR") && domainTimeHandler.isAllow(e.getDomain()))
                .toArray(Link[]::new);
        return finalLinks;
    }

    public static void returnFinalLinks(ArrayList<Link> links, ArrayList<Link> internalLinks, ArrayList<Link> externalLinks, String mainDomain) {
        for (Link link : links) {
            if (link.getDomain().equals(mainDomain)) {
                internalLinks.add(link);
            } else {
                externalLinks.add(link);
            }
        }
    }
}

