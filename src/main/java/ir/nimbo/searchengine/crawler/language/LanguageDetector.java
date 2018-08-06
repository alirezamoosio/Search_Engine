package ir.nimbo.searchengine.crawler.language;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import ir.nimbo.searchengine.crawler.Link;
import ir.nimbo.searchengine.exception.IllegalLanguageException;

import java.util.LinkedHashSet;

public class LanguageDetector {
   public static void profileLoad(String address){
       try {
           DetectorFactory.loadProfile(address);
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