package ir.nimbo.searchengine.kafkainstead;

import ir.nimbo.searchengine.crawler.QueueCommunicable;

import java.util.concurrent.ArrayBlockingQueue;

public class URLQueue implements QueueCommunicable {
    private final int hashDomainSize;
    private  ArrayBlockingQueue<String> URLQueue = new ArrayBlockingQueue<>(10000);
    private int[] hashTableLinks;
    private long[] hashTableDomain;
    private int hashTableSize;

    public URLQueue(int hashTableSize,int hashDomainSize,String... firstUrls) {
        this.hashTableSize=hashTableSize;
        this.hashDomainSize=hashDomainSize;
        hashTableLinks =new int[hashTableSize];
        pushNewURL( firstUrls);
        hashTableDomain=new long[hashDomainSize];
    }

    @Override
    public String pollNewURL() {
        try {
            String url=URLQueue.take();
            int hash=((url.split("/")[2].hashCode())%hashDomainSize+hashDomainSize)%hashDomainSize;
            if (System.currentTimeMillis()-hashTableDomain[hash]<30000){
                URLQueue.offer(url);
                System.out.println("this domain used in last .5 minute "+ url);
                return pollNewURL();
            }else {
                hashTableDomain[hash]=System.currentTimeMillis();
                return url;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("poll returned google");
        return "https://www.google.com";
    }

    @Override
    public void pushNewURL(String... urls) {
        for (String url : urls) {
            int hash =((url.hashCode()%hashTableSize)+hashTableSize)%hashTableSize;
            if (hashTableLinks[hash]==0){
                hashTableLinks[hash]=1;
                URLQueue.offer(url);
            }
        }
        System.out.println("reminds"+URLQueue.size());
    }
}
