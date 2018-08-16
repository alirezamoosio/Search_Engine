package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.metrics.Metrics;
import ir.nimbo.searchengine.crawler.WebDocument;
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
    private static int elasticFlushSizeLimit = 2;
    private static int elasticFlushNumberLimit = 193;
    private RestHighLevelClient client;
    private String index = "pages";
    private Logger errorLogger = Logger.getLogger("error");
    private IndexRequest indexRequest;
    private BulkRequest bulkRequest;
    private static int added = 0;
    private static final Integer sync = 0;
    private static Logger infoLogger = Logger.getLogger("info");

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
                errorLogger.error("ERROR! couldn't add " + document.getPagelink() + " to elastic");
            }
            if (bulkRequest.estimatedSizeInBytes() / 1000_000 >= elasticFlushSizeLimit ||
                    bulkRequest.numberOfActions() >= elasticFlushNumberLimit) {
                synchronized (sync) {
                    BulkResponse bulkResponse = client.bulk(bulkRequest);
                    bulkRequest = new BulkRequest();
                    Metrics.numberOfPagesAddedToElastic = added;
                }
            }
        } catch (IOException e) {
            errorLogger.error("ERROR! Couldn't add the document for " + document.getPagelink());
        }
    }

}
