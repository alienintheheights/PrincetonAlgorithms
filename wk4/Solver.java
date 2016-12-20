
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
/**
 * This is the Solver class. 
 * It plays the game using the Board.
 * 
 * @author andrew
 */
public class Solver {
    private int moves = 0;
    private Board gameBoard;
    private SearchNode sourceNode;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (null == initial) {
            throw new java.lang.NullPointerException();
        }
        // copy initial board to avoid mutation
        gameBoard = initial;

        Board twin = gameBoard.twin();
        moves = dualScan(twin, moves);
    }

    // is the initial board solvable?
    public boolean isSolvable()  {        
        return gameBoard.isGoal();
    }
    
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves()                 {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution()     {
        if (null == sourceNode || !isSolvable()) {
            return null;
        }

        Stack<Board> solutionList = new Stack<Board>();
        SearchNode displayNode = sourceNode;
        while (null != displayNode) {
            solutionList.push(displayNode.board);
            displayNode = displayNode.previousNode;
        }
        return solutionList;
    }

    public static void main(String[] args) {
        // solve a slider puzzle (given below)

    }

    /**** private methods **/

    /**
     *  We define a search node of the game to be:
     * 	1) a board, 
     * 	2) the number of moves made to reach the board, 
     *  3) and the previous search node. 
     *  
     */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode previousNode;
        private int totalMoves;

        SearchNode(Board ib, int m, SearchNode pn) {
            board = ib;
            totalMoves = m;
            previousNode = pn;
        }

        public int getPriority() { return getScore() + totalMoves; }
        public int getScore() { return board.manhattan(); }

        public int compareTo(SearchNode compareNode) {
            int inScore = getPriority();
            int outScore = compareNode.getPriority();
            if (inScore == outScore) {
                return intCompare(getScore(), compareNode.getScore());
            } else {
                return intCompare(inScore, outScore);
            }
        }

        private int intCompare(int a, int b) {
            if (a > b) {
                return 1;
            } else if (a < b) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    // scans gameBoard against a twin board, winner determines
    // solvability.
    private int dualScan(Board inTwin, int sourceMoves) {
        Board twinBoard = inTwin; // don't mutate.
        
        // TODO use only one PQ, probably can 
        // achieve this via the SearchNode comparator
        MinPQ<SearchNode> pq1 = new MinPQ<SearchNode>();
        pq1.insert(new SearchNode(gameBoard, 0, null));
        sourceNode = pq1.delMin();

        MinPQ<SearchNode> pq2 = new MinPQ<SearchNode>();
        pq2.insert(new SearchNode(twinBoard, 0, null));
        SearchNode twinNode = pq2.delMin();

        int twinMoves = 0;
        while (!gameBoard.isGoal() && !twinBoard.isGoal()) {
            sourceMoves++;
            sourceNode = interiorScan(pq1, sourceMoves, sourceNode);
            // important! update the moves in case we move backwards.
            sourceMoves = sourceNode.totalMoves; 
            gameBoard = sourceNode.board;

            twinMoves++;
            twinNode = interiorScan(pq2, twinMoves, twinNode);
           // important! update the moves in case we move backwards.
            twinMoves = twinNode.totalMoves; 
            twinBoard = twinNode.board;
        }
        return (twinBoard.isGoal()) ? -1 : sourceMoves;
    }

    // shared method for dualScan
    private SearchNode interiorScan(
            MinPQ<SearchNode> pq,
            int moves, 
            SearchNode curNode) {
        
        for (Board item: curNode.board.neighbors()) { 
            if (!isDup(curNode.previousNode, item)) {
                pq.insert(new SearchNode(item, moves, curNode));
            }
        }
        return pq.delMin();
    }

    // recursive duplicate check
    private boolean isDup(SearchNode prevNode, Board board) {
        if (null == prevNode) {
            return false;
        }
        return (prevNode.board.equals(board));
    }


}