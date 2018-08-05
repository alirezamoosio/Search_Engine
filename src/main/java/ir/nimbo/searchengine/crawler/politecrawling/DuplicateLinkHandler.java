package ir.nimbo.searchengine.crawler.politecrawling;

public class DuplicateLinkHandler {
    private static DuplicateLinkHandler ourInstance = new DuplicateLinkHandler();

    public static DuplicateLinkHandler getInstance() {
        return ourInstance;
    }

    private static int hashPrime = 201326611;
    private byte[] linkHashTableTime;

    private DuplicateLinkHandler() {
        linkHashTableTime = new byte[hashPrime];
    }

    public boolean isDuplicate(String url) {
        int hash = ((url.hashCode() % hashPrime) + hashPrime) % hashPrime;
        if (linkHashTableTime[hash] == 1) {
            return true;
        } else {
            linkHashTableTime[hash]=1;
            return false;
        }
    }
}
