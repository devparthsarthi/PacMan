package com.pacman.entities;

import com.pacman.game.Direction;

public abstract class Entity {
    protected int row;
    protected int col;
    protected Direction direction = Direction.NONE;
    protected Direction nextDirection = Direction.NONE;

    protected Entity(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
    public Direction getDirection() { return direction; }
    public Direction getNextDirection() { return nextDirection; }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public void setNextDirection(Direction direction) {
        this.nextDirection = direction;
    }
}
