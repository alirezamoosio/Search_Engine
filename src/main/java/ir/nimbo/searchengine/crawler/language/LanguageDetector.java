package ir.nimbo.searchengine.crawler.language;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import ir.nimbo.searchengine.exception.IllegalLanguageException;

public class LanguageDetector {
    static {
        try {
            String path = LanguageDetector.class.getClassLoader().getResource("profiles").getPath();
            DetectorFactory.loadProfile(path);
        } catch (LangDetectException e) {
            e.printStackTrace();
        }
    }


    public static void languageCheck(String text) throws IllegalLanguageException {
        Detector detector;
        try {
            detector = DetectorFactory.create();
            detector.append(text);
            if (!detector.detect().equals("en")) {
                throw new IllegalLanguageException();
            }
        } catch (LangDetectException e) {
            throw new IllegalLanguageException();
        }
    }
}
