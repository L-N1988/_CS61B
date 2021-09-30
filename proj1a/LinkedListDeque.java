/**
 * Performs some basic linked list function.
 * @author Liu Ning
 */
public class LinkedListDeque<T> {
    /**
     * Private class.
     */
    private class StuffNode {
        private final T item;
        private StuffNode prev;
        private StuffNode next;
        /**
         * constructor of StuffNode class.
         * @param i item.
         * @param p previous pointer.
         * @param n next pointer.
         */
        private StuffNode(T i, StuffNode p, StuffNode n) {
            item = i;
            prev = p;
            next = n;
        }
    }
    private int size;
    private StuffNode sentinel;

    /**
     * Constructor.
     */
    public LinkedListDeque() {
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
        size = 0;
    }

    /**
     * Constructor of deque.
     * @param i item.
     * @param p previous point.
     * @param n next point.
     */
    public LinkedListDeque(T i, StuffNode p, StuffNode n) {
        sentinel = new StuffNode(null, null, null);
        sentinel.next = new StuffNode(i, sentinel, sentinel);
        sentinel.prev = sentinel.next;
        size = 1;
    }

    /**
     * Constructor of deque.
     * @param other another deque.
     */
    public LinkedListDeque(LinkedListDeque other) {
        sentinel = new StuffNode(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;

        StuffNode ptr = other.sentinel.next;
        while (ptr != other.sentinel) {
            addLast(ptr.item);
            ptr = ptr.next;
        }
        size = other.size();
    }

    /**
     * Return size of deque.
     * @return size of deque.
     */
    public int size() {
        return size;
    }

    /**
     * Adjust the deque is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Print out the whole deque.
     */
    public void printDeque() {
        StuffNode ptr = sentinel;
        while (ptr.next != sentinel) {
            System.out.print(ptr.item + " ");
            ptr = ptr.next;
        }
        System.out.print("\n");
    }

    /**
     * Using the sentinel (or dummy head)
     * make the codes simple and beautiful.
     * @param it item to be inserted.
     */
    public void addFirst(T it) {
        StuffNode tmp = new StuffNode(it, sentinel, sentinel.next);
        sentinel.next = tmp;
        tmp.next.prev = tmp;
        size += 1;
    }

    /**
     * Simple but efficient code.
     * @param it item to be inserted.
     */
    public void addLast(T it) {
        StuffNode tmp = new StuffNode(it, sentinel.prev, sentinel);
        tmp.prev.next = tmp;
        sentinel.prev = tmp;
        size += 1;
    }

    /**
     * Remove the first item in the deque.
     * @return the first item.
     */
    public T removeFirst() {
        if (sentinel.next == sentinel) {
            return null;
        } else {
            StuffNode tmp = sentinel.next;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return tmp.item;
        }
    }

    /**
     * Remove the last item.
     * @return the last item in the deque.
     */
    public T removeLast() {
        if (sentinel.next == sentinel) {
            return null;
        } else {
            StuffNode tmp = sentinel.prev;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
            size -= 1;
            return tmp.item;
        }
    }

    /**
     * Get the i_th item in the deque.
     * @param index position of the item.
     * @return the i_th item
     */
    public T get(int index) {
        StuffNode ptr = sentinel.next;
        int i;
        if (ptr == sentinel) {
            return null;
        }
        for (i = 0; i < index; i++) {
            ptr = ptr.next;
            if (ptr == sentinel) {
                return null;
            }
        }
        return ptr.item;
    }

    /**
     * Get the i_th item using recursive way.
     * Help method of the getRecursive function.
     * @param index i_th position.
     * @param ptr the deque's head pointer.
     * @return the i_th item.
     */
    private T getRecursive(int index, StuffNode ptr) {
        if (ptr == sentinel) {
            return null;
        } else {
            if (index == 0) {
                return ptr.item;
            } else {
                return getRecursive(index - 1, ptr.next);
            }
        }
    }

    /**
     * Get i_th item using recursive way.
     * @param index i_th position.
     * @return i_th item.
     */
    public T getRecursive(int index) {
        return getRecursive(index, sentinel.next);
    }
}
