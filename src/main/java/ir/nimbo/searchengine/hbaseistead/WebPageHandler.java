package ir.nimbo.searchengine.hbaseistead;

import ir.nimbo.searchengine.crawler.FinishedRequest;
import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

public class WebPageHandler implements FinishedRequest {
    private Admin admin = null;
    private TableName table1 = TableName.valueOf("Table1");
    private String family1 = "Family1";
    private String family2 = "Family2";

    private void connect() {
        Configuration config = HBaseConfiguration.create();
        String path = this.getClass()
                .getClassLoader()
                .getResource("hbase-site.xml")
                .getPath();
        config.addResource(new Path(path));
        Connection connection = null;
        try {
            connection = ConnectionFactory.createConnection(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            admin = connection.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        HTableDescriptor desc = new HTableDescriptor(table1);
        desc.addFamily(new HColumnDescriptor(family1));
        desc.addFamily(new HColumnDescriptor(family2));
        try {
            admin.createTable(desc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Queue<WebDocument> webDocuments = new SynchronousQueue<>();

    @Override
    public void accept(WebDocument webDocument) {
        webDocuments.add(webDocument);
    }
}
