import org.junit.Test;
import static org.junit.Assert.*;

public class TestOffByN {
    static CharacterComparator offBy5 = new OffByN(5);

    @Test
    public void testEqualChars() {
        assertTrue(offBy5.equalChars('a', 'f'));
        assertTrue(offBy5.equalChars('f', 'a'));
        assertFalse(offBy5.equalChars('f', 'h'));
    }

    /* Run the unit tests in this file. */
    public static void main(String... args) {        
        jh61b.junit.TestRunner.runTests("all", TestOffByN.class);
    }
}
