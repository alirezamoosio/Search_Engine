package ir.nimbo.searchengine.crawler.politecrawling;

public class DomainFrequencyHandler {
    private static DomainFrequencyHandler ourInstance = new DomainFrequencyHandler();

    public static DomainFrequencyHandler getInstance() {
        return ourInstance;
    }
    private static int domainHashPrime=196613;
    private long[] domainHashTableTime;
    private DomainFrequencyHandler() {
        domainHashTableTime=new long[domainHashPrime];
    }
    public boolean isAllow(String url){
        int hash=(url.hashCode()%domainHashPrime+domainHashPrime)%domainHashPrime;
        if(System.currentTimeMillis()-domainHashTableTime[hash]>30000){
            domainHashTableTime[hash]=System.currentTimeMillis();
            return true;
        }
        return false;
    }
}