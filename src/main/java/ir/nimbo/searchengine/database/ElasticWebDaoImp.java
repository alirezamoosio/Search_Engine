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
    private static int elasticFlushSizeLimit = 2;
    private static int elasticFlushNumberLimit = 200;
    private RestHighLevelClient client;
    private String index = "pages";
    private Logger errorLogger = Logger.getLogger("error");
    private IndexRequest indexRequest;
    private BulkRequest bulkRequest;
    private static int added = 0;
    private static final Integer sync = 0;

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
                indexRequest = new IndexRequest(index, "_doc");
                added++;
            } catch (IOException e) {
                System.out.println("here");
                errorLogger.error("ERROR! couldn't add " + document.getPagelink() + " to elastic");
            }
            if (bulkRequest.estimatedSizeInBytes() / 1000_000 >= elasticFlushSizeLimit ||
                    bulkRequest.numberOfActions() >= elasticFlushNumberLimit) {
                synchronized (sync) {
                    BulkResponse bulkResponse = client.bulk(bulkRequest);
                    bulkRequest = new BulkRequest();
                    System.out.println(added + " added in elastic since start running");
                }
            }
        } catch (IOException e) {
            System.out.println("error");
            errorLogger.error("ERROR! Couldn't add the document for " + document.getPagelink());
        }
    }


    public Map<String, Float> search(String necessaryWord, ArrayList<String> preferredWords, ArrayList<String> forbiddenWords) {
        Map<String, Float> results = new HashMap<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchRequest.types("_doc");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        searchRequest.source(searchSourceBuilder);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        for(String necessaryWord:necessaryWords {
        boolQueryBuilder.must(QueryBuilders.matchQuery("pageText", necessaryWord));
//        }
//        for(String preferredWord:preferredWords) {
//            boolQueryBuilder.should(QueryBuilders.matchQuery("pageText", preferredWord));
//        }
//        for(String forbiddenWord:forbiddenWords) {
//            boolQueryBuilder.mustNot(QueryBuilders.matchQuery("pageText", forbiddenWord));
//        }
        sourceBuilder.query(boolQueryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(2000);
        sourceBuilder.timeout(new TimeValue(5, TimeUnit.SECONDS));
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = null;
        boolean searchStatus = false;
        while (!searchStatus) {
            try {
                searchResponse = client.search(searchRequest);
                searchStatus = true;
            } catch (IOException e) {
                System.out.println("Elastic connection timed out! Trying again...");
                searchStatus = false;
            }
        }
        SearchHit[] hits = searchResponse.getHits().getHits();
        int i = 1;
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(i + " " + sourceAsMap.get("pageLink") + " " + hit.getScore());
            i++;
            results.put((String) sourceAsMap.get("pageLink"), hit.getScore());
        }
//        Collections.sort((List<Float>) results.values());
        return results;
    }

}

