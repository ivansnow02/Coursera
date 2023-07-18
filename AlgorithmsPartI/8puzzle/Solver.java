import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;

public class Solver {
    private static class GameTreeNode implements Comparable<GameTreeNode> {
        private final Board board;
        private final GameTreeNode parent;
        private final boolean twin;
        private final int moves;
        private final int distance;
        private final int priority;

        public GameTreeNode(Board board, boolean twin) {
            this.board = board;
            parent = null;
            this.twin = twin;
            moves = 0;
            distance = board.manhattan();
            priority = distance + moves;
        }

        public GameTreeNode(Board board, GameTreeNode parent) {
            this.board = board;
            this.parent = parent;
            twin = parent.twin;
            moves = parent.moves + 1;
            distance = board.manhattan();
            priority = distance + moves;
        }

        public Board getBoard() {
            return board;
        }

        public GameTreeNode getParent() {
            return parent;
        }

        public boolean isTwin() {
            return twin;
        }

        @Override
        public int compareTo(GameTreeNode node) {
            if (priority == node.priority) {
                return Integer.compare(distance, node.distance);
            }
            else {
                return Integer.compare(priority, node.priority);
            }
        }
    }

    private int moves;
    private boolean solvable;
    private Iterable<Board> solution;
    private final Board initial;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;
        cache();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return this.solution;
    }

    private void cache() {
        MinPQ<GameTreeNode> pq = new MinPQ<>();
        pq.insert(new GameTreeNode(initial, false));
        pq.insert(new GameTreeNode(initial.twin(), true));
        GameTreeNode node = pq.delMin();
        Board b = node.getBoard();
        while (!b.isGoal()) {
            for (Board neighbor : b.neighbors()) {
                if (node.getParent() == null || !neighbor.equals(node.getParent().getBoard())) {
                    pq.insert(new GameTreeNode(neighbor, node));
                }
            }
            node = pq.delMin();
            b = node.getBoard();
        }
        solvable = !node.isTwin();
        if (!solvable) {
            moves = -1;
            solution = null;
        }
        else {
            ArrayList<Board> list = new ArrayList<>();
            while (node != null) {
                list.add(node.getBoard());
                node = node.getParent();
            }
            moves = list.size() - 1;
            Collections.reverse(list);
            solution = list;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
