package ir.nimbo.searchengine.crawler;

public class DuplicateLinkHandler {
    private static DuplicateLinkHandler ourInstance = new DuplicateLinkHandler();

    public static DuplicateLinkHandler getInstance() {
        return ourInstance;
    }

    private static int hashPrime ;
    private static int hashTableSize;
    private byte[] linkHashTableTime;
    private byte[] twoPowers;
    private DuplicateLinkHandler() {
        hashPrime = 201326611;
        linkHashTableTime = new byte[hashPrime];
        twoPowers= new byte[]{0b1, 0b10, 0b100, 0b1000, 0b10000, 0b100000, 0b1000000, -128};//-128 = 10000000
        loadHashTable();
    }

    private void loadHashTable() {

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
    public void saveHashTable(){
//must be called
//its important before downing system
    }
}
