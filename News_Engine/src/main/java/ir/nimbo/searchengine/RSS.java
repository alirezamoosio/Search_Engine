package ir.nimbo.searchengine;

import java.util.LinkedHashMap;

public class RSS {
    public static final int HASH_MAP_SIZE=20;
    public static LinkedHashMap<String,RSS> rssMap=new LinkedHashMap<>();
    private String rssUrl;
    private String domain;
    private String linkAddress;
    public static void loadRss(){

        // TODO: 8/16/18
    }
    public static void saveRss(){
        // TODO: 8/16/18
    }
    public RSS(String rssUrl, String domain, String linkAddress) {
        this.rssUrl = rssUrl;
        this.domain = domain;
        this.linkAddress = linkAddress;

    }
    public String getRssUrl() {
        return rssUrl;
    }

    public String getDomain() {
        return domain;
    }

    public String getLinkAddress() {
        return linkAddress;
    }
}