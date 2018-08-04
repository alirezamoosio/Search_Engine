package ir.nimbo.searchengine.crawler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.awt.image.ImageWatched;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

public class UrlHandler {

    public static String normalizeLink(String link, String mainUrl) {
        if(link.startsWith("/")){
            return mainUrl + link;
        }
        return link;
    }

    public Link[] getLinks(Elements links, Link mainLink) {
         Link[] finalLinks=links.stream().filter(element -> !element.attr("href").equals("#"))
                .map(element -> new Link(element, mainLink)).toArray(Link[]::new);
        for (Link link : finalLinks) {
            System.out.println(link.getAnchorLink() + link.getUrl() + link.getDomain());
        }
        return finalLinks;
    }

    public static void returnFinalLinks(ArrayList<Link> links, ArrayList<Link> internalLinks, ArrayList<Link> externalLinks) {

    }
}

