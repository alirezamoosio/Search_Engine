package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.database.webdocumet.WebDocument;
import org.junit.Test;

import java.util.ArrayList;

public class ElasticWebDaoImpTest {
    private ElasticWebDaoImp elasticWebDaoImp;

    @Test
    public void put() {
        elasticWebDaoImp = new ElasticWebDaoImp();
        ArrayList<WebDocument> webDocuments = new ArrayList<>();
        WebDocument webDocument = new WebDocument();
        webDocument.setPagelink("www.my4.com");
        webDocument.setTextDoc("hello hello everybody hello");
        webDocuments.add(webDocument);
        elasticWebDaoImp.put(webDocument);
//        elasticWebDaoImp.updateElastic();
        webDocument.setPagelink("www.my2.com");
        webDocument.setTextDoc("hello from the other side");
        webDocuments.add(webDocument);
        for (WebDocument webDocument1 : webDocuments) {
            elasticWebDaoImp.put(webDocument1);
        }
//        elasticWebDaoImp.updateElastic();
        webDocument.setPagelink("www.my6.com");
        webDocument.setTextDoc("hello for fun!");
        elasticWebDaoImp.put(webDocument);
//        elasticWebDaoImp.updateElastic();
    }

}