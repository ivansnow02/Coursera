import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int nN;
    private int numOfOpenSites = 0;
    private boolean[][] grid;


    private WeightedQuickUnionUF weightedQuickUnionUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        nN = n;
        grid = new boolean[n + 1][n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                grid[i][j] = false;
            }
        }

    }


    private int t2o(int row, int col) {
        return (row - 1) * nN + col;
    }


    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > nN || col < 1 || col > nN) {
            throw new IllegalArgumentException();
        }
        numOfOpenSites++;
        if (!isOpen(row, col)) grid[row][col] = true;
        if (row == 1) {
            weightedQuickUnionUF.union(t2o(row, col), 0);
        }
        if (row == nN) {
            weightedQuickUnionUF.union(t2o(row, col), nN * nN + 1);
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {

                if (row + i < 1 || row + i > nN || col + j < 1 || col + j > nN || i == j
                        || i == -j) {
                    continue;
                }
                if (isOpen(row + i, col + j)) {
                    weightedQuickUnionUF.union(t2o(row, col), t2o(row + i, col + j));

                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > nN || col < 1 || col > nN) {
            throw new IllegalArgumentException();
        }
        return grid[row][col];
    }

    // is the site (row, col) full?

    public boolean isFull(int row, int col) {
        if (row < 1 || row > nN || col < 1 || col > nN) {
            throw new IllegalArgumentException();
        }
        return isOpen(row, col)
                && weightedQuickUnionUF.find(t2o(row, col)) == weightedQuickUnionUF.find(0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.find(nN * nN + 1) == weightedQuickUnionUF.find(0);
    }
}
