package ir.nimbo.searchengine.crawler;

import ir.nimbo.searchengine.Intiaizer;
import ir.nimbo.searchengine.exception.DomainFrequencyException;
import ir.nimbo.searchengine.exception.DuplicateLinkException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;
import ir.nimbo.searchengine.exception.URLException;
import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ParserTest {

    @Test
    public void parse() throws IOException, IllegalLanguageException, DomainFrequencyException, DuplicateLinkException, URLException {
        Intiaizer.intialize();
        System.out.println(Parser.getInstance().parse("http://careers.q4inc.com").getTextDoc());
    }
}