package ir.nimbo.searchengine.database;

import ir.nimbo.searchengine.crawler.WebDocument;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class HbaseWebDaoImp implements WebDoa {
    private TableName webPageTable = TableName.valueOf("webpage3");
    private boolean flag =false;
    private String contextFamily = "context";
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
            ColumnFamilyDescriptorBuilder anchorFamilyBuilder = ColumnFamilyDescriptorBuilder
                    .newBuilder(contextFamily.getBytes());
            tableDescriptorBuilder.setColumnFamily(anchorFamilyBuilder.build());
            admin.createTable(tableDescriptorBuilder.build());
            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean isFlag(){
        return flag;
    }
    @Override
    public void put(WebDocument document) {
        Put put = new Put(Bytes.toBytes(invertUrl(document.getPagelink())));
        put.addColumn(contextFamily.getBytes(), "pageLink".getBytes(), document.getPagelink().getBytes());
//        put.addColumn(contextFamily.getBytes(), "anchors".getBytes(), document.ge);

    }

    public String invertUrl(String url){
        return null;
    }
}
