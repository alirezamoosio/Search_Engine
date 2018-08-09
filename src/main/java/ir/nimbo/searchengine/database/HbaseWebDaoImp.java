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
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HbaseWebDaoImp implements WebDao {
    private static Logger errorLogger = Logger.getLogger("error");
    private TableName webPageTable = TableName.valueOf(ConfigManager.getInstance().getProperty(PropertyType.HBASE_TABLE));
    private String contextFamily = ConfigManager.getInstance().getProperty(PropertyType.HBASE_FAMILY);
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
            Put put = new Put(Bytes.toBytes(generateRowKeyFromUrl(document.getPagelink())));
//            put.addColumn(contextFamily.getBytes(), "pageLink".getBytes(), document.getPagelink().getBytes());
            Gson gson = new Gson();
            Map<String, String> outLinksMap = new HashMap<>();
            document.getLinks().forEach(link -> outLinksMap.put(link.getUrl(), link.getAnchorLink()));
            String serializedMap = gson.toJson(outLinksMap);
            put.addColumn(contextFamily.getBytes(), outLinksColumn.getBytes(), serializedMap.getBytes());
            put.addColumn(contextFamily.getBytes(), pageRankColumn.getBytes(), Bytes.toBytes(1.0));
            t.put(put);
            t.close();
        } catch (IOException e) {
            errorLogger.error("couldn't put document for " + document.getPagelink() + " into HBase!");
        }
    }

    public String generateRowKeyFromUrl(String url) {
        String domain;
        try {
            domain = new URL(url).getHost();
        } catch (MalformedURLException e) {
            domain = "ERROR";
        }
        String[] urlSections = url.split(domain);
        String[] domainSections = domain.split("\\.");
        StringBuilder domainToHbase = new StringBuilder();
        for (int i = domainSections.length - 1; i >= 0; i--) {
            domainToHbase.append(domainSections[i]);
            if(i == 0) {
                if (!url.startsWith(domain)) {
                    domainToHbase.append("." + urlSections[0]);
                }
            }
            else {
                domainToHbase.append(".");
            }
        }
        return domainToHbase + "-" + urlSections[urlSections.length - 1];
    }
}
