package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ElasticWebDaoImp implements WebDao {
    private final static int BULK_SIZE = 100;
    private static int elasticFlushSizeLimit = 2;
    private static int elasticFlushNumberLimit = 100;
    private RestHighLevelClient client;
    private String index = "pages";
    private Logger errorLogger = Logger.getLogger("error");
    private IndexRequest indexRequest;
    private BulkRequest bulkRequest;

    public ElasticWebDaoImp() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("94.23.214.93", 9200, "http")));
        indexRequest = new IndexRequest(index, "_doc");
        bulkRequest = new BulkRequest();
    }

    @Override
    public boolean createTable() {
        return false;
    }

    @Override
    public synchronized void put(WebDocument document) {
        try {
            System.out.println(document.getPagelink());
            XContentBuilder builder = XContentFactory.jsonBuilder();
            try {
                builder.startObject();
                {
                    builder.field("pageLink", document.getPagelink());
                    builder.field("pageText", document.getTextDoc());
                }
                builder.endObject();
                indexRequest.source(builder);
                bulkRequest.add(indexRequest);
            } catch (IOException e) {
                errorLogger.error("ERROR! couldn't add " + document.getPagelink() + " to elastic");
            }
            if (bulkRequest.estimatedSizeInBytes() / 1000_000 >= elasticFlushSizeLimit ||
                    bulkRequest.numberOfActions() >= elasticFlushNumberLimit) {
                BulkResponse bulkResponse = client.bulk(bulkRequest);
                bulkRequest = new BulkRequest();
                indexRequest = new IndexRequest(index, "_doc");

            }
        } catch (IOException e) {
            errorLogger.error("ERROR! Couldn't add the document for " + document.getPagelink());
        }
    }


    public Map<String, Float> search(String text) {
        Map<String, Float> results = new HashMap<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchRequest.types("doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.matchQuery("pageText", text));
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(10);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        boolean searchStatus = false;
        while(!searchStatus){
            try {
                searchResponse = client.search(searchRequest);
                searchStatus = true;
            } catch (IOException e) {
                System.out.println("Elastic connection timed out! Trying again...");
                searchStatus = false;
            }
        }
        SearchHit[] hits = searchResponse.getHits().getHits();
//        int i = 1;
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
//            System.out.println(i + " " + sourceAsMap.get("pageLink") + " " + hit.getScore());
//            i++;
            results.put((String) sourceAsMap.get("pageLink"), hit.getScore());
        }
//        Collections.sort((List<Float>) results.values());
        return results;
    }

}

