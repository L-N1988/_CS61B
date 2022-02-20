package hw2;

import edu.princeton.cs.introcs.StdStats;
import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.Stopwatch;

public class PercolationStats {
    private double[] data;
    private int N;
    private int T;
    private PercolationFactory ppf;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        data = new double[T];
        this.N = N;
        this.T = T;
        ppf = pf;
        Stopwatch timer = new Stopwatch();
        expSimulation();
        System.out.println(timer.elapsedTime());
    }

    private void expSimulation() {
        for (int i = 0; i < T; i++) {
            data[i] = expSim();
        }
    }

    private double expSim() {
        Percolation perc = ppf.make(N);
        while (!perc.percolates()) {
            int row = StdRandom.uniform(N);
            int col = StdRandom.uniform(N);
            perc.open(row, col);
        }
        return (double) perc.numberOfOpenSites() / (N * N);
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(data);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(data);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return StdStats.mean(data) - 1.96 * StdStats.stddev(data) / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return StdStats.mean(data) + 1.96 * StdStats.stddev(data) / Math.sqrt(T);
    }
}
