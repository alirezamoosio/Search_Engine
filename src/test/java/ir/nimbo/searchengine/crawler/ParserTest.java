package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.exception.IllegalLangaugeException;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void parse() throws IOException, IllegalLangaugeException {
        Parser.parse("https://try.alexa.com/marketing-stack/keyword-difficulty-tool").getLinks().get(6);
    }
}