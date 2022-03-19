import java.util.Arrays;

/**
 * Class with 2 ways of doing Counting sort, one naive way and one "better" way
 *
 * @author Akhil Batra, Alexander Hwang
 *
 **/
public class CountingSort {
    /**
     * Counting sort on the given int array. Returns a sorted version of the array.
     * Does not touch original array (non-destructive method).
     * DISCLAIMER: this method does not always work, find a case where it fails
     *
     * @param arr int array that will be sorted
     * @return the sorted array
     */
    public static int[] naiveCountingSort(int[] arr) {
        // find max
        int max = Integer.MIN_VALUE;
        for (int i : arr) {
            max = max > i ? max : i;
        }

        // gather all the counts for each value
        int[] counts = new int[max + 1];
        for (int i : arr) {
            counts[i]++;
        }

        // when we're dealing with ints, we can just put each value
        // count number of times into the new array
        int[] sorted = new int[arr.length];
        int k = 0;
        for (int i = 0; i < counts.length; i += 1) {
            for (int j = 0; j < counts[i]; j += 1, k += 1) {
                sorted[k] = i;
            }
        }

        // however, below is a more proper, generalized implementation of
        // counting sort that uses start position calculation
        int[] starts = new int[max + 1];
        int pos = 0;
        for (int i = 0; i < starts.length; i += 1) {
            starts[i] = pos;
            pos += counts[i];
        }

        int[] sorted2 = new int[arr.length];
        for (int i = 0; i < arr.length; i += 1) {
            int item = arr[i];
            int place = starts[item];
            sorted2[place] = item;
            starts[item] += 1;
        }

        // return the sorted array
        return sorted;
    }

    /**
     * Counting sort on the given int array, must work even with negative numbers.
     * Note, this code does not need to work for ranges of numbers greater
     * than 2 billion.
     * Does not touch original array (non-destructive method).
     * use radix sort of string to treat integer number overflow
     * but too slowwwwwwwwwwwwwww!!!!!!!!!!
     *
     * @param arr int array that will be sorted
     */
    public static int[] betterCountingSort(int[] arr) {
        int[] tmp = new int[arr.length];
        int[] posArr;
        int[] negArr;
        int negativeIndex = -1;
        int positiveIndex = arr.length;
        for (int item : arr) {
            if (item < 0) {
                negativeIndex += 1;
                tmp[negativeIndex] = -1 * item;
            } else {
                positiveIndex -= 1;
                tmp[positiveIndex] = item;
            }
        }
        posArr = new int[arr.length - 1 - negativeIndex];
        negArr = new int[negativeIndex + 1];
        if (positiveIndex == arr.length) {
            // only negative items
            System.arraycopy(tmp, 0, negArr, 0, negArr.length);
        } else if (negativeIndex == -1) {
            // only positive items
            System.arraycopy(tmp, positiveIndex, posArr, 0, posArr.length);
        } else {
            System.arraycopy(tmp, 0, negArr, 0, negArr.length);
            System.arraycopy(tmp, positiveIndex, posArr, 0, posArr.length);
        }
        // ascending
        int[] positiveSort = betterCountingSortHelper(posArr);
        // descending (item is positive actually, so it should reverse order matching ascending
        int[] negativeSort = betterCountingSortHelper(negArr);
        for (int i = 0; i < tmp.length; i++) {
            if (i > negativeIndex) {
                // set positive item to the right end
                tmp[i] = positiveSort[i - negativeIndex - 1];
            } else {
                // reverse negative item
                tmp[i] = -1 * negativeSort[negativeIndex - i];
            }
        }
        return tmp;
    }

    private static int[] betterCountingSortHelper(int[] arr) {
        if (arr.length == 0) {
            return arr;
        }
        String[] asciis = new String[arr.length];
        int[] sorted = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            asciis[i] = Integer.toString(arr[i]);
        }
        int maxStringLen = 0;
        for (String ascii : asciis) {
            maxStringLen = Math.max(maxStringLen, ascii.length());
        }
        String[] sameLenStr = new String[asciis.length];
        // uniform all strings' length by padding them on the right
        // padding uses white space
        for (int i = 0; i < asciis.length; i++) {
            // add leading zeros
            StringBuilder padStr =
                    new StringBuilder("0".repeat(Math.max(0, maxStringLen - asciis[i].length())));
            sameLenStr[i] = padStr + asciis[i];
        }
        for (int i = 0; i < maxStringLen; i++) {
            // the lest significant digit has the most significant position
            sortHelperLSD(sameLenStr, maxStringLen - 1 - i);
        }
        for (int i = 0; i < asciis.length; i++) {
            sorted[i] = Integer.parseInt(sameLenStr[i]);
        }
        return sorted;
    }


    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static void sortHelperLSD(String[] asciis, int index) {
        int[] count = new int[256];
        for (int i = 0; i < asciis.length; i++) {
            int val = asciis[i].charAt(index);
            count[val] += 1;
        }
        int[] start = new int[256];
        int pos = 0;
        for (int i = 0; i < count.length; i++) {
            start[i] = pos;
            pos += count[i];
        }
        String[] sorted = new String[asciis.length];
        for (int i = 0; i < asciis.length; i++) {
            String item = asciis[i];
            int startPos = start[item.charAt(index)];
            sorted[startPos] = item;
            start[item.charAt(index)] += 1;
        }
        System.arraycopy(sorted, 0, asciis, 0, sorted.length);
    }

    public static void main(String[] args) {
        int[] nonNegative = {2147483647, 5, 2, 1, 5, 3, 0, 3, 1, 1};
        System.out.println(Arrays.toString(nonNegative));
        int[] sorted = betterCountingSort(nonNegative);
        System.out.println(Arrays.toString(sorted));
    }
}
