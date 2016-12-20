import edu.princeton.cs.algs4.StdOut;

import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.In;

public class Test {
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        }

        Board initial = new Board(blocks);
        StdOut.println(args[0] + "\n" + initial + " d=" + initial.dimension() + " m=" + initial.manhattan() + " is goal? " + initial.isGoal());
        StdOut.println("Meet the neighbors:");
        for(Board item: initial.neighbors()) {
            StdOut.println("Here's one: " + item);
        }

        Stopwatch timer = new Stopwatch();
        Solver solver = new Solver(initial);
        double time2 = timer.elapsedTime();

        int counter=0;
        Iterable<Board> solList  = solver.solution();
        if (null != solList) {
            for(Board item: solList){
                StdOut.println("Move " + (counter++)  + " " + item);
            }
        }
        StdOut.printf("%d moves on %d items in (%.2f seconds)\n", solver.moves(), counter, time2);
        StdOut.println("Is Solvable?\n" + solver.isSolvable());

    }

}
