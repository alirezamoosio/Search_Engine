package ir.nimbo.searchengine;

import ir.nimbo.searchengine.crawler.Parser;
import ir.nimbo.searchengine.crawler.language.LangDetector;

public class Intiaizer {
    public static void intialize(){
        LangDetector langDetector = LangDetector.getInstance();
        Parser.setLangDetector(langDetector);
        langDetector.profileLoad();
    }
}
