package ir.nimbo.searchengine.crawler;

public class DomainFrequencyHandler {
    private static DomainFrequencyHandler ourInstance = new DomainFrequencyHandler();
    private static int domainHashPrime = 196613;

    public static DomainFrequencyHandler getInstance() {
        return ourInstance;
    }

    private long[] domainHashTableTime;

    private DomainFrequencyHandler() {
        domainHashPrime = 786433;
        domainHashTableTime = new long[domainHashPrime];
    }

    public boolean isAllow(String url) {
        int hash = (url.hashCode() % domainHashPrime + domainHashPrime) % domainHashPrime;
        if (System.currentTimeMillis() - domainHashTableTime[hash] > 28000) {
            domainHashTableTime[hash] = System.currentTimeMillis();
            return true;
        }
        return false;
    }
}