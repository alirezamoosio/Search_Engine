package ir.nimbo.searchengine.util;

import org.junit.Assert;
import org.junit.Test;

public class ConfigManagerTest {
    @Test
    public void test() {
        String hbaseFamily = ConfigManager.getInstance().getProperty(PropertyType.H_BASE_FAMILY_1);
        String outLinksName = ConfigManager.getInstance().getProperty(PropertyType.H_BASE_COLUMN_OUT_LINKS);
        String pageRankName = ConfigManager.getInstance().getProperty(PropertyType.H_BASE_COLUMN_PAGE_RANK);
        String tableName = ConfigManager.getInstance().getProperty(PropertyType.H_BASE_TABLE);
        Assert.assertEquals("context", hbaseFamily);
        Assert.assertEquals("outLinks", outLinksName);
        Assert.assertEquals("pageRank", pageRankName);
        Assert.assertEquals("webpage", tableName);
    }
}