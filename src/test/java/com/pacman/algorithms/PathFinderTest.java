package com.pacman.algorithms;

import com.pacman.game.Direction;
import com.pacman.world.Maze;

/**
 * Dependency-free smoke tests. Run with:
 * java -cp out com.pacman.algorithms.PathFinderTest
 */
public final class PathFinderTest {
    public static void main(String[] args) {
        Maze maze = new Maze();
        Direction step = PathFinder.nextStep(maze, 15, 9, 19, 9);
        if (step == Direction.NONE) throw new AssertionError("BFS did not find a route.");
        if (!maze.isWalkable(15 + step.rowDelta(),
                maze.wrapColumn(15, 9 + step.colDelta()))) {
            throw new AssertionError("BFS returned a blocked first step.");
        }
        System.out.println("PathFinder smoke tests passed.");
    }
}
