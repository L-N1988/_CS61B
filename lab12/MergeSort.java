import edu.princeton.cs.algs4.Queue;

public class MergeSort {
    /**
     * Removes and returns the smallest item that is in q1 or q2.
     *
     * The method assumes that both q1 and q2 are in sorted order, with the smallest item first. At
     * most one of q1 or q2 can be empty (but both cannot be empty).
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      The smallest item that is in q1 or q2.
     */
    private static <Item extends Comparable<Item>> Item getMin(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1.isEmpty()) {
            return q2.dequeue();
        } else if (q2.isEmpty()) {
            return q1.dequeue();
        } else {
            // Peek at the minimum item in each queue (which will be at the front, since the
            // queues are sorted) to determine which is smaller.
            Comparable q1Min = q1.peek();
            Comparable q2Min = q2.peek();
            if (q1Min.compareTo(q2Min) <= 0) {
                // Make sure to call dequeue, so that the minimum item gets removed.
                return q1.dequeue();
            } else {
                return q2.dequeue();
            }
        }
    }

    /** Returns a queue of queues that each contain one item from items. */
    private static <Item extends Comparable<Item>> Queue<Queue<Item>>
            makeSingleItemQueues(Queue<Item> items) {
        Queue<Queue<Item>> singleResult = new Queue<>();
        int N = items.size();
        for (int i = 0; i < N; i++) {
            Queue<Item> item = new Queue<>();
            item.enqueue(items.dequeue());
            singleResult.enqueue(item);
            // put back items in queue.
            items.enqueue(item.peek());
        }
        return singleResult;
    }

    /**
     * Returns a new queue that contains the items in q1 and q2 in sorted order.
     *
     * This method should take time linear in the total number of items in q1 and q2.  After
     * running this method, q1 and q2 will be empty, and all of their items will be in the
     * returned queue.
     *
     * @param   q1  A Queue in sorted order from least to greatest.
     * @param   q2  A Queue in sorted order from least to greatest.
     * @return      A Queue containing all of the q1 and q2 in sorted order, from least to
     *              greatest.
     *
     */
    private static <Item extends Comparable<Item>> Queue<Item> mergeSortedQueues(
            Queue<Item> q1, Queue<Item> q2) {
        if (q1 == null) {
            return q2;
        }
        if (q2 == null) {
            return q1;
        }
        Queue<Item> mergeResult = new Queue<>();
        while (!q1.isEmpty() || !q2.isEmpty()) {
            mergeResult.enqueue(getMin(q1, q2));
        }
        return mergeResult;
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable<Item>> Queue<Item> mergeSort(
            Queue<Item> items) {
        // print unsorted queue.
        Queue<Queue<Item>> singleItems = makeSingleItemQueues(items);
        if (items.isEmpty()) {
            return items;
        }
        while (singleItems.size() != 1) {
            singleItems.enqueue(mergeSortedQueues(singleItems.dequeue(), singleItems.dequeue()));
        }
        // print sorted queue.
        return singleItems.dequeue();
    }

    public static void main(String[] args) {
        Queue<Integer> numbers = new Queue<>();
        System.out.println(mergeSort(numbers));
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
        System.out.println("sorted data: " + mergeSort(numbers));

        Queue<String> students = new Queue<>();
        students.enqueue("Alice");
        students.enqueue("Vanessa");
        students.enqueue("Ethan");
        System.out.println("raw data: " + students);
        System.out.println("sorted data: " + mergeSort(students));
    }
}
