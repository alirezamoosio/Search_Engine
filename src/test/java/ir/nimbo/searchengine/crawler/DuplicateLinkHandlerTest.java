package ir.nimbo.searchengine.crawler;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class DuplicateLinkHandlerTest {

    @Test
    public void isDuplicate() {
        DuplicateLinkHandler.getInstance().isDuplicate("salam!");
        Assert.assertTrue(DuplicateLinkHandler.getInstance().isDuplicate("salam!"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("che khabar"));
    }

    @Test
    public void loadHashTable() {
        DuplicateLinkHandler.getInstance().isDuplicate("salam");
        DuplicateLinkHandler.getInstance().saveHashTable();
        try {
            DuplicateLinkHandler.getInstance().loadHashTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(DuplicateLinkHandler.getInstance().isDuplicate("salam"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate(" che khabar"));
    }
    @Test
    public void saveHashTable() {
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("salam"));
        DuplicateLinkHandler.getInstance().saveHashTable();
    }

}