/**
 * The <tt>PercolationStats</tt> class models <tt>Percolation</tt> test runs.
 * The input parameters are the matrix size, N, and the number of samples to
 * execute, T.
 * <p>
 * To run: java PercolationStats <matrix size> <number of trials>
 * <p>
 * Reference:
 * href="http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 *
 * @author Andrew Lienhard.
 * January 25th, 2015.
 */
public class PercolationStats {
    /** constant for stats. **/
    private static final double VARIANCE_FACTOR = 1.96;

    /** The number of trials to perform. **/
    private int runs;

    /** The cumulative average of opens/size over all runs.**/
    private double[] scores;

    /**
     * Constructor. Kicks off test run (NxN, T times).
     * @param dimensions the matrix size.
     * @param num the number of trials to run.
     */
    public PercolationStats(int dimensions, int num) {
        if (dimensions <= 0 || num <= 0) {
            throw new IllegalArgumentException("Please use positive integers.");
        }
        runs = num;
        scores = new double[runs];
        // run trials
        for (int k = 0; k < runs; k++) {
            int openSiteCount = runTest(dimensions);
            scores[k] = openSiteCount / (double) (dimensions * dimensions);
        }
    }

    /**
     * The mean value for the trials.
     * @return mean value.
     */
    public double mean() {
        return StdStats.mean(scores);
    }

    /**
     * Standard-Deviation value.
     * @return standard deviation value.
     */
    public double stddev() {
        if (runs == 1) {
           return Double.NaN;
        } else {
           return StdStats.stddev(scores);
        }
    }

    /**
     * Top-end of a 95% confidence interval.
     * @return confidence interval upper bound
     */
    public double confidenceLo() {
        return mean() - (VARIANCE_FACTOR * stddev()) / Math.sqrt(runs);
    }

    /**
     * Top-end of a 95% confidence interval.
     * @return confidence interval upper bound
     */
    public double confidenceHi() {
        return mean() + (VARIANCE_FACTOR * stddev()) / Math.sqrt(runs);
    }

    /**
     * Executes a trial. Perform T independent experiments on an N-by-N grid.
     */
    private int runTest(int matrixSize) {
        Percolation percolation = new Percolation(matrixSize);
        int maxSize = matrixSize * matrixSize;
        int openSiteCount = 0;
        while (true) {
            if (percolation.percolates()) {
                break;
            }
            int ranNum = StdRandom.uniform(1, maxSize + 1);
            int row = 1 + (int) ((ranNum - 1) / (double) matrixSize);
            int col = ranNum - (row - 1) * (matrixSize);
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col); // TODO devise better check
                openSiteCount++;
            }
        }
        return openSiteCount;
    }

    /**
     * main for running tests.
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException(
                "Enter two positive integer values: N and T");
        }

        int matrixSize = 0, numberOfRuns = 0;
        try {
            matrixSize = Integer.parseInt(args[0]);
            numberOfRuns = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Arguments must be an integer.");
           return;
        }

        // Execute Trials
        PercolationStats stats = new PercolationStats(matrixSize, numberOfRuns);
        StdOut.println("mean \t\t\t= " + stats.mean());
        StdOut.println("stddev \t\t\t= " + stats.stddev());
        StdOut.println("95% confidence interval = " + stats.confidenceLo()
                + ", " + stats.confidenceHi());
    }

}
