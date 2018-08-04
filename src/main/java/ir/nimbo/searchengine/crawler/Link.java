package ir.nimbo.searchengine.crawler;

import org.jsoup.nodes.Element;

public class Link {
    private String anchorLink;
    private String url;
    private String domain;

    public Link(Element element, Link mainLink) {
        this.anchorLink = element.text();
        this.url = UrlHandler.normalizeLink(element.attr("href"), mainLink.getUrl());
        this.domain = this.url.split("/")[2];
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
