package com.pacman.world;

import java.util.ArrayList;
import java.util.List;

public final class Maze {
    private static final String[] TEMPLATE = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX"
    };

    private final int rows = TEMPLATE.length;
    private final int cols = TEMPLATE[0].length();
    private final boolean[][] walls = new boolean[rows][cols];
    private final boolean[][] pellets = new boolean[rows][cols];
    private final boolean[][] powerPellets = new boolean[rows][cols];
    private int remainingPellets;

    public Maze() { resetPellets(); }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public boolean isInside(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    public boolean isTunnelRow(int row) { return row == 9; }

    public int wrapColumn(int row, int col) {
        if (isTunnelRow(row) && col < 0) return cols - 1;
        if (isTunnelRow(row) && col >= cols) return 0;
        return col;
    }

    public boolean isWall(int row, int col) {
        if (!isInside(row, col)) return true;
        return walls[row][col];
    }

    public boolean isWalkable(int row, int col) {
        int wrapped = wrapColumn(row, col);
        return isInside(row, wrapped) && !walls[row][wrapped];
    }

    public boolean hasPellet(int row, int col) {
        return isInside(row, col) && pellets[row][col];
    }

    public boolean hasPowerPellet(int row, int col) {
        return isInside(row, col) && powerPellets[row][col];
    }

    public boolean eatPellet(int row, int col) {
        if (!isInside(row, col) || !pellets[row][col]) return false;
        pellets[row][col] = false;
        remainingPellets--;
        return true;
    }

    public boolean eatPowerPellet(int row, int col) {
        if (!isInside(row, col) || !powerPellets[row][col]) return false;
        powerPellets[row][col] = false;
        remainingPellets--;
        return true;
    }

    public int getRemainingPellets() { return remainingPellets; }

    public List<int[]> getPelletPositions() {
        List<int[]> result = new ArrayList<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (pellets[r][c] || powerPellets[r][c]) result.add(new int[]{r, c});
            }
        }
        return result;
    }

    public void resetPellets() {
        remainingPellets = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char tile = TEMPLATE[r].charAt(c);
                walls[r][c] = tile == 'X';
                boolean floor = tile == ' ' || tile == 'O';
                pellets[r][c] = floor;
                powerPellets[r][c] = false;
                if (floor) remainingPellets++;
            }
        }

        // Four high-value power pellets placed in reachable corners.
        int[][] powers = {{3, 1}, {3, 17}, {19, 1}, {19, 17}};
        for (int[] p : powers) {
            if (!walls[p[0]][p[1]] && pellets[p[0]][p[1]]) {
                pellets[p[0]][p[1]] = false;
                powerPellets[p[0]][p[1]] = true;
            }
        }
    }
}
