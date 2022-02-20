package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] map;
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufHelper;
    private final int size;
    private int numSite;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        map = new boolean[N][N];
        uf = new WeightedQuickUnionUF(N * N + 2);
        ufHelper = new WeightedQuickUnionUF(N * N + 1);
        size = N;
        numSite = 0;
        // union virtual sites
        for (int i = 0; i < size; i++) {
            uf.union(i, N * N);
            uf.union((N - 1) * N + i, N * N + 1);
            ufHelper.union(i, N * N);
        }
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0 || row >= size || col >= size) {
            // throw new java.lang.IllegalArgumentException();
            return;
        }
        if (!isOpen(row, col)) {
            map[row][col] = true;
            numSite += 1;
            // union around sites.
            if (isOpen(row - 1, col)) {
                uf.union(row * size + col, (row - 1) * size + col);
                ufHelper.union(row * size + col, (row - 1) * size + col);
            }
            if (isOpen(row + 1, col)) {
                uf.union(row * size + col, (row + 1) * size + col);
                ufHelper.union(row * size + col, (row + 1) * size + col);
            }
            if (isOpen(row, col - 1)) {
                uf.union(row * size + col, row * size + col - 1);
                ufHelper.union(row * size + col, row * size + col - 1);
            }
            if (isOpen(row, col + 1)) {
                uf.union(row * size + col, row * size + col + 1);
                ufHelper.union(row * size + col, row * size + col + 1);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 0 || col < 0) {
            // throw new java.lang.IllegalArgumentException();
            return false;
        } else if (row >= size || col >= size) {
            // throw new java.lang.IndexOutOfBoundsException();
            return false;
        }
        return map[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        // invalid index
        if (row < 0 || col < 0 || row >= size || col >= size) {
            return false;
        }
        if (isOpen(row, col)) {
            return uf.connected(row * size + col, size * size)
                    && ufHelper.connected(row * size + col, size * size);
        } else {
            return false;
        }
    }

    // number of open sites
    public int numberOfOpenSites() {
        return numSite;
    }

    // does the system percolated?
    public boolean percolates() {
        return uf.connected(size * size, size * size + 1);
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
        int N = 35;
        int T = 900;
        PercolationStats stat = new PercolationStats(N, T, new PercolationFactory());
        System.out.println("mean = " + stat.mean());
        System.out.println("stddev = " + stat.stddev());
        System.out.println("(" + stat.confidenceLow() + ", " + stat.confidenceHigh() + ")");
    }

}
