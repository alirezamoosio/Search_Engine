package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ParserTest {

    @Test
    public void parse() throws IOException, IllegalLanguageException {
        Assert.assertTrue(10<Parser.parse("https://alexa.com/topsites").getLinks().size());
    }
}