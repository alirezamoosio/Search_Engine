package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class ElasticWebDaoImpTest {
    private ElasticWebDaoImp elasticWebDaoImp;
    @Test
    public void putAndUpdate() {
        elasticWebDaoImp = new ElasticWebDaoImp();
        ArrayList<WebDocument> webDocuments = new ArrayList<>();
        WebDocument webDocument = new WebDocument();
        webDocument.setPagelink("www.my4.com");
        webDocument.setTextDoc("hello hello everybody hello");
        webDocuments.add(webDocument);
        elasticWebDaoImp.put(webDocument);
        elasticWebDaoImp.updateElastic();
        webDocument.setPagelink("www.my2.com");
        webDocument.setTextDoc("hello from the other side");
        webDocuments.add(webDocument);
        for(WebDocument webDocument1 : webDocuments){
            elasticWebDaoImp.put(webDocument1);
        }
        elasticWebDaoImp.updateElastic();
        webDocument.setPagelink("www.my6.com");
        webDocument.setTextDoc("hello for fun!");
        elasticWebDaoImp.put(webDocument);
        elasticWebDaoImp.updateElastic();
        try {
            Map<String, Float> searchResult = elasticWebDaoImp.search("for fun!");
            System.out.println(searchResult.size());
            Set<String> hits = searchResult.keySet();
            for(String hit: hits){
                System.out.println(hit + "\t" + searchResult.get(hit));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}