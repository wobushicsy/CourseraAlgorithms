import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private double[] data;
    private double mean;
    private double stddev;
    private int T;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Variable n and trials must be positive numbers");
        }

        // declare variables
        Percolation pf;
        mean = 0;
        stddev = 0;
        T = trials;
        data = new double[T];

        // simulate percolation trials times
        for (int i = 0; i < T; i += 1) {
            pf = new Percolation(n);
            int cnt = 0;
            while (!pf.percolates()) {
                int row = StdRandom.uniform(0, n);
                int col = StdRandom.uniform(0, n);
                while (pf.isOpen(row, col)) {
                    row = StdRandom.uniform(0, n);
                    col = StdRandom.uniform(0, n);
                }
                cnt += 1;
                pf.open(row, col);
            }
            data[i] = 1.0 * cnt / (n * n);
            mean += data[i];
        }

        // calculate mean
        mean /= T;

        // calculate stddev
        for (int i = 0; i < T; i += 1) {
            stddev += Math.pow((data[i] - mean), 2);
        }
        stddev /= T - 1;
        stddev = Math.sqrt(stddev);
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
        return mean - 1.96 * stddev / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + 1.96 * stddev / Math.sqrt(T);
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("expect two positive arguments");
        }
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, T);
        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}