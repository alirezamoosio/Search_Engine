package ir.nimbo.searchengine.database;

import com.google.gson.Gson;
import ir.nimbo.searchengine.crawler.WebDocument;
import ir.nimbo.searchengine.util.ConfigManager;
import ir.nimbo.searchengine.util.PropertyType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HbaseWebDaoImp implements WebDoa {
    private TableName webPageTable = TableName.valueOf("webpage");;
    private String contextFamily = "context";
    private Configuration configuration;

    public HbaseWebDaoImp() {
        configuration = HBaseConfiguration.create();
        String path = this.getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        configuration.addResource(new Path(path));
        try {
            HBaseAdmin.available(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createTable() {
        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
            Admin admin = connection.getAdmin();
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(webPageTable);
            ColumnFamilyDescriptorBuilder anchorFamilyBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(contextFamily.getBytes());
            tableDescriptorBuilder.setColumnFamily(anchorFamilyBuilder.build());
            if (!admin.tableExists(webPageTable))
                admin.createTable(tableDescriptorBuilder.build());
            System.out.println("create");
            admin.close();
            connection.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public synchronized void put(WebDocument document) {
        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
//        for(WebDocument document : documents){
            String outLinksColumn = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_OUTLINKS);
            String pageRankColumn = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_PAGERANK);
            Table t = connection.getTable(webPageTable);
            Put put = new Put(Bytes.toBytes(document.getPagelink()));
            put.addColumn(contextFamily.getBytes(), "pageLink".getBytes(), document.getPagelink().getBytes());
            Gson gson = new Gson();
            String serializedList = gson.toJson(document.getLinks());
            put.addColumn(contextFamily.getBytes(), outLinksColumn.getBytes(), serializedList.getBytes());
            put.addColumn(contextFamily.getBytes(), outLinksColumn.getBytes(), pageRankColumn.getBytes());
            t.put(put);
            t.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String invertUrl(String url) {
        return null;
    }
}
