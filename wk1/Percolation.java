/**
 * The <tt>Percolation</tt> class models the data structure for Percolation
 * system. This class uses the <tt>WeightedQuickUnionUF</tt> class to model the
 * state of an NxN matrix. We first flatten our NxN matrix into a N^2 array such
 * that "site" (i,j) is represented by array element N*(i-1) + j. We then extend
 * this data structure to include a pair of "virtual nodes" to represent the
 * bottom and top of the percolation grid. We also add a second array of the
 * same size to track whether a given site is open or blocked. 
 * <p>
 * NOTE: API follows a 1,N element convention, not 0,N-1.
 * NOTE: The bottom row has the highest row index, N. The top row is row is 1.
 * <p>
 * Reference:
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 * @author Andrew Lienhard.
 * January 25th, 2015.
 */
public class Percolation {
    /** Data structure for tracking Union-Finds (8). **/
    private WeightedQuickUnionUF uf;

    /** Array of blocked sites (8+24+N). **/
    private boolean[] openSites;

    /** The size of the matrix (4). **/
    private int matrixSize;

    /** The number elements to track. N^2 + 2 because of virtual nodes (4). **/
    private int maxElements;

    /** for visited sites check during isFull. **/
    private boolean [] visitedSites; 

    /**
     * Constructor for the Percolation class.
     * @param dimension NxN matrix size
     */
    public Percolation(int dimension) {
        if (dimension <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive.");
        }
        matrixSize = dimension; // input value
        maxElements = dimension * dimension + 2; // N^2 + 2 virtual nodes
        uf = new WeightedQuickUnionUF(maxElements);
        initBlocked(); // init block tracking matrix
    }

    /**
     * Open a site at coordinates (i,j). Connects to all unblocked adjacent
     * sites.
     * @param i starts at 1
     * @param j starts at 1
     */
    public void open(int i, int j) {
        if (isOpen(i, j)) {
            return;
        }
        int location = xyTo1D(i, j); // get matrix location
        openSites[location] = true; // unblock ("open") site

        moveUp(location);
        moveRight(location);
        moveLeft(location);
        moveDown(location);

        if (i == matrixSize) {
            uf.union(location, maxElements - 1); // connect to virtual bottom
        } // not mutually exclusive if matrixSize = 1
        if (i == 1) { // attach to virtual nodes
            uf.union(location, 0); // connect to virtual top (top => first row)
        }
    }

    /**
     * Tests to see if a site is open.
     * @param i row number (1,N)
     * @param j column number (1,N)
     * @return boolean
     */
    public boolean isOpen(int i, int j) {
        validateXY(i, j);
        // is site (row i, column j) open?
        return openSites[xyTo1D(i, j)];
    }

    /**
     * Check if there's a full site on the bottom row. If so, we can say that
     * the system percolates.
     * @return boolean
     */
    public boolean percolates() {
        return uf.connected(0, maxElements - 1);
    }

    /**
     * Tests for a full site, which is an open site that can be connected to an
     * open site in the top row via a chain of neighboring (left, right, up,
     * down) open sites.
     * @param i row number (1,N)
     * @param j column number (1,N)
     * @return boolean
     */
    public boolean isFull(int i, int j) {
        validateXY(i, j);
        visitedSites = new boolean[maxElements];
        return walk(xyTo1D(i, j));
    }

    /**
     * Perform a recursive walk to examine open sites
     * starting from the bottom row isFull() location.
     * @param location
     */
    private boolean walk(int location) {
        if (!openSites[location] || visitedSites[location]) {
            return false;
        } 
        if (location <= matrixSize) {
            return true; // made it to the top!
        }
        visitedSites[location] = true; // track
         
        if (validUp(location) && walk(location - matrixSize)) {
            return true;
        }
        if (validRight(location) && walk(location + 1)) {
            return true;
        }
        if (validLeft(location) && walk(location - 1)) {
            return true;
        }
        if (validDown(location) && walk(location + matrixSize)) {
            return true;
        }
        return false;
    }

    /**
     * Connect adjacent node if open.
     * @param location current location in matrix
     * @param adjacent location of adjacent site in matrix
     */
    private void connect(int location, int adjacent) {
        if (!openSites[adjacent]) {
            return;
        }
        uf.union(location, adjacent);
    }


    private boolean validRight(int location) {
        return (location % matrixSize != 0);
    }

    /** connects site to the right.
     * @param location
     * @return true if connection is made
     */
    private void moveRight(int location) {
        if (!validRight(location)) {
            return;
        }
        connect(location, location + 1);
    }

    private boolean validLeft(int location) {
        return ((location - 1) % matrixSize != 0);
    }

    /** connects site to the left.
     * @param location
     * @return true if connection is made
     */
    private void moveLeft(int location) {
        if (!validLeft(location)) {
            return;
        }
        connect(location, location - 1);
    }

    private boolean validDown(int location) {
        return (location <= matrixSize*(matrixSize -1));
    }

    /** connects site down a row.
     * @param location
     * @return true if connection is made
     */
    private void moveDown(int location) {
        if (!validDown(location)) {
            return;
        }
        connect(location, location + matrixSize);
    }

    private boolean validUp(int location) {
        return (location > matrixSize);
    }

    /** connects site up a row.
     * @param location
     * @return true if connection is made
     */
    private void moveUp(int location) {
        if (!validUp(location)) {
            return;
        }
        connect(location, location - matrixSize);
    }


    /**
     * Initialize the array that tracks which sites are open.
     */
    private void initBlocked() {
        openSites = new boolean[maxElements]; // init blocked matrix
        openSites[0] = true; // virtual top node
        openSites[maxElements - 1] = true; // virtual top node
    }

    /**
     * Converts (i,j) to a 1-D matrix index.
     * @param i the row value (1,N)
     * @param j the column value (1,N)
     * @return the matrix index
     */
    private int xyTo1D(int i, int j) {
        return (i - 1) * matrixSize + j;
    }

    /**
     * Validates a coordinate, (i,j).
     * @param i the row value (1,N)
     * @param j the column value (1,N)
     */
    private void validateXY(int i, int j) {
        if ((i <= 0 || i > matrixSize) || (j <= 0 || j > matrixSize)) {
            throw new IndexOutOfBoundsException("Illegal matrix value.");
        }
    }

    /**
     * Test client.
     * @param args command line arguments
     */
    public static void main(String[] args) {

        // Trivial Percolation Test
        Percolation p = new Percolation(2);
        p.open(1, 1);
        p.open(1, 2);
        StdOut.println(p.uf.connected(1, 2)); // true

        // isFull Test
        p = new Percolation(4);
        p.open(1, 3);
        p.open(2, 3);
        p.open(3, 3);
        p.open(4, 3);
        p.open(2, 1);
        p.open(4, 1);
        p.open(1, 1);
        StdOut.println("Is 2,1 -> 4,3 TRUE?" + p.uf.connected(1, 15)); // true
        StdOut.println("Is 1,1 full TRUE?" + p.isFull(1, 1));
        StdOut.println("Is 1,3 full TRUE?" + p.isFull(1, 3));
        StdOut.println("Is 1,3 -> 4,3 TRUE?" + p.uf.connected(3, 15));
        p.open(1, 1);
        StdOut.println("Is 2,1 -> 4,3 TRUE?" + p.uf.connected(1, 15));
        StdOut.println("Is 4,1 full FALSE?" + p.isFull(4, 1));

        // Trivial Percolation Test
        p = new Percolation(2);
        p.open(1, 1);
        p.open(1, 2);
        StdOut.println(p.uf.connected(1, 2)); // true

        // isFull Test
        p =  new Percolation(4);
        p.open(4, 4);
        p.open(3, 4);
        p.open(2, 4);
        p.open(1, 4);
        p.open(3, 2);
        p.open(4, 2);
        p.open(4, 3);
        StdOut.println("2nd TEST: Is 4,3 full TRUE?" + p.isFull(4, 3));

        // Boundary Conditions Test

        try {
            new Percolation(-1);
            StdOut.println("TEST FAILURE: allows illegal constructor value.");
        } catch (IllegalArgumentException e) {
            StdOut.println("Caught illegal input of N=-1: " + e);
        }
        try {
            Percolation testP = new Percolation(10);
            testP.open(-1, 5);
            StdOut.println("TEST FAILURE: allows illegal open value (-1,5)");
        } catch (IndexOutOfBoundsException e) {
            StdOut.println("Caught illegal input on open (-1, 5): " + e);
        }
        try {
            Percolation testP = new Percolation(10);
            testP.open(0, 5);
            StdOut.println("TEST FAILURE: allows illegal open value (0,5)!");
        } catch (IndexOutOfBoundsException e) {
            StdOut.println("Caught illegal input on open (0, 5): " + e);
        }
        try {
            Percolation testP = new Percolation(10);
            testP.open(11, 5);
            StdOut.println("TEST FAILURE: allows illegal open value (11,5)!");
        } catch (IndexOutOfBoundsException e) {
            StdOut.println("Caught illegal input on open (11, 5): " + e);
        }
        try {
            Percolation testP = new Percolation(10);
            testP.open(5, -1);
            StdOut.println("TEST FAILURE: allows illegal open value (5, -1)");
        } catch (IndexOutOfBoundsException e) {
            StdOut.println("Caught illegal input on open (5, -1): " + e);
        }
        try {
            Percolation testP = new Percolation(10);
            testP.open(5, 0);
            StdOut.println("TEST FAILURE: allows illegal open value (5, 0)!");
        } catch (IndexOutOfBoundsException e) {
            StdOut.println("Caught illegal input on open (5, 0): " + e);
        }
        try {
            Percolation testP = new Percolation(10);
            testP.open(5, 11);
            StdOut.println("TEST FAILURE: allows illegal open value (5, 11)!");
        } catch (IndexOutOfBoundsException e) {
            StdOut.println("Caught illegal input on open (5, 11): " + e);
        }

    }
}
