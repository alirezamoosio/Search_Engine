package ir.nimbo.searchengine.trend;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class WordCounterTest {

    @Test
    public void tokenize() {
        WordCounter counter = new WordCounter();
        counter.setDocument("this..   is! a teسst test this");
        Map<String, Integer> result = counter.tokenize();
        assertNull(result.get("this"));
        assertNull(result.get("is"));
        assertNull(result.get("a"));
        assertEquals(new Integer(2), result.get("test"));
    }
}