package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.Initializer;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import org.junit.Test;

import java.io.IOException;

public class ParserTest {

    @Test
    public void parse() throws IOException, IllegalLanguageException, DomainFrequencyException, DuplicateLinkException, URLException {
        Initializer.initialize();
        System.out.println(Parser.getInstance().parse("http://careers.q4inc.com").getTextDoc());
    }
}