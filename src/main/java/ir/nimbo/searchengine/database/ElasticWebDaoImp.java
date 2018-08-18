package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.database.webdocumet.WebDocument;
import ir.nimbo.searchengine.metrics.Metrics;
import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;


public class ElasticWebDaoImp implements WebDao {
    private RestHighLevelClient client;
    private String index = "pages";
    private Logger errorLogger = Logger.getLogger("error");
    private IndexRequest indexRequest;
    private BulkRequest bulkRequest;
    private static int added = 0;
    private static final Integer sync = 0;
    private static final int ELASTIC_FLUSH_SIZE_LIMIT = 2;
    private static final int ELASTIC_FLUSH_NUMBER_LIMIT = 193;

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
    public void put(WebDocument document) {
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
                errorLogger.error("ERROR! couldn't add " + document.getPagelink() + " to elastic");
            }
            if (bulkRequest.estimatedSizeInBytes() / 1000_000 >= ELASTIC_FLUSH_SIZE_LIMIT ||
                    bulkRequest.numberOfActions() >= ELASTIC_FLUSH_NUMBER_LIMIT) {
                synchronized (sync) {
                    client.bulk(bulkRequest);
                    bulkRequest = new BulkRequest();
                    Metrics.numberOfPagesAddedToElastic = added;
                }
            }
        } catch (IOException e) {
            errorLogger.error("ERROR! Couldn't add the document for " + document.getPagelink());
        }
    }

}
