package com.pacman.entities;

import com.pacman.game.Direction;

public final class PacMan extends Entity {
    private final int startRow;
    private final int startCol;

    public PacMan(int row, int col) {
        super(row, col);
        this.startRow = row;
        this.startCol = col;
        this.direction = Direction.LEFT;
    }

    public void reset() {
        row = startRow;
        col = startCol;
        direction = Direction.NONE;
        nextDirection = Direction.NONE;
    }
}
