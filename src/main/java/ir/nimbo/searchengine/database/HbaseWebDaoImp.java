package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.List;

public class HbaseWebDaoImp implements WebDoa {
    private TableName webPageTable;
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
        webPageTable = TableName.valueOf("webpage");
        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
            Admin admin = connection.getAdmin();
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(webPageTable);
            ColumnFamilyDescriptorBuilder anchorFamilyBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(contextFamily.getBytes());
            tableDescriptorBuilder.setColumnFamily(anchorFamilyBuilder.build());
            if (!admin.tableExists(webPageTable))
                admin.createTable(tableDescriptorBuilder.build());
            connection.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void put(WebDocument document) {
        try (Connection connection = ConnectionFactory.createConnection(configuration)) {
//        for(WebDocument document : documents){
            Table t = connection.getTable(webPageTable);

            Put put = new Put(Bytes.toBytes(document.getPagelink()));
            put.addColumn(contextFamily.getBytes(), "pageLink".getBytes(), document.getPagelink().getBytes());
//            put.addColumn(contextFamily.getBytes(),"outlinks".getBytes(),document.getLinks().forEach(e->e.getUrl().getBytes()));
//        }
            t.put(put);
            System.out.println("added");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String invertUrl(String url) {
        return null;
    }
}
