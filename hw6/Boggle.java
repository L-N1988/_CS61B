import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.HashSet;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;

public class Boggle {
    private static class Position {
        int x;
        int y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class MyString implements Comparable<MyString> {
        String str;

        MyString(String str) {
            this.str = str;
        }

        @Override
        public int compareTo(MyString o) {
            // reverse compare function, make length max string in the peek of priority queue
            int cmp = -1 * Integer.compare(this.str.length(), o.str.length());
            if (cmp == 0) {
                return this.str.compareTo(o.str);
            } else {
                return cmp;
            }
        }
    }
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    private static Trie trie = new Trie();
    private static String[] board;
    private static boolean[][] marked;

    /**
     * Solves a Boggle puzzle.
     * 
     * @param k The maximum number of words to return.
     * @param boardFilePath The file path to Boggle board file.
     * @return a list of words found in given Boggle board.
     *         The Strings are sorted in descending order of length.
     *         If multiple words have the same length,
     *         have them in ascending alphabetical order.
     */
    public static List<String> solve(int k, String boardFilePath) {
        int height = 0;
        int width;
        In in = new In(dictPath);
        MinPQ<MyString> pq = new MinPQ<>();
        List<String> ret = new LinkedList<>();
        // read in all dict words
        while (!in.isEmpty()) {
            String s = in.readString();
            trie.put(s);
        }
        // count board strings number
        in = new In(boardFilePath);
        while (!in.isEmpty()) {
            in.readString();
            height += 1;
        }
        board = new String[height];
        in = new In(boardFilePath);
        height = 0;
        // create board
        while (!in.isEmpty()) {
            board[height] = in.readString();
            height += 1;
        }
        width = board[0].length();
        marked = new boolean[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boardDFS(i, j, new StringBuilder(), pq);
            }
        }
        // poll k max length words from pq
        for (int i = 0; i < k;) {
            if (pq.isEmpty()) {
                break;
            }
            String item = pq.delMin().str;
            if (!ret.contains(item)) {
                ret.add(item);
                i += 1;
            }
        }
        return ret;
    }

    private static void boardDFS(int x, int y, StringBuilder s, MinPQ<MyString> pq) {
        if (!marked[y][x]) {
            // non-destruct way to use string builder object
            StringBuilder str = new StringBuilder(s);
            str.append(board[y].charAt(x));
            marked[y][x] = true;
            if (str.length() >= 3 && trie.contains(str.toString())) {
                pq.insert(new MyString(str.toString()));
            }
            for (Position next : adjacent(x, y, str.toString())) {
                boardDFS(next.x, next.y, str, pq);
            }
            // when pop recursion stack, clear up marked along the way
            marked[y][x] = false;
        }
    }

    // horizontal, vertical and diagonal
    private static Set<Position> adjacent(int x, int y, String c) {
        Set<Position> dir = new HashSet<>();
        StringBuilder right = new StringBuilder(c);
        StringBuilder left = new StringBuilder(c);
        StringBuilder up = new StringBuilder(c);
        StringBuilder down = new StringBuilder(c);
        StringBuilder upR = new StringBuilder(c);
        StringBuilder upL = new StringBuilder(c);
        StringBuilder downR = new StringBuilder(c);
        StringBuilder downL = new StringBuilder(c);
        if (x + 1 < board[0].length() && y - 1 >= 0 && !marked[y - 1][x + 1]) {
            upR.append(board[y - 1].charAt(x + 1));
            if (trie.hasPrefix(upR.toString())) {
                dir.add(new Position(x + 1, y - 1));
            }
        }
        if (x - 1 >= 0 && y - 1 >= 0 && !marked[y - 1][x - 1]) {
            upL.append(board[y - 1].charAt(x - 1));
            if (trie.hasPrefix(upL.toString())) {
                dir.add(new Position(x - 1, y - 1));
            }
        }
        if (x - 1 >= 0 && y + 1 < board.length && !marked[y + 1][x - 1]) {
            downL.append(board[y + 1].charAt(x - 1));
            if (trie.hasPrefix(downL.toString())) {
                dir.add(new Position(x - 1, y + 1));
            }
        }
        if (x + 1 < board[0].length() && y + 1 < board.length && !marked[y + 1][x + 1]) {
            downR.append(board[y + 1].charAt(x + 1));
            if (trie.hasPrefix(downR.toString())) {
                dir.add(new Position(x + 1, y + 1));
            }
        }
        if (x + 1 < board[0].length() && !marked[y][x + 1]) {
            right.append(board[y].charAt(x + 1));
            if (trie.hasPrefix(right.toString())) {
                dir.add(new Position(x + 1, y));
            }
        }
        if (x - 1 >= 0 && !marked[y][x - 1]) {
            left.append(board[y].charAt(x - 1));
            if (trie.hasPrefix(left.toString())) {
                dir.add(new Position(x - 1, y));
            }
        }
        if (y + 1 < board.length && !marked[y + 1][x]) {
            down.append(board[y + 1].charAt(x));
            if (trie.hasPrefix(down.toString())) {
                dir.add(new Position(x, y + 1));
            }
        }
        if (y - 1 >= 0 && !marked[y - 1][x]) {
            up.append(board[y - 1].charAt(x));
            if (trie.hasPrefix(up.toString())) {
                dir.add(new Position(x, y - 1));
            }
        }
        return dir;
    }
}
