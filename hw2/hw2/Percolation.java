package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] map;
    private WeightedQuickUnionUF ufBlock;
    private WeightedQuickUnionUF ufWater;
    private final int size;
    private int numSite;

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        map = new boolean[N][N];
        ufBlock = new WeightedQuickUnionUF(N * N);
        ufWater = new WeightedQuickUnionUF(N * N);
        size = N;
        numSite = 0;
    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 0 || col < 0) {
            // throw new java.lang.IllegalArgumentException();
            return;
        } else if (row >= size || col >= size) {
            // throw new java.lang.IndexOutOfBoundsException();
            return;
        }
        if (!isOpen(row, col)) {
            map[row][col] = true;
            numSite += 1;
            // union around sites.
            if (isOpen(row - 1, col)) {
                ufBlock.union(row * size + col, (row - 1) * size + col);
            }
            if (isOpen(row + 1, col)) {
                ufBlock.union(row * size + col, (row + 1) * size + col);
            }
            if (isOpen(row, col - 1)) {
                ufBlock.union(row * size + col, row * size + col - 1);
            }
            if (isOpen(row, col + 1)) {
                ufBlock.union(row * size + col, row * size + col + 1);
            }
            // the site is not water
            // search around water
            if (isFull(row - 1, col)) {
                ufWater.union(row * size + col, (row - 1) * size + col);
            }
            if (isFull(row + 1, col)) {
                ufWater.union(row * size + col, (row + 1) * size + col);
            }
            if (isFull(row, col - 1)) {
                ufWater.union(row * size + col, row * size + col - 1);
            }
            if (isFull(row, col + 1)) {
                ufWater.union(row * size + col, row * size + col + 1);
            }
            // the site is water
            // percolate around sites
            if (isFull(row, col)) {
                if (isOpen(row - 1, col)) {
                    ufWater.union(row * size + col, (row - 1) * size + col);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (ufBlock.connected(i * size + j, (row - 1) * size + col)) {
                                ufWater.union(row * size + col, i * size + j);
                            }
                        }
                    }
                }
                if (isOpen(row + 1, col)) {
                    ufWater.union(row * size + col, (row + 1) * size + col);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (ufBlock.connected(i * size + j, (row + 1) * size + col)) {
                                ufWater.union(row * size + col, i * size + j);
                            }
                        }
                    }
                }
                if (isOpen(row, col - 1)) {
                    ufWater.union(row * size + col, row * size + col - 1);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (ufBlock.connected(i * size + j, row * size + col - 1)) {
                                ufWater.union(row * size + col, i * size + j);
                            }
                        }
                    }
                }
                if (isOpen(row, col + 1)) {
                    ufWater.union(row * size + col, row * size + col + 1);
                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (ufBlock.connected(i * size + j, row * size + col + 1)) {
                                ufWater.union(row * size + col, i * size + j);
                            }
                        }
                    }
                }
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
            if (row == 0) {
                return true;
            } else {
                // explore around corners
                return ((row - 1) * size + col < size * size
                        && ufWater.connected(row * size + col, (row - 1) * size + col))
                        || ((row + 1) * size +col < size * size
                        && ufWater.connected(row * size + col, (row + 1) * size + col))
                        || (row * size + col - 1 < size * size
                        && ufWater.connected(row * size + col, row * size + col - 1))
                        || (row * size + col + 1 < size * size
                        && ufWater.connected(row * size + col, row * size + col + 1));
            }
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
        for (int i = 0; i < size; i++) {
            if (isFull(size - 1, i)) {
                return true;
            }
        }
        return  false;
    }

    // use for unit testing (not required)
    public static void main(String[] args) {
        int N = 35;
        int T = 30;
        PercolationStats stat = new PercolationStats(N, T, new PercolationFactory());
        System.out.println("mean = " + stat.mean());
        System.out.println("stddev = " + stat.stddev());
        System.out.println("(" + stat.confidenceLow() + ", " + stat.confidenceHigh() + ")");
    }

}
