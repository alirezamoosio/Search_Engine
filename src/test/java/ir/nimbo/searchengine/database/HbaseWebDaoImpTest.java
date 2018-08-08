package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;
import org.junit.Assert;
import org.junit.Test;

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
        hbaseWebDaoImp.put(webDocument);
    }
}