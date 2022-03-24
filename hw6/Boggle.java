import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Boggle {

    private static class Position {
        int x;
        int y;

        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    // File path of dictionary file
    static String dictPath = "words.txt";
    static Trie trie = new Trie();
    static String[] board;
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
        In in = new In(dictPath);
        int height = 0, width = 0;
        StringBuilder str = new StringBuilder();
        MinPQ<String> pq = new MinPQ<>();
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
        marked = new boolean[height][width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                boardDFS(i, j, str, pq);
            }
        }
        return null;
    }

    private static void boardDFS(int x, int y, StringBuilder str, MinPQ<String> pq) {
        // search around 3 layer, calculate all words number with specific prefix
        // dfs search at the largest number of words' tile
        char c = board[y].charAt(x);
        str.append(c);
        marked[y][x] = true;
        Position next = mostWordsDirection(x, y, c);
        if (next == null) {
            return;
        }
        boardDFS(next.x, next.y, str, pq);
        marked[y][x] = false;
        if (trie.contains(str.toString())) {
            pq.insert(str.toString());
        }
        str.deleteCharAt(str.length() - 1);
    }

    private static Position mostWordsDirection(int x, int y, char c) {
        int toRight = 0;
        int toLeft = 0;
        int toUp = 0;
        int toDown = 0;
        Map<Integer, Position> dir = new HashMap<>();
        StringBuilder right = new StringBuilder();
        StringBuilder left = new StringBuilder();
        StringBuilder up = new StringBuilder();
        StringBuilder down = new StringBuilder();
        Position next = null;
        int maxDir = 0;
        right.append(c);
        left.append(c);
        up.append(c);
        down.append(c);
        if (x + 1 < board[0].length() && !marked[y][x + 1]) {
            right.append(board[y].charAt(x + 1));
            toRight = trie.keyWithPrefix(right.toString()).size();
            dir.put(toRight, new Position(x + 1, y));
        }
        if (x - 1 >= 0 && !marked[y][x - 1]) {
            left.append(board[y].charAt(x - 1));
            toLeft = trie.keyWithPrefix(left.toString()).size();
            dir.put(toLeft, new Position(x - 1, y));
        }
        if (y + 1 < board.length && !marked[y + 1][x]) {
            down.append(board[y + 1].charAt(x));
            toDown = trie.keyWithPrefix(down.toString()).size();
            dir.put(toDown, new Position(x, y + 1));
        }
        if (y - 1 >= 0 && !marked[y - 1][x]) {
            up.append(board[y - 1].charAt(x));
            toUp = trie.keyWithPrefix(up.toString()).size();
            dir.put(toUp, new Position(x, y - 1));
        }
        for (Map.Entry<Integer, Position> d : dir.entrySet()) {
            if (maxDir < d.getKey()) {
                maxDir = d.getKey();
                next = d.getValue();
            }
        }
        return next;
    }
}
