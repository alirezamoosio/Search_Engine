package ir.nimbo.searchengine.database;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.junit.Test;

import java.io.IOException;
import java.util.Date;

public class elastictexampleTest {
    @Test
    public void ElasticTest() throws IOException {
        Logger logger = Logger.getLogger("error");
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("94.23.214.93", 9200, "http")));
//                        new HttpHost("worker-node", 9200, "http")));
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("user", "Amirsaeed");
            builder.timeField("postDate", new Date());
            builder.field("message", "i am different different");
        }
        builder.endObject();
        IndexRequest indexRequest = new IndexRequest("test2", "doc", "5" )
                .source(builder);
        IndexRequest indexRequest1 = new IndexRequest("test2", "doc", "6")
                .source("user", "Mohammadreza",
                        "postDate", new Date(),
                        "message", "different elasticsearch different different");
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);
        bulkRequest.add(indexRequest1);
        BulkResponse bulkResponse = client.bulk(bulkRequest);
        System.out.println(bulkResponse.status());
        indexRequest = new IndexRequest("test2", "doc", "8")
                .source("user", "Mehran",
                        "postDate", new Date(),
                        "message", "The last Added");
//        bulkRequest = new BulkRequest();
        bulkRequest.add(indexRequest);
        System.out.println(bulkRequest.getDescription());
        bulkResponse = client.bulk(bulkRequest);
        GetRequest getRequest = new GetRequest("test2", "doc", "5");
        GetRequest getRequest2 = new GetRequest("test2", "doc", "8");
        GetResponse getResponse = client.get(getRequest);
        System.out.println(getResponse.getSourceAsString());
        GetResponse getResponse2 = client.get(getRequest2);
        System.out.println(getResponse2.getSourceAsString());
        getRequest = new GetRequest("test2", "doc", "5");
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        System.out.println(client.exists(getRequest));
        SearchRequest searchRequest = new SearchRequest("test2");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("message", "different")
                .fuzziness(Fuzziness.AUTO)
                .prefixLength(3)
                .maxExpansions(10);
        searchSourceBuilder.query(matchQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            System.out.println(hit.getSourceAsString());
            System.out.println(hit.getScore());
        }
    }

}