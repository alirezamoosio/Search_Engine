package ir.nimbo.searchengine.crawler;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import ir.nimbo.searchengine.exception.IllegalLangaugeException;

public class LanguageDetector {
    static {
        try {
            DetectorFactory.loadProfile("./profiles");
        } catch (LangDetectException e) {
            e.printStackTrace();
        }
    }


    public static void languageCheck(String text) throws IllegalLangaugeException {
        Detector detector;
        try {
            detector = DetectorFactory.create();
            detector.append(text);
            if (!detector.detect().equals("en")){
                throw new IllegalLangaugeException();
            }
        } catch (LangDetectException e) {
            e.printStackTrace();
            throw new IllegalLangaugeException();

        }
    }
}
