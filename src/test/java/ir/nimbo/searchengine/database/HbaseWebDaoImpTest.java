package ir.nimbo.searchengine.database;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class HbaseWebDaoImpTest {
    @Test
    public void createTest(){
        HbaseWebDaoImp hbaseWebDaoImp = new HbaseWebDaoImp();
        Assert.assertTrue(hbaseWebDaoImp.isFlag());
    }
}