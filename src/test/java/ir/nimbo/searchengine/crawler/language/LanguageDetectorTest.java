package ir.nimbo.searchengine.crawler.language;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.junit.Test;

public class LanguageDetectorTest {

    @Test
    public void languageCheck() throws IllegalLanguageException {
        LanguageDetector.languageCheck("hi how are you");
    }
}