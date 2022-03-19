import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int maxStringLen = 0;
        String[] sameLenStr = new String[asciis.length];
        System.arraycopy(asciis, 0, sameLenStr, 0, asciis.length);
        for (String ascii : asciis) {
            maxStringLen = Math.max(maxStringLen, ascii.length());
        }
        // do not uniform all strings' length by padding them on the right using any code
        // instead extend count array to store char occurrence using original string length
        for (int i = 0; i < maxStringLen; i++) {
            // the lest significant digit has the most significant position
            sortHelperLSD(sameLenStr, maxStringLen - 1 - i);
        }
        return sameLenStr;
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        // extend one position array to store missing part of shorter string
        int[] count = new int[256 + 1];
        for (int i = 0; i < asciis.length; i++) {
            int val = charOrNone(index, asciis[i]);
            count[val] += 1;
        }
        int[] start = new int[256 + 1];
        int pos = 0;
        for (int i = 0; i < start.length; i++) {
            start[i] = pos;
            pos += count[i];
        }
        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            String item = asciis[i];
            int startPos = start[charOrNone(index, item)];
            sorted[startPos] = item;
            start[charOrNone(index, item)] += 1;
        }
        System.arraycopy(sorted, 0, asciis, 0, sorted.length);
    }

    private static int charOrNone(int i, String ascii) {
        if (i < ascii.length()) {
            return ascii.charAt(i) + 1;
        }
        return 0;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        return;
    }

    public static void main(String[] args) {
        String[] unsorted = new String[] {"9", "80", "600", "4", "20000", "10000"};
        String[] sorted = sort(unsorted);
        System.out.println(Arrays.toString(unsorted));
        System.out.println(Arrays.toString(sorted));
    }
}
