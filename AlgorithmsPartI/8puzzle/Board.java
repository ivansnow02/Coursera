import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Board {
    private final int[][] board;
    private final int n;
    private int blankRow = -1;
    private int blankCol = -1;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        int[][] board1;
        if (tiles == null || tiles.length == 0 || tiles.length != tiles[0].length) {
            throw new IllegalArgumentException();
        }
        n = tiles.length;
        board = copyTiles(tiles);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                    break;
                }
            }
            if (blankRow != -1) break;
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%2d ", board[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = board[i][j];
                if (tile != 0 && tile != i * n + j + 1) {
                    count++;
                }
            }
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int tile = board[i][j];
                if (tile != 0) {
                    int row = (tile - 1) / n;
                    int col = (tile - 1) % n;
                    sum += Math.abs(i - row) + Math.abs(j - col);
                }
            }
        }
        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (this == y) return true;
        if (!(y.getClass() == Board.class)) return false;
        Board that = (Board) y;
        return this.n == that.n && Arrays.deepEquals(this.board, that.board);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return new Neighbors();
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        Board b = null;
        int[][] copy = copyTiles(board);
        for (int i = 0; i < n * n - 1; i++) {
            int x = i / n;
            int y = i % n;
            int xx = (i + 1) / n;
            int yy = (i + 1) % n;
            if (board[x][y] != 0 && board[xx][yy] != 0) {
                swap(copy, x, y, xx, yy);
                b = new Board(copy);
                break;
            }
        }
        return b;
    }


    private class Neighbors implements Iterable<Board> {
        private Board[] boards;

        public Neighbors() {
            boards = new Board[4];
            int index = 0;
            int[][] dirs = {
                    { -1, 0 },
                    { 1, 0 },
                    { 0, -1 },
                    { 0, 1 }
            };
            for (int[] dir : dirs) {
                int row = blankRow + dir[0];
                int col = blankCol + dir[1];
                if (row >= 0 && row < n && col >= 0 && col < n) {
                    int[][] copy = copyTiles(board);
                    swap(copy, blankRow, blankCol, row, col);
                    boards[index++] = new Board(copy);
                }

            }

        }

        public Iterator<Board> iterator() {
            return new Iterator<Board>() {
                private int i = 0;

                public boolean hasNext() {
                    while (i < boards.length && boards[i] == null) i++;
                    return i < boards.length;
                }

                public Board next() {
                    if (!hasNext()) throw new NoSuchElementException();
                    Board b = boards[i];
                    boards[i] = null;
                    i++;
                    return b;
                }

                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }
    }

    private int[][] copyTiles(int[][] tiles) {
        int[][] copy = new int[n][n];
        for (int i = 0; i < n; i++) {
            System.arraycopy(tiles[i], 0, copy[i], 0, n);
        }
        return copy;
    }

    private void swap(int[][] array, int i, int j, int k, int m) {
        int temp = array[i][j];
        array[i][j] = array[k][m];
        array[k][m] = temp;
    }
}