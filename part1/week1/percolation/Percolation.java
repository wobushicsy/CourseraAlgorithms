import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufWithTop;
    private boolean[][] open;
    private int length;
    private int numOfOpen;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("variable n must be a positive number");
        }
        length = n;
        numOfOpen = 0;
        uf = new WeightedQuickUnionUF(n * n + 2);
        ufWithTop = new WeightedQuickUnionUF(n * n + 1);
        for (int i = 1; i < length + 1; i += 1) {
            uf.union(0, i);
            uf.union(n * n + 1, n * (n - 1) + i);
            ufWithTop.union(0, i);
        }
        open = new boolean[n][n];
    }

    private boolean isInGrid(int row, int col) {
        return row >= 0 && row < length && col >= 0 && col < length;
    }

    private int getIndex(int row, int col) {
        return row * length + col + 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("Variable row and col must be in range(0, length)");
        }
        if (isOpen(row, col)) {
            return;
        }
        int index = getIndex(row, col);
        numOfOpen += 1;
        open[row][col] = true;
        if (isInGrid(row + 1, col) && isOpen(row + 1, col)) {
            int upIndex = getIndex(row + 1, col);
            uf.union(index, upIndex);
            ufWithTop.union(index, upIndex);
        }
        if (isInGrid(row - 1, col) && isOpen(row - 1, col)) {
            int downIndex = getIndex(row - 1, col);
            uf.union(index, downIndex);
            ufWithTop.union(index, downIndex);
        }
        if (isInGrid(row, col - 1) && isOpen(row, col - 1)) {
            int leftIndex = getIndex(row, col - 1);
            uf.union(index, leftIndex);
            ufWithTop.union(index, leftIndex);
        }
        if (isInGrid(row, col + 1) && isOpen(row, col + 1)) {
            int rightIndex = getIndex(row, col + 1);
            uf.union(index, rightIndex);
            ufWithTop.union(index, rightIndex);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("Variable row and col must be in range(0, length)");
        }
        return open[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!isInGrid(row, col)) {
            throw new IllegalArgumentException("Variable row and col must be in range(0, length)");
        }
        return isOpen(row, col) && ufWithTop.connected(getIndex(row, col), 0);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOfOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.connected(0, length * length + 1) && numOfOpen > 0;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation per = new Percolation(5);
        per.open(0, 0);
        per.open(0, 1);
        per.open(1, 3);
        per.open(2, 3);
        per.open(3, 3);
        per.open(4, 4);
        System.out.println(per.isFull(4, 4));
        per.open(3, 2);
        per.open(3, 1);
        per.open(4, 1);
//        per.open(5, 1);
        System.out.println(per.percolates());
        per.open(0, 3);
        System.out.println(per.percolates());
    }
}