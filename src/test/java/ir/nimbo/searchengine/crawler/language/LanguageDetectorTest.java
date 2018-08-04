package ir.nimbo.searchengine.crawler.language;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.junit.Test;

public class LanguageDetectorTest {

    @Test(expected = IllegalLanguageException.class)
    public void languageCheck() throws IllegalLanguageException {
        LanguageDetector.languageCheck("سلام خوبی؟");
    }
}