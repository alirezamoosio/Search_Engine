package ir.nimbo.searchengine.politecrawling;

public class DuplicateLinkHandler {
    private static DuplicateLinkHandler ourInstance = new DuplicateLinkHandler();

    public static DuplicateLinkHandler getInstance() {
        return ourInstance;
    }

    private DuplicateLinkHandler() {
    }
}
