package ir.nimbo.searchengine.crawler;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class DuplicateLinkHandlerTest {

    @Test
    public void isDuplicate() {
        DuplicateLinkHandler.getInstance().isDuplicate("salam!");
        Assert.assertTrue(DuplicateLinkHandler.getInstance().isDuplicate("salam!"));
        Assert.assertFalse(DuplicateLinkHandler.getInstance().isDuplicate("che khabar"));
    }
}