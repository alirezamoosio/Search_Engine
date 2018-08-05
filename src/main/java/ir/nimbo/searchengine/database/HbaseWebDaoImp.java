package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HbaseWebDaoImp implements WebDoa {
    private TableName webPageTable = TableName.valueOf("webpage");
    private String linkFamily = "link";
    private String anchorFamliy = "anchor";
    private String timeStamp = "timestamp";

    public HbaseWebDaoImp() {
        Configuration configuration = HBaseConfiguration.create();
        String path = this.getClass().getClassLoader().getResource("hbase-site.xml").getPath();
        configuration.addResource(new Path(path));
        try {
            HBaseAdmin.available(configuration);
            createTable(configuration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createTable(Configuration conf) {
        try (Connection connection = ConnectionFactory.createConnection(conf)) {
            Admin admin = connection.getAdmin();
            TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(webPageTable);
            ColumnFamilyDescriptorBuilder linkFamilyBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(linkFamily.getBytes());
            ColumnFamilyDescriptorBuilder anchorFamilyBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(anchorFamliy.getBytes());
            ColumnFamilyDescriptorBuilder timeStampFamilyBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(timeStamp.getBytes());
            tableDescriptorBuilder.setColumnFamily(linkFamilyBuilder.build());
            tableDescriptorBuilder.setColumnFamily(anchorFamilyBuilder.build());
            tableDescriptorBuilder.setColumnFamily(timeStampFamilyBuilder.build());
            admin.createTable(tableDescriptorBuilder.build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(WebDocument document) {

    }
}
