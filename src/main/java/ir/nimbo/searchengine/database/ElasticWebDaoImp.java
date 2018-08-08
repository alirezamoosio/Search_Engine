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
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;

public class ElasticWebDaoImp implements WebDoa {
    private final static int BULK_SIZE = 10;
    private static int size;
    private RestHighLevelClient client;
    private String index = "pages";
    private Logger logger = Logger.getLogger(ElasticWebDaoImp.class);
    private IndexRequest indexRequest;
    private BulkRequest bulkRequest;

    public ElasticWebDaoImp() {
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("94.23.214.93", 9200, "http")));
        indexRequest = new IndexRequest(index, "doc");
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
                size++;
            } catch (IOException e) {
                logger.error("ERROR! couldn't add " + document.getPagelink() + " to elastic");
            }
            if (size >= BULK_SIZE) {
                BulkResponse bulkResponse = client.bulk(bulkRequest);
                size = 0;
                bulkRequest = new BulkRequest();
                indexRequest = new IndexRequest(index, "doc");
            }

        } catch (IOException e) {
            logger.error("ERROR! Couldn't add the document for " + document.getPagelink());
        }
    }

    public void updateElastic(){
        try {
            client.bulk(bulkRequest);
            bulkRequest = new BulkRequest();
        } catch (IOException e) {
            System.out.println("ERROR! update on elastic failed");
            e.printStackTrace();
        }

    }

    public Map<String, Float> search(String text) throws IOException {
        Map<String, Float> results = new HashMap<>();
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("pageText", text)
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(100);
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            results.put((String) sourceAsMap.get("pageLink"), hit.getScore());
        }
//        Collections.sort((List<Float>) results.values());
        return results;
    }

}

