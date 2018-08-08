package ir.nimbo.searchengine.crawler;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class DuplicateLinkHandlerTest {

    @Test
    public void isDuplicate() {
        DuplicateLinkHandler.getInstance().confirm("salam!");
        Assert.assertTrue(DuplicateLinkHandler.getInstance().isDuplicate("salam!"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("che khabar"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("che khabar"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("che khabar"));
        DuplicateLinkHandler.getInstance().confirm("che khabar");
        Assert.assertTrue(DuplicateLinkHandler.getInstance().isDuplicate("che khabar"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("che khabar546ghie45r78/3r/f/3f///3r3fe"));
    }

    @Test
    public void loadHashTable() {
        DuplicateLinkHandler.getInstance().isDuplicate("salam");
        DuplicateLinkHandler.getInstance().confirm("salam");
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