import org.junit.Test;
import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

public class BoggleTest {

    @Test
    // dictPath is "words.txt"
    public void testBoggleSolverOne() {
        List<String> L = Boggle.solve(7, "exampleBoard.txt");
        List<String> expected = new LinkedList<>();
        expected.add("thumbtacks");
        expected.add("thumbtack");
        expected.add("setbacks");
        expected.add("setback");
        expected.add("ascent");
        expected.add("humane");
        expected.add("smacks");
        assertEquals(expected.size(), L.size());
        for (int i = 0; i < L.size(); i++) {
            System.out.println(L.get(i));
            assertEquals(expected.get(i), L.get(i));
        }
    }

    @Test
    // dictPath is "trivial_words.txt"
    public void testBoggleSolverTwo() {
        List<String> L = Boggle.solve(20, "exampleBoard2.txt");
        List<String> expected = new LinkedList<>();
        expected.add("aaaaa");
        expected.add("aaaa");
        assertEquals(expected.size(), L.size());
        for (int i = 0; i < L.size(); i++) {
            System.out.println(L.get(i));
            assertEquals(expected.get(i), L.get(i));
        }
    }
}
