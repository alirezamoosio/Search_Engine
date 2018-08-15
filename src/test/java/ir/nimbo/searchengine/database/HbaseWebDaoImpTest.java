package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.crawler.WebDocument;
import org.junit.Assert;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class HbaseWebDaoImpTest {
    @Test
    public void createTest(){
        HbaseWebDaoImp hbaseWebDaoImp = new HbaseWebDaoImp();
        Assert.assertTrue(hbaseWebDaoImp.createTable());
    }
    @Test
    public void putTest(){
        HbaseWebDaoImp hbaseWebDaoImp = new HbaseWebDaoImp();
        WebDocument webDocument = new WebDocument();
        webDocument.setTitle("a");
        hbaseWebDaoImp.createTable();
        Link link = new Link("reza","www.instagram.com","instagram.com");
        List<Link>list = new LinkedList<>();
        list.add(link);
        webDocument.setLinks(list);
        hbaseWebDaoImp.put(webDocument);
    }

    @Test
    public void generateRowKeyFromUrl() {
        String url = "https://stackoverflow.com/questions/5816212/difference-between-map-put-and-map-putall-methods";
        String domain;
        try {
            domain = new URL(url).getHost();
        } catch (MalformedURLException e) {
            domain = url;
        }
        String[] urlSections = url.split(domain);
        String[] domainSections = domain.split("\\.");
        StringBuilder domainToHbase = new StringBuilder();
        for (int i = domainSections.length - 1; i >= 0; i--) {
            domainToHbase.append(domainSections[i]);
            if(i == 0) {
                if (!url.startsWith(domain)) {
                    domainToHbase.append("." + urlSections[0]);
                }
            }
            else {
                domainToHbase.append(".");
            }
        }
        System.out.println(url);
        String result = domainToHbase + "-the-rest-of-the-url-is:";
        if(urlSections.length > 0){
            result += urlSections[urlSections.length - 1];
        }
        System.out.println(result);
        assertEquals(url, generateUrlFromRowKey(result));
    }

    private String  generateUrlFromRowKey(String url) {
        StringBuilder result = new StringBuilder();
        String[] sections = url.split("-the-rest-of-the-url-is:");
        String firstSection = sections[0];
        String[] domainSections = firstSection.split("\\.");
        StringBuilder domain = new StringBuilder();
        for(int i = domainSections.length - 1; i >= 0; i--){
            if(i < domainSections.length - 2){
                domain.append(".");
            }
            domain.append(domainSections[i]);
        }
        result = domain;
        if(sections.length > 1){
            result.append(sections[1]);
        }
        return result.toString();
    }
}