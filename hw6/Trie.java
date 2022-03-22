import edu.princeton.cs.algs4.MinPQ;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Queue;

public class Trie {
    private int n;
    private final Node root;

    private class Node {
        boolean exist;
        Map<Character, Node> links;

        public Node() {
            exist = false;
            links = new HashMap<>();
        }
    }

    public Trie() {
        n = 0;
        root = new Node();
    }

    // returns the number of strings in this trie
    public int size() {
        return n;
    }

    // put string into trie
    public void put(String key) {
        if (key == null) {
            return;
        }
        put(key, root, 0);
    }

    // most worlds' sizes are not longer than 20, so recursion is safe
    private void put(String s, Node root, int index) {
        if (index >= s.length()) {
            return;
        }
        char c = s.charAt(index);
        Node next = root.links.get(c);
        if (next == null) {
            root.links.put(c, new Node());
        }
        if (index == s.length() - 1) {
            root.exist = true;
            n += 1;
        }
        put(s, next, index + 1);
    }

    // determine whether trie contains this specific string
    public boolean contains(String key) {
        if (key == null || key.length() == 0) {
            return false;
        }
        return contains(key, root, 0);
    }

    private boolean contains(String key, Node root, int index) {
        char c = key.charAt(index);
        Node next = root.links.get(c);
        if (next == null) {
            return false;
        } else {
            if (index == key.length() - 1 && next.exist) {
                return true;
            }
            return contains(key, next, index + 1);
        }
    }

    public Iterator<String> keyWithPrefix(String prefix) {
        Queue<String> queue = new LinkedList<>();
        Node start;
        if (prefix == null || prefix.length() == 0) {
            return null;
        }
        start = downAlongPrefix(prefix, root, 0);
        collect(start, new StringBuilder(prefix), queue);
        return queue.iterator();
    }

    private void collect(Node start, StringBuilder prefix, Queue<String> queue) {
        if (start == null) {
            return;
        }
        for (Map.Entry<Character, Node> node : start.links.entrySet()) {
            char c = node.getKey();
            Node next = node.getValue();
            prefix.append(c);
            // dfs search trie
            collect(next, prefix, queue);
            if (start.exist) {
                queue.offer(prefix.toString());
            }
            // pop stack and shift backwards
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    private Node downAlongPrefix(String prefix, Node root, int index) {
        char c = prefix.charAt(index);
        Node next = root.links.get(c);
        if (next == null) {
            return null;
        } else {
            if (index == prefix.length() - 1) {
                return next;
            }
            return downAlongPrefix(prefix, root, index + 1);
        }
    }
}
