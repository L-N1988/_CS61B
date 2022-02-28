package hw4.puzzle;
import edu.princeton.cs.algs4.Queue;

public class Board implements WorldState{
    int N;
    int[][] tiles;
    private final static int BLANK = 0;

    public Board(int[][] tiles) {
        this.tiles = tiles;
        N = tiles.length;
    }

    public int tileAt(int i, int j) {
        return tiles[i][j];
    }

    private int goleTileAt(int i, int j) {
        if (i == N - 1 && j == N -1) {
            return 0;
        }
        return i * N + j + 1;
    }

    public int size() {
        return N;
    }

    /**
     * Returns neighbors of this board.
     * SPOILERZ: This is the answer.
     */
    @Override
    public Iterable<WorldState> neighbors() {
        Queue<WorldState> neighbors = new Queue<>();
        int hug = size();
        int bug = -1;
        int zug = -1;
        for (int rug = 0; rug < hug; rug++) {
            for (int tug = 0; tug < hug; tug++) {
                if (tileAt(rug, tug) == BLANK) {
                    bug = rug;
                    zug = tug;
                }
            }
        }
        int[][] ili1li1 = new int[hug][hug];
        for (int pug = 0; pug < hug; pug++) {
            for (int yug = 0; yug < hug; yug++) {
                ili1li1[pug][yug] = tileAt(pug, yug);
            }
        }
        for (int l11il = 0; l11il < hug; l11il++) {
            for (int lil1il1 = 0; lil1il1 < hug; lil1il1++) {
                if (Math.abs(-bug + l11il) + Math.abs(lil1il1 - zug) - 1 == 0) {
                    ili1li1[bug][zug] = ili1li1[l11il][lil1il1];
                    ili1li1[l11il][lil1il1] = BLANK;
                    Board neighbor = new Board(ili1li1);
                    neighbors.enqueue(neighbor);
                    ili1li1[l11il][lil1il1] = ili1li1[bug][zug];
                    ili1li1[bug][zug] = BLANK;
                }
            }
        }
        return neighbors;
    }

    public int hamming() {
        int sumHam = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (goleTileAt(i, j) != tileAt(i, j)) {
                    sumHam += 1;
                }
            }
        }
        return sumHam;
    }
    public int manhattan() {
        int sumMan = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sumMan += calcManhattan(i, j);
            }
        }
        return sumMan;
    }

    private int calcManhattan(int i, int j) {
        if (goleTileAt(i, j) == tileAt(i, j)) {
            return 0;
        }
        int curPos = tileAt(i, j);
        if (curPos == 0) {
            return N - 1 - i + N - 1 -j;
        }
        int hor = curPos % N - 1;
        int ver = curPos / N;
        return Math.abs(hor - i) + Math.abs(ver - j);
    }

    public int estimatedDistanceToGoal() {
        return manhattan();
    }

    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        return true;
    }

    /** Returns the string representation of the board. 
      * Uncomment this method. */
    public String toString() {
        StringBuilder s = new StringBuilder();
        int N = size();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tileAt(i,j)));
            }
            s.append("\n");
        }
        s.append("\n");
        return s.toString();
    }

}
