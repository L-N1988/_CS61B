import org.junit.Test;
import static org.junit.Assert.*;

public class TestFlik {
    @Test
    public void testIsSameNumber() {
        assertTrue(Flik.isSameNumber(1, 1));
        assertTrue(Flik.isSameNumber(0, 0));
        assertTrue(Flik.isSameNumber(200, 200));
        assertFalse(Flik.isSameNumber(1, 0));
    }
}
