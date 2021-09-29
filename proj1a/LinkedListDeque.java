/** Performs some basic linked list function. */
public class LinkedListDeque<T> {
	public class StuffNode {
		private final T item;
		private StuffNode prev;
		private StuffNode next;
		private StuffNode(T i, StuffNode p, StuffNode n) {
			item = i;
			prev = p;
			next = n;
		}
	}
	private int size;
	private StuffNode sentinel;

	/** Constructor. */
	public LinkedListDeque() {
		sentinel = new StuffNode(null, null, null);
		sentinel.prev = sentinel;
		sentinel.next = sentinel;
		size = 0;
	}
	public LinkedListDeque(T i, StuffNode p, StuffNode n) {
		sentinel = new StuffNode(null, null, null);
		sentinel.next = new StuffNode(i, sentinel, sentinel);
		sentinel.prev = sentinel.next;
		size = 1;
	}
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

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		if (size == 0) {
			return true;
		}
		return false;
	}

	public void printDeque() {
		StuffNode ptr = sentinel;
		while (ptr.next != sentinel) {
			System.out.print(ptr.item + " ");
			ptr = ptr.next;
		}
		System.out.println("");
	}

	/** Using the sentinel (or dummy head)
	 * make the codes simple and beautiful.
	 */
	public void addFirst(T it) {
		StuffNode tmp = new StuffNode(it, sentinel, sentinel.next);
		sentinel.next = tmp;
		tmp.next.prev = tmp;
		size += 1;
	}

	/** Simple but efficient code. */
	public void addLast(T it) {
		StuffNode tmp = new StuffNode(it, sentinel.prev, sentinel);
		tmp.prev.next = tmp;
		sentinel.prev = tmp;
		size += 1;
	}

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
	public T getRecursive(int index) {
		return getRecursive(index, sentinel.next);
	}
}
