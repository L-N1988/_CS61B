import java.io.ObjectStreamException;

public class ArrayDeque<T> {
    private int size;
    private T[] ALList;
    private int sentinel;
    private int front;
    private int tail;

    public ArrayDeque() {
        size = 0;
        ALList = (T[]) new Object[8];
        sentinel = 0;
        ALList[sentinel] = null;
        front = 0;
        tail = 0;
    }

    public ArrayDeque(ArrayDeque other) {
        size = other.size;
        ALList = (T[]) new Object[other.ALList.length];
        sentinel = other.sentinel;
        ALList[sentinel] = null;
        front = other.front;
        tail = other.tail;
        System.arraycopy(other.ALList, 0, ALList, 0, ALList.length);
    }

    private void resize() {
        T[] tmp = (T[]) new Object[ALList.length * 2];
        System.arraycopy(ALList, 0, tmp, 0, tail + 1);
        System.arraycopy(ALList, front, tmp,
                tmp.length - (ALList.length - front), ALList.length - front);
        front = tmp.length - (ALList.length - front);
        ALList = tmp;
    }

    public void addFirst(T item) {
        front = front - 1;
        front = (front < 0) ? front + ALList.length : front;
        if (front == tail && front != sentinel) {
            /** Resize the array.
             *  Change the front position */
            resize();
        }
        ALList[front] = item;
        size += 1;
    }

    public void addLast(T item) {
        tail = tail + 1;
        tail = (tail < ALList.length) ? tail : tail % ALList.length;
        if (front == tail && tail != sentinel) {
            /** Resize the array.
             *  Change the front position */
            resize();
        }
        ALList[tail] = item;
        size += 1;
    }

    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        int i = front;
        while (i != tail) {
            if (i == sentinel) {
                i = i + 1;
            }
            System.out.print(ALList[i] + " ");
            i = i + 1;
            i = (i < ALList.length) ? i : i % ALList.length;
        }
        System.out.print("\n");
    }

    public T removeFirst() {
        if (front == sentinel) {
            return null;
        }
        T tmp = ALList[front];
        front = front + 1;
        front = (front < ALList.length) ? front : front % ALList.length;
        size = size - 1;
        return tmp;
    }

    public T removeLast() {
        if (tail == sentinel) {
            return null;
        }
        T tmp = ALList[tail];
        tail = tail - 1;
        size = size - 1;
        return tmp;
    }

    public T get(int index) {
        int i;
        if (front == sentinel && tail == sentinel) {
            return null;
        }
        i = front + index;
        i = (i < ALList.length) ? i : i % ALList.length + 1;
        if (i > tail) {
            return null;
        }
        return ALList[i];
    }
}