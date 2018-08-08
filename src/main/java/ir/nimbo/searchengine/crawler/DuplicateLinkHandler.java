package ir.nimbo.searchengine.crawler;

public class DuplicateLinkHandler {
    private static DuplicateLinkHandler ourInstance = new DuplicateLinkHandler();

    public static DuplicateLinkHandler getInstance() {
        return ourInstance;
    }

    private static int hashPrime ;
    private byte[] linkHashTableTime;

    private DuplicateLinkHandler() {
        hashPrime = 201326611;
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
