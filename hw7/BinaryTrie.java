import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    Node root;

    private class Node implements Comparable<Node>, Serializable {
        char ch;
        int freq;
        Node left;
        Node right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        public int compareTo(Node o) {
            return Integer.compare(this.freq, o.freq);
        }
    }

    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<>();
        for (Map.Entry<Character, Integer> ch : frequencyTable.entrySet()) {
            pq.insert(new Node(ch.getKey(), ch.getValue(), null, null));
        }
        // merge nodes by ascending frequency
        while (pq.size() > 1) {
            Node lest = pq.delMin();
            Node less = pq.delMin();
            Node parent = new Node('\0', lest.freq + less.freq, lest, less);
            pq.insert(parent);
        }
        root = pq.delMin();
    }

    // if existed, return longest prefix match character. Otherwise, return '\0'
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node cur = root;
        int i;
        for (i = 0; i < querySequence.length(); i++) {
            if (querySequence.bitAt(i) == 0) {
                if (cur.left == null) {
                    break;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    break;
                }
                cur = cur.right;
            }
        }
        return new Match(querySequence.firstNBits(i), cur.ch);
    }

    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> map = new HashMap<>();
        buildLookupTableHelper(root, new BitSequence(), map);
        return map;
    }

    // postorder traverse, but only act on leaf nodes
    private void buildLookupTableHelper(Node cur, BitSequence bits,
                                        Map<Character, BitSequence> map) {
        if (cur == null) {
            return;
        }
        buildLookupTableHelper(cur.left, bits.appended(0), map);
        buildLookupTableHelper(cur.right, bits.appended(1), map);
        // bits append uses a non-destruct way implementation,
        // so bits in this method does not change. only input leaf nodes in trie
        if (cur.left == null && cur.right == null) {
            map.put(cur.ch, bits);
        }
    }
}
