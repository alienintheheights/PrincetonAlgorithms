import java.util.ArrayList;
import java.util.List;

/**
 * This is the Board class. It represents a game board, NxN.
 * Internally, this has been optimized to use a char[N^2] 
 * internally instead of a 2D int[][] array. Makes a huge 
 * difference in performance.
 * 
 * @author andrew
 **/
public class Board {
    private char[] tiles;
    private int N;
    private int NS;
    private int zeroPoint;
    private int mscore = -1;
    private int hscore = -1;
    private boolean goalCheck;
    private boolean isGoal;

    // construct a board from an n-by-n array of blocks
    public Board(int[][] blocks) {
        N = blocks.length;
        NS = N * N;
        tiles = new char[NS]; // flatten and shrink int[][] -> char[]
        copy(blocks, tiles, true); // copy to keep immutable, set ZeroPoint too
    }

    // optimization requires we need a char-based constructor internally
    private Board(char[] chars, int size) {
        N = size;
        NS = size * size;
        // construct a board from an n-by-n array of blocks
        tiles = new char[NS]; // flatten and shrink int[][] -> char[]
        copy(chars, tiles, true); // set ZeroPoint too
    }

    // board dimension n
    public int dimension() {
        return N;
    }

    // number of blocks out of place
    public int hamming() {      
        if (hscore ==  -1) {
            int misses = 0;
            for (int i = 0; i < tiles.length; i++) {
                int value = (int)tiles[i];
                int correctValue = getExpectedValue(i);
                if (value == 0 || value == correctValue) {
                    continue;
                }
                misses++;
            }
            hscore = misses;
        }
        return hscore;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (mscore ==  -1) {
            int blocks = 0;
            for (int i = 0; i < tiles.length; i++) {
                int value = tiles[i];
                if (value == 0) {
                    continue;
                }
                int correctValue = getExpectedValue(i);
                if (value != correctValue) {
                    blocks += getDisplacement(value, correctValue, i);
                }
            }
            mscore = blocks;
        } 
        return mscore;
    }

    // is this board a goal board?
    public boolean isGoal()    {
        if (goalCheck) // no point re-running this
            return isGoal;
        isGoal = true;
        for (int i=0; i<tiles.length; i++) {
            int cv = (int)tiles[i];
            if (cv == 0) continue;
            if (cv != getExpectedValue(i)) {
                isGoal = false;
                break;
            }
        }
        goalCheck = true; // mark it as checked
        return isGoal;
    }

    // all neighboring boards
    public Iterable<Board> neighbors()  { // o(N^2)
        List<Board> neighbors = new ArrayList<Board>();
        int i = zeroPoint / N;
        int j = zeroPoint % N;
        // some of these will get rejected
        addNeighbor(getNeighbor(i - 1, j), neighbors); // o(N^2) + o(N)
        addNeighbor(getNeighbor(i + 1, j), neighbors);
        addNeighbor(getNeighbor(i, j - 1), neighbors);
        addNeighbor(getNeighbor(i, j + 1), neighbors);
        return neighbors;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        if (N < 2) {
            return null;
        }
        char[] twinTiles = new char[NS];
        copy(tiles, twinTiles, false);

        // exchange: need two non-zero spots, trying 0 & 1
        if (zeroPoint == 0) { // zp is 0, use 1 and 2
            twinTiles[2] = tiles[1];
            twinTiles[1] = tiles[2];
        } else if (zeroPoint == 1) { // zp is a 1, use 0 and 2
            twinTiles[2] = tiles[0];
            twinTiles[0] = tiles[2];
        } else { // 0 & 1 are safe
            twinTiles[1] = tiles[0];
            twinTiles[0] = tiles[1]; 
        }

        return new Board(twinTiles, N);
    }

    public boolean equals(Object other)  {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }

        Board that = (Board) other;
        return (dimension() == that.dimension() 
                && toString().equals(that.toString()));
    }

    public String toString() { // o(N^2)
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < NS; i++) {
            s.append(" ");
            s.append((int)tiles[i]);
            if (i > 0 && (i + 1) % N == 0)
                s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        // unit tests (not graded)
    }

    //******** private methods ******/

    private void addNeighbor(Board neighbor, List<Board> list) {
        if (null != neighbor) {
            list.add(neighbor);
        }
    }

    private Board getNeighbor(int i, int j) { // o(N^2)
        if (i < 0 || i > N - 1 || j > N - 1 || j < 0) {
            return null;
        }
        // exchange and create
        int index = N*i + j;
        char temp = tiles[index];
        tiles[index] = 0;
        tiles[zeroPoint] = temp;

        Board neighbor = new Board(tiles, N);

        tiles[index] = temp;
        tiles[zeroPoint] = 0;
        return neighbor;
    }

    private int getExpectedValue(int index) {
        int i = index / N;
        int j = index % N;
        return  N * i + (j + 1);
    }

    // the distance in manhattan's metric from 
    // a point to its expected value
    private int getDisplacement(int value, int correctValue, int index) {
        int i = index / N;
        int j = index % N;
        int correctRow = (value - 1) / N; 
        int correctCol = value - N * correctRow - 1;
        int rowDiff = Math.abs(correctRow - i);
        int colDiff = Math.abs(correctCol - j);
        int displacement = rowDiff + colDiff;
        return displacement;
    }

    // int[][] => char[] with an option to set the zeroPoint of the board
    private void copy(int[][] source, char[] clone, boolean setZP) {
        // o(n^2) copy
        for(int i = 0; i < source.length; i++) {
            for(int j = 0; j < source[i].length; j++) {
                int index = N * i + j;
                clone[index] = (char)source[i][j];
                if (setZP && clone[index] == 0) {
                    zeroPoint = index; 
                }
            }
        }
    }
    
    // char[] => char[] with an option to set the zeroPoint of the board
    private void copy(char[] source, char[] clone, boolean setZP) {
        // o(n^2) copy
        for(int i = 0; i < source.length; i++) {
            clone[i] = source[i];
            if (setZP && clone[i] == 0) {
                zeroPoint = i; 
            }
        }
    }

}