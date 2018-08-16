package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.metrics.Metrics;
import ir.nimbo.searchengine.crawler.WebDocument;
import ir.nimbo.searchengine.util.ConfigManager;
import ir.nimbo.searchengine.util.PropertyType;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.shaded.org.apache.commons.lang.SerializationUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class HbaseWebDaoImp implements WebDao {
    private static Logger errorLogger = Logger.getLogger("error");
    private TableName webPageTable = TableName.valueOf(ConfigManager.getInstance().getProperty(PropertyType.HBASE_TABLE));
    private String contextFamily = ConfigManager.getInstance().getProperty(PropertyType.HBASE_FAMILY_1);
    private String rankFamily = ConfigManager.getInstance().getProperty(PropertyType.HBASE_FAMILY_2);
    private Configuration configuration;
    private final List<Put> puts;
    private static int size = 0;
    private final static int SIZE_LIMIT = 100;
    private static int added = 0;
    private static Logger infoLogger = Logger.getLogger("info");

    public HbaseWebDaoImp() {
        configuration = HBaseConfiguration.create();
        String path = this.getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        configuration.addResource(new Path(path));
        puts = new ArrayList<>();
        boolean status = false;
        while (!status) {
            try {
                HBaseAdmin.available(configuration);
                status = true;
            } catch (IOException e) {
                errorLogger.error(e.getMessage());
            }
        }
    }

    public boolean createTable() {
        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
            Admin admin = connection.getAdmin();
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(webPageTable);
            ColumnFamilyDescriptorBuilder anchorFamilyBuilderContext = ColumnFamilyDescriptorBuilder
                    .newBuilder(contextFamily.getBytes());
            ColumnFamilyDescriptorBuilder anchorFamilyBuilderRank = ColumnFamilyDescriptorBuilder
                    .newBuilder(rankFamily.getBytes());
            tableDescriptorBuilder.setColumnFamily(anchorFamilyBuilderContext.build());
            tableDescriptorBuilder.setColumnFamily(anchorFamilyBuilderRank.build());
            if (!admin.tableExists(webPageTable))
                admin.createTable(tableDescriptorBuilder.build());
            System.out.println("create");
            admin.close();
            connection.close();
            return true;

        } catch (IOException e) {
            errorLogger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void put(WebDocument document) {
//        for(WebDocument document : documents){
        String outLinksColumn = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_OUTLINKS);
        String pageRankColumn = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_PAGERANK);
        Put put = new Put(Bytes.toBytes(generateRowKeyFromUrl(document.getPagelink())));
        byte[] outLinks = SerializationUtils.serialize(document.getLinks());
        put.addColumn(contextFamily.getBytes(), outLinksColumn.getBytes(), outLinks);
        put.addColumn(rankFamily.getBytes(), pageRankColumn.getBytes(), Bytes.toBytes(1.0));
        puts.add(put);
        size++;
        if (size >= SIZE_LIMIT) {
            synchronized (puts) {
                try (Connection connection = ConnectionFactory.createConnection(configuration)) {
                    Table t = connection.getTable(webPageTable);
                    t.put(puts);
                    t.close();
                    puts.clear();
                    added += size;
                    Metrics.numberOfPagesAddedToHbase = added;
                    size = 0;
                } catch (IOException e) {
                    errorLogger.error("couldn't put document for " + document.getPagelink() + " into HBase!");
                } catch (RuntimeException e) {
                    errorLogger.error("habase error" + e.getMessage());
                }
            }
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
            if (i == 0) {
                if (!url.startsWith(domain)) {
                    domainToHbase.append(".").append(urlSections[0]);
                }
            } else {
                domainToHbase.append(".");
            }
        }
        return domainToHbase + "-" + urlSections[urlSections.length - 1];
    }
}
