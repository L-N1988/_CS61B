/**
 * Performs some basic array list function.
 * @author Liu Ning
 */

public class ArrayDeque<T> {
    private int size;
    private T[] aList;
    private int front;
    private int tail;

    /**
     * Constructor of Array Deque class.
     * Create an empty Array Deque.
     * Front point to the middle of array.
     */
    public ArrayDeque() {
        size = 0;
        aList = (T[]) new Object[8];
        front = aList.length / 2;
        tail = front + 1;
    }

    /**
     * Another constructor of Array Deque.
     * Create an Array Deque using deep copy.
     * @param other another array deque.
     */
    public ArrayDeque(ArrayDeque other) {
        size = other.size;
        aList = (T[]) new Object[other.aList.length];
        front = other.front;
        tail = other.tail;
        System.arraycopy(other.aList, 0, aList, 0, aList.length);
    }

    /**
     * Resize the array to fill new items.
     */
    private void resize() {
        int factor = 2;
        T[] tmp = (T[]) new Object[aList.length * factor];
        System.arraycopy(aList, front, tmp, tmp.length - (aList.length - front),
                aList.length - front);
        System.arraycopy(aList, 0, tmp, 0, tail + 1);
        aList = tmp;
        front = tmp.length - (aList.length - front);
    }

    /**
     * Insert item to the front of array deque.
     * @param item inserted item.
     */
    public void addFirst(T item) {
        if (front == tail) {
            resize();
        }
        aList[front] = item;
        size += 1;
        front = front - 1;
        front = (front < 0) ? front + aList.length : front;
    }

    /**
     * Insert item to the tail of deque array.
     * @param item inserted item.
     */
    public void addLast(T item) {
        if (front == tail) {
            resize();
        }
        aList[tail] = item;
        size += 1;
        tail = tail + 1;
        tail = (tail < aList.length) ? tail : tail % aList.length;
    }

    /**
     * Adjust whether the array is empty.
     * @return true if array is empty, otherwise return false.
     */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Return the number of existing items.
     * @return number of items.
     */
    public int size() {
        return size;
    }

    /**
     * Print out the whole array deque from front to tail.
     */
    public void printDeque() {
        int i = front + 1;
        while (i != tail) {
            System.out.print(aList[i] + " ");
            i = i + 1;
            i = (i < aList.length) ? i : i % aList.length;
        }
        System.out.print("\n");
    }

    /**
     * Remove the first item of array deque.
     * @return the first item.
     */
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        T tmp = aList[front + 1];
        aList[front + 1] = null;
        front = front + 1;
        front = (front < aList.length) ? front : front % aList.length;
        size = size - 1;
        return tmp;
    }

    /**
     * Remove the Last item of array deque.
     * @return the last item.
     */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T tmp = aList[tail - 1];
        aList[tail - 1] = null;
        tail = tail - 1;
        tail = (tail < 0) ? tail + aList.length : tail;
        size = size - 1;
        return tmp;
    }

    /**
     * Get the index_th item in the array deque.
     * If index too big, return null.
     * @param index the i_th item's position.
     * @return the i_th item.
     */
    public T get(int index) {
        int i;
        if (isEmpty()) {
            return null;
        }
        i = front + index + 1;
        i = (i < aList.length) ? i : i % aList.length;
        if (i >= tail) {
            return null;
        }
        return aList[i];
    }
}
