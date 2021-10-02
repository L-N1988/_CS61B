public interface Deque<T> {
    void resize();

    void addFirst(T item);

    void addLast(T item);

    default boolean isEmpty() {
        return size() == 0;
    }

    int size();

    void printDeque();

    T removeFirst();

    T removeLast();

    void downSize();

    T get(int index);
}
