import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE = 1.96;
    private final double[] xs;
    private final double xMean;
    private final double std;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("illegal parameter");
        }
        xs = new double[trials];
        experiment(n, trials);
        xMean = StdStats.mean(xs);
        std = StdStats.stddev(xs);
    }

    private void experiment(int n, int trials) {
        boolean[] lineIsEmpty;
        int lines;

        for (int i = 0; i < trials; i++) {
            lineIsEmpty = new boolean[n];
            lines = 0;
            Percolation percolation = new Percolation(n);
            while (true) {
                int xi = StdRandom.uniformInt(n) + 1;
                int yi = StdRandom.uniformInt(n) + 1;
                if (!percolation.isOpen(xi, yi)) {
                    percolation.open(xi, yi);
                    xs[i]++;
                    if (!lineIsEmpty[xi - 1]) {
                        lineIsEmpty[xi - 1] = true;
                        lines++;
                    }
                    if (lines == n) {
                        if (percolation.percolates()) {
                            break;
                        }
                    }
                }
            }
            xs[i] /= n * n;
        }
    }


    // sample mean of percolation threshold
    public double mean() {
        return xMean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return std;
    }

    // // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return xMean - (CONFIDENCE * std / Math.sqrt(xs.length));
    }

    // // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return xMean + (CONFIDENCE * std / Math.sqrt(xs.length));
    }

    // test client (see below)
    public static void main(String[] args) {
        // int n = 200;
        // int trials = 100;
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percStats = new PercolationStats(n, trials);
        System.out.printf("mean                     = %f\n", percStats.mean());
        System.out.printf("stddev                   = %f\n", percStats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]\n", percStats.confidenceLo(),
                      percStats.confidenceHi());

    }
}
