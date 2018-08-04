package ir.nimbo.searchengine.kafkainstead;

import ir.nimbo.searchengine.crawler.QueueCommunicable;

import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;


public class URLQueue implements QueueCommunicable {
    private final int hashDomainSize;
    private  ArrayBlockingQueue<String> URLQueue = new ArrayBlockingQueue<>(10000);
    private LinkedList<String> urlTempList=new LinkedList<>();
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
            long deltaTime=System.currentTimeMillis()-hashTableDomain[hash];
            System.out.println(deltaTime);
            if (deltaTime<30000){
                pushNewURL(url);
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

    public void shuffle() {
        Collections.shuffle( urlTempList);
    }
    public void appendTempToQueue(){
        urlTempList.forEach(e->URLQueue.offer(e));
        urlTempList.clear();
    }

    @Override
    public void pushNewURL(String... urls) {
        for (String url : urls) {
            int hash =((url.hashCode()%hashTableSize)+hashTableSize)%hashTableSize;
            if (hashTableLinks[hash]==0){
                hashTableLinks[hash]=1;
                urlTempList.add(url);
            }
        }
    }

    public ArrayBlockingQueue<String> getURLQueue() {
        return URLQueue;
    }

    public void setURLQueue(ArrayBlockingQueue<String> URLQueue) {
        this.URLQueue = URLQueue;
    }

    public LinkedList<String> getUrlTempList() {
        return urlTempList;
    }

    public void setUrlTempList(LinkedList<String> urlTempList) {
        this.urlTempList = urlTempList;
    }
}
