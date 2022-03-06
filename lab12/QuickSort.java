import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all the items in q1.
     */
    private static <Item extends Comparable<Item>> Queue<Item>
            catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable<Item>> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable<Item>> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        while (!unsorted.isEmpty()) {
            int cmp = unsorted.peek().compareTo(pivot);
            if (cmp > 0) {
                greater.enqueue(unsorted.dequeue());
            } else if (cmp == 0) {
                equal.enqueue(unsorted.dequeue());
            } else {
                less.enqueue(unsorted.dequeue());
            }
        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable<Item>> Queue<Item> quickSort(
            Queue<Item> items) {
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        Item pivot = getRandomItem(items);
        partition(items, pivot, less, equal, greater);
        if (less.isEmpty() && greater.isEmpty()) {
            return equal;
        }
        Queue<Item> sorted = catenate(quickSort(less), equal);
        sorted = catenate(sorted, quickSort(greater));
        return sorted;
    }

    public static void main(String[] args) {
        Queue<Integer> numbers = new Queue<>();
        numbers.enqueue(9);
        numbers.enqueue(8);
        numbers.enqueue(7);
        numbers.enqueue(6);
        numbers.enqueue(5);
        numbers.enqueue(4);
        numbers.enqueue(3);
        numbers.enqueue(2);
        numbers.enqueue(1);
        numbers.enqueue(0);
        System.out.println("raw data: " + numbers);
        System.out.println("sorted data: " + quickSort(numbers));

        Queue<String> students = new Queue<>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        System.out.println("raw data: " + students);
        System.out.println("sorted data: " + quickSort(students));
    }
}
