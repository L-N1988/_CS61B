import edu.princeton.cs.algs4.In;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Iterator;

public class TrieTest {
    static String dictPath = "/home/liuning/Desktop/RoadToTheWorld";

    @Test
    public void testPrefix() {
        In in = new In(dictPath);
        Trie trie = new Trie();
        int cnt = 0;
        while (!in.isEmpty()) {
            String s = in.readString();
            cnt += 1;
            trie.put(s);
        }
        assertEquals(cnt, trie.size());
        in = new In(dictPath);
        while (!in.isEmpty()) {
            String s = in.readString();
            assertTrue(trie.contains(s));
        }
        for (String s : trie.keyWithPrefix("la")) {
            System.out.println(s);
        }
    }
}
