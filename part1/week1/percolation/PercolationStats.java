import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] data;
    private double mean;
    private double stddev;
    private int t;
    private double rate;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Variable n and trials must be positive numbers");
        }

        // declare variables
        boolean[][] open;
        Percolation pf;
        rate = 1.96;
        mean = 0;
        stddev = 0;
        t = trials;
        data = new double[t];
        int row, col;
        int cnt = 0;


        // simulate percolation trials times
        for (int i = 0; i < t; i += 1) {
            cnt = 0;
            open = new boolean[n][n];
            pf = new Percolation(n);
            row = StdRandom.uniform(n) + 1;
            col = StdRandom.uniform(n) + 1;
            open[row - 1][col - 1] = true;
            while (!pf.percolates()) {
                while (open[row - 1][col - 1]) {
                    row = StdRandom.uniform(n) + 1;
                    col = StdRandom.uniform(n) + 1;
                }
                open[row - 1][col - 1] = true;
                cnt += 1;
                pf.open(row, col);
            }
            data[i] = 1.0 * cnt / (n * n);
        }

        // calculate mean
        mean = StdStats.mean(data);

        // calculate stddev
        stddev = StdStats.stddev(data);
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - rate * stddev() / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + rate * stddev() / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("expect two positive arguments");
        }
        int n = Integer.parseInt(args[0]);
        int trails = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trails);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}