import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.*;


public class Boggle {
    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private static class myString implements Comparable<myString> {
        String str;

        public myString(String str) {
            this.str = str;
        }

        @Override
        public int compareTo(myString o) {
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
    static Trie trie = new Trie();
    static String[] board;
    static String[][] pathBoard;
    static boolean[][] marked;
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
        MinPQ<myString> pq = new MinPQ<>();
        List<String> ret = new LinkedList<>();
        // read in all dict words
        while (!in.isEmpty()) {
            String s = in.readString();
            trie.put(s);
        }
        in = new In(boardFilePath);
        while (!in.isEmpty()) {
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
        pathBoard = new String[height][width];
        marked = new boolean[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                clearBoard();
                boardBFS(i, j, pq);
            }
        }
        // poll k max length words from pq
        for (int i = 0; i < k; i++) {
            if (pq.isEmpty()) {
                break;
            }
            ret.add(pq.delMin().str);
        }
        return ret;
    }

    private static void clearBoard() {
        for (int j = 0; j < board.length; j++) {
            for (int i = 0; i < board[0].length(); i++) {
                marked[j][i] = false;
                pathBoard[j][i] = String.valueOf(board[j].charAt(i));
            }
        }
    }

    private static void boardBFS(int x, int y, MinPQ<myString> pq) {
        // enqueue all possible words in pq
        Queue<Position> queue = new LinkedList<>();
        Position cur = new Position(x, y);
        char c;
        queue.offer(cur);
        marked[y][x] = true;
        while (!queue.isEmpty()) {
            cur = queue.poll();
            c = board[cur.y].charAt(cur.x);
            StringBuilder str = new StringBuilder(pathBoard[cur.y][cur.x]);
            if (str.length() >= 3 && trie.contains(str.toString())) {
                pq.insert(new myString(str.toString()));
            }
            for (Position next : adjacent(cur.x, cur.y, c)) {
                queue.offer(next);
                str.append(board[next.y].charAt(next.x));
                pathBoard[next.y][next.x] = str.toString();
                marked[next.y][next.x] = true;
            }
        }
    }

    // horizontal, vertical and diagonal
    private static Set<Position> adjacent(int x, int y, char c) {
        int toRight = 0;
        int toLeft = 0;
        int toUp = 0;
        int toDown = 0;
        int upRight = 0;
        int upLeft = 0;
        int downRight = 0;
        int downLeft = 0;
        Set<Position> dir = new HashSet<>();
        StringBuilder right = new StringBuilder();
        StringBuilder left = new StringBuilder();
        StringBuilder up = new StringBuilder();
        StringBuilder down = new StringBuilder();
        StringBuilder upR = new StringBuilder();
        StringBuilder upL = new StringBuilder();
        StringBuilder downR = new StringBuilder();
        StringBuilder downL = new StringBuilder();
        right.append(c);
        left.append(c);
        up.append(c);
        down.append(c);
        upR.append(c);
        upL.append(c);
        downR.append(c);
        downL.append(c);
        if (x + 1 < board[0].length() && y - 1 >= 0 && !marked[y - 1][x + 1]) {
            upR.append(board[y - 1].charAt(x + 1));
            upRight = trie.keyWithPrefix(upR.toString()).size();
            if (upRight > 0) {
                dir.add(new Position(x + 1, y - 1));
            }
        }
        if (x - 1 >= 0 && y - 1 >= 0 && !marked[y - 1][x - 1]) {
            upL.append(board[y - 1].charAt(x - 1));
            upLeft = trie.keyWithPrefix(upL.toString()).size();
            if (upLeft > 0) {
                dir.add(new Position(x - 1, y - 1));
            }
        }
        if (x - 1 >= 0 && y + 1 < board.length && !marked[y + 1][x - 1]) {
            downL.append(board[y + 1].charAt(x - 1));
            downLeft = trie.keyWithPrefix(downL.toString()).size();
            if (downLeft > 0) {
                dir.add(new Position(x - 1, y + 1));
            }
        }
        if (x + 1 < board[0].length() && y + 1 < board.length && !marked[y + 1][x + 1]) {
            downR.append(board[y + 1].charAt(x + 1));
            downRight = trie.keyWithPrefix(downR.toString()).size();
            if (downRight > 0) {
                dir.add(new Position(x + 1, y + 1));
            }
        }
        if (x + 1 < board[0].length() && !marked[y][x + 1]) {
            right.append(board[y].charAt(x + 1));
            toRight = trie.keyWithPrefix(right.toString()).size();
            if (toRight > 0) {
                dir.add(new Position(x + 1, y));
            }
        }
        if (x - 1 >= 0 && !marked[y][x - 1]) {
            left.append(board[y].charAt(x - 1));
            toLeft = trie.keyWithPrefix(left.toString()).size();
            if (toLeft > 0) {
                dir.add(new Position(x - 1, y));
            }
        }
        if (y + 1 < board.length && !marked[y + 1][x]) {
            down.append(board[y + 1].charAt(x));
            toDown = trie.keyWithPrefix(down.toString()).size();
            if (toDown > 0) {
                dir.add(new Position(x, y + 1));
            }
        }
        if (y - 1 >= 0 && !marked[y - 1][x]) {
            up.append(board[y - 1].charAt(x));
            toUp = trie.keyWithPrefix(up.toString()).size();
            if (toUp > 0) {
                dir.add(new Position(x, y - 1));
            }
        }
        return dir;
    }
}
