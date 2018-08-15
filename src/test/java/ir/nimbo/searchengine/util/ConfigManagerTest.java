package ir.nimbo.searchengine.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConfigManagerTest {
    @Test
    public void test() {
        String hbaseFamily = ConfigManager.getInstance().getProperty(PropertyType.HBASE_FAMILY_1);
        String outLinksName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_OUTLINKS);
        String pageRankName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_COLUMN_PAGERANK);
        String tableName = ConfigManager.getInstance().getProperty(PropertyType.HBASE_TABLE);
        Assert.assertEquals("context", hbaseFamily);
        Assert.assertEquals("outLinks", outLinksName);
        Assert.assertEquals("pageRank", pageRankName);
        Assert.assertEquals("webpage", tableName);
    }
}