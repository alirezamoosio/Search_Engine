package ir.nimbo.searchengine.crawler;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


public class DuplicateLinkHandlerTest {

    @Test
    public void isDuplicate() {
        DuplicateLinkHandler.getInstance().confirm("https://www.linkedin.com/shareArticle?mini=true&title=Build%20a%20user%20settings%20store%20with%20AWS%20AppSync&source=Amazon%20Web%20Services&summary=Amazon%20Cognito%20Sync%20is%20a%20service%20that%20you%20can%20use%20for%20syncing%20application-related%20user%20profile%20data%20across%20devices.%20The%20client%20library%20caches%20the%20data%20locally%20so%20that%20the%20app%20can%20read%20and%20write%20data%2C%20regardless%20of%20the%20device%20connectivity%20state.%20The%20data%20stored%20is%20limited%20to%20key-value%20pairs%20where%20the%20keys%20and%20values%20are%20both%20%5B%26hellip%3B%5D&url=https://aws.amazon.com/blogs/mobile/build-a-user-settings-store-with-aws-appsync/");
        Assert.assertTrue(DuplicateLinkHandler.getInstance().isDuplicate("https://www.linkedin.com/shareArticle?mini=true&title=Build%20a%20user%20settings%20store%20with%20AWS%20AppSync&source=Amazon%20Web%20Services&summary=Amazon%20Cognito%20Sync%20is%20a%20service%20that%20you%20can%20use%20for%20syncing%20application-related%20user%20profile%20data%20across%20devices.%20The%20client%20library%20caches%20the%20data%20locally%20so%20that%20the%20app%20can%20read%20and%20write%20data%2C%20regardless%20of%20the%20device%20connectivity%20state.%20The%20data%20stored%20is%20limited%20to%20key-value%20pairs%20where%20the%20keys%20and%20values%20are%20both%20%5B%26hellip%3B%5D&url=https://aws.amazon.com/blogs/mobile/build-a-user-settings-store-with-aws-appsync/"));
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