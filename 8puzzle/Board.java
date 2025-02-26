import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private final int[][] tiles;
    private Board twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) throw new IllegalArgumentException();
        this.tiles = this.copy(tiles);
    }

    private int[][] copy(int[][] input) {
        return Arrays.stream(input).map(int[]::clone).toArray(int[][]::new);
    }

    // string representation of this board
    public String toString() {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(this.dimension());

        for (int[] row : this.tiles) {
            strBuilder.append("\n");
            for (int tile : row) {
                strBuilder.append(" ");
                strBuilder.append(tile);
            }
        }
        String value = strBuilder.toString();

        return value;
    }


    // board dimension n
    public int dimension() {
        return this.tiles.length;
    }

    // number of tiles out of place
    public int hamming() {
        int n = this.dimension();
        int outOfPlace = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0 || this.tiles[i][j] == j + 1 + i * n) continue;
                outOfPlace++;
            }
        }

        return outOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int n = this.dimension();
        int sum = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (this.tiles[i][j] == 0 || this.tiles[i][j] == i * n + j + 1) continue;
                int row = (int) Math.ceil(this.tiles[i][j] * 1.0 / n) - 1;
                int col = this.tiles[i][j] - n * row - 1;
                sum += Math.abs(i - row) + Math.abs(j - col);
            }
        }

        return sum;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return this.hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !this.getClass().equals(y.getClass())) return false;
        if (y == this) return true;
        return Arrays.deepEquals(this.tiles, ((Board) y).tiles);
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        int n = this.dimension();
        ArrayList<Board> neighbors = new ArrayList<Board>(0);

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (this.tiles[i][j] == 0) {
                    if (j < n - 1) neighbors.add(this.move(i, j, Direction.RIGHT));
                    if (i > 0) neighbors.add(this.move(i, j, Direction.TOP));
                    if (j > 0) neighbors.add(this.move(i, j, Direction.LEFT));
                    if (i < n - 1) neighbors.add(this.move(i, j, Direction.BOTTOM));
                    return neighbors;
                }


        return neighbors;
    }

    private enum Direction {
        RIGHT, TOP, LEFT, BOTTOM
    }

    private Board move(int x, int y, Direction direction) {
        int[][] nextTiles = this.copy(this.tiles);

        if (direction == Direction.RIGHT) {
            nextTiles[x][y] = this.tiles[x][y + 1];
            nextTiles[x][y + 1] = 0;
        }
        if (direction == Direction.TOP) {
            nextTiles[x][y] = this.tiles[x - 1][y];
            nextTiles[x - 1][y] = 0;
        }
        if (direction == Direction.LEFT) {
            nextTiles[x][y] = this.tiles[x][y - 1];
            nextTiles[x][y - 1] = 0;
        }
        if (direction == Direction.BOTTOM) {
            nextTiles[x][y] = this.tiles[x + 1][y];
            nextTiles[x + 1][y] = 0;
        }

        Board nextBoard = new Board(nextTiles);

        return nextBoard;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (this.twin != null) return this.twin;

        int n = this.dimension();
        int ax = StdRandom.uniform(0, n);
        int ay = StdRandom.uniform(0, n);
        if (this.tiles[ax][ay] == 0) return this.twin();

        int bx = StdRandom.uniform(0, n);
        int by = StdRandom.uniform(0, n);
        if (this.tiles[bx][by] == 0 || this.tiles[ax][ay] == this.tiles[bx][by]) {
            return this.twin();
        }

        int[][] nextTiles = this.copy(this.tiles);
        nextTiles[ax][ay] = this.tiles[bx][by];
        nextTiles[bx][by] = this.tiles[ax][ay];
        this.twin = new Board(nextTiles);

        return this.twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // empty on purpose
    }
}
