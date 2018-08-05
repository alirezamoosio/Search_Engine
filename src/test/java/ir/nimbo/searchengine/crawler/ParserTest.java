package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ParserTest {

    @Test
    public void parse() throws IOException, IllegalLanguageException {
        Assert.assertTrue(1<Parser.parse("https://zoomg.ir/").getLinks().size());
    }
}