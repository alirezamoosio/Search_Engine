package ir.nimbo.searchengine.crawler.language;

import ir.nimbo.searchengine.exception.IllegalLanguageException;
import org.junit.Test;

public class LanguageDetectorTest {

    @Test
    public void languageCheck() throws IllegalLanguageException {
        LanguageDetector.profileLoad();
        LanguageDetector.languageCheck("hello everybody. i am a sentence. nice to meet you");
    }
}