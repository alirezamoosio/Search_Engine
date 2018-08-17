package ir.nimbo.searchengine;

public class RSS {
    public static final int HASH_MAP_SIZE=20;
    private String rssUrl;
    private String domain;
    public static void loadRss(){
        // TODO: 8/16/18
    }
    public static void saveRss(){
        // TODO: 8/16/18
    }
    public RSS(String rssUrl, String domain, String linkAddress) {
        this.rssUrl = rssUrl;
        this.domain = domain;

    }
    public String getRssUrl() {
        return rssUrl;
    }

    public String getDomain() {
        return domain;
    }
}