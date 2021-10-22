import static org.junit.Assert.*;
import org.junit.Test;

import java.util.EnumMap;

public class TestArrayDequeGold {
    /**
     * @source from StudentArrayDequeLauncher
     */
    @Test
    public void testBoth() {
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> sad = new StudentArrayDeque();
        String message = null;
        double numberBetweenZeroAndOne;

        for (int i = 0; i < 100; i += 1) {
            numberBetweenZeroAndOne = StdRandom.uniform();
            /**
             * number less than 0.5 test first, otherwise test last.
             */
            if (numberBetweenZeroAndOne < 0.5) {
                if (numberBetweenZeroAndOne < 0.25) {
                    if (!sad.isEmpty()) {
                        Integer actual = sad.removeFirst();
                        Integer expected =  ads.removeFirst();
                        if (message == null) {
                            message = "removeFirst()\n";
                        } else {
                            message =  message + "removeFirst()\n";
                        }
                        assertEquals(expected, actual);
                    }
                } else {
                    sad.addFirst(i);
                    ads.addFirst(i);
                    if (message == null) {
                        message = "addFirst(" + i + ")\n";
                    } else {
                        message = message + "addFirst(" + i + ")\n";
                    }
                    assertEquals(ads.get(0), sad.get(0));
                }
            } else {
                if (numberBetweenZeroAndOne < 0.75) {
                    if (!sad.isEmpty()) {
                        Integer actual = sad.removeLast();
                        Integer expected = ads.removeLast();
                        if (message == null) {
                            message = "removeLast()\n";
                        } else {
                            message = message + "removeLast()\n";
                        }
                        assertEquals(message, expected, actual); // ******
                    }
                } else {
                    sad.addLast(i);
                    ads.addLast(i);
                    if (message == null) {
                        message = "addLast(" + i +")";
                    } else{
                        message = message + "addLast(" + i + ")\n";
                    }
                    assertEquals(message, ads.get(ads.size() - 1), sad.get(sad.size() - 1)); // *******
                }
            }
        }
    }
}
