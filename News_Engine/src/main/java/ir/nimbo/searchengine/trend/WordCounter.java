package ir.nimbo.searchengine.trend;

import java.util.*;

public class WordCounter {
    private String document;
    private static Set<String> stopWords = new HashSet<>();

    static {
        Scanner scanner = new Scanner(WordCounter.class.getResourceAsStream("/stop_words"));
        while (scanner.hasNext())
            stopWords.add(scanner.nextLine());
        scanner.close();

    }

    public void setDocument(String document) {
        this.document = document;
    }

    private void normalize() {
        document = document.toLowerCase();
        document = document.replaceAll("[^a-z0-9\\s]", "");
        document = document.replaceAll("\\s+", " ");
    }

    public Map<String, Integer> tokenize() {
        normalize();
        String[] words = document.split(" ");
        Map<String, Integer> map = new HashMap<>();
        for (String word : words) {
            if (!stopWords.contains(word)) {
                if (map.containsKey(word))
                    map.put(word, map.get(word) + 1);
                else
                    map.put(word, 1);
            }
        }
        return map;
    }
}
