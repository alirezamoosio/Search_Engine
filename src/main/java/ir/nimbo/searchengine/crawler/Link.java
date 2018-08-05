package ir.nimbo.searchengine.crawler;

import org.apache.log4j.Logger;
import org.jsoup.nodes.Element;

import java.net.MalformedURLException;
import java.net.URL;

public class Link {
    private String anchorLink;
    private String url;
    private String domain;
    private static Logger logger = Logger.getLogger(Crawler.class);

    Link(Element element, String mainUrl) {
        this.anchorLink = element.text();
        this.url = UrlHandler.normalizeLink(element.attr("href"), mainUrl);
        try {
            this.domain=new URL(url).getHost();
        } catch (MalformedURLException e) {
//            e.printStackTrace();
            domain="ERROR";
            logger.error("Couldn't fetch domain of " + this.url);

        }
    }

    public Link(String anchorLink, String url, String domain) {
        this.anchorLink = anchorLink;
        this.url = url;
        this.domain = domain;
    }

    public String getAnchorLink() {
        return anchorLink;
    }

    public String getUrl() {
        return url;
    }

    public String getDomain() {
        return domain;
    }

    public void setAnchorLink(String anchorLink) {
        this.anchorLink = anchorLink;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
