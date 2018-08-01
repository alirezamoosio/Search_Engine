package ir.nimbo.searchengine.elasticsearch;

import org.apache.http.HttpHost;
import org.apache.log4j.Logger;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class elastictexampleTest {
    @Test
    public void givenJsonString_whenJavaObject_thenIndexDocument() {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http"),
                        new HttpHost("localhost", 9201, "http")));
        IndexRequest request = new IndexRequest(
                "test",
                "doc",
                "1");
        JSONObject json = new JSONObject();
        json.put("user", "Mehran");
        json.put("age", "20");
        json.put("field", "CE");
        request.source(json.toString(), XContentType.JSON);
        try {
            client.index(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GetRequest getRequest = new GetRequest(
                "test",
                "doc",
                "1");
        try {
            GetResponse getResponse = client.get(getRequest);
            JSONObject result = new JSONObject(getResponse.getSourceAsString());
            assertEquals("test", getResponse.getIndex());
            assertEquals("{\"field\":\"CE\",\"user\":\"Mehran\",\"age\":\"20\"}", result.toString());
            assertEquals("Mehran", result.get("user"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}