package com.pacman.algorithms;

import com.pacman.game.Direction;
import com.pacman.world.Maze;
import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Grid-based BFS pathfinding.
 * Time complexity: O(V + E), where V is walkable cells and E <= 4V.
 * Space complexity: O(V) for the queue, distance and first-step grids.
 */
public final class PathFinder {
    private PathFinder() {}

    private record Node(int row, int col) {}
    private static final Direction[] DIRECTIONS = {
        Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT
    };

    public static Direction nextStep(Maze maze, int startRow, int startCol,
                                     int targetRow, int targetCol) {
        if (startRow == targetRow && startCol == targetCol) return Direction.NONE;
        int rows = maze.getRows(), cols = maze.getCols();
        int[][] firstRow = new int[rows][cols];
        int[][] firstCol = new int[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        Queue<Node> queue = new ArrayDeque<>();

        if (!maze.isWalkable(startRow, startCol) || !maze.isWalkable(targetRow, targetCol)) {
            return Direction.NONE;
        }

        queue.add(new Node(startRow, startCol));
        visited[startRow][startCol] = true;

        while (!queue.isEmpty()) {
            Node current = queue.remove();
            for (Direction direction : DIRECTIONS) {
                int nr = current.row() + direction.rowDelta();
                int nc = maze.wrapColumn(current.row(), current.col() + direction.colDelta());
                if (!maze.isWalkable(nr, nc) || visited[nr][nc]) continue;

                visited[nr][nc] = true;
                firstRow[nr][nc] = current.row() == startRow && current.col() == startCol
                        ? direction.rowDelta() : firstRow[current.row()][current.col()];
                firstCol[nr][nc] = current.row() == startRow && current.col() == startCol
                        ? direction.colDelta() : firstCol[current.row()][current.col()];

                if (nr == targetRow && nc == targetCol) {
                    return directionFromDelta(firstRow[nr][nc], firstCol[nr][nc]);
                }
                queue.add(new Node(nr, nc));
            }
        }
        return Direction.NONE;
    }

    /** Returns a direction that maximizes shortest-path distance from the target. */
    public static Direction farthestStep(Maze maze, int startRow, int startCol,
                                         int targetRow, int targetCol,
                                         Direction previousDirection) {
        int rows = maze.getRows(), cols = maze.getCols();
        int[][] distance = distancesFrom(maze, targetRow, targetCol);
        Direction best = Direction.NONE;
        int bestDistance = Integer.MIN_VALUE;

        for (Direction direction : DIRECTIONS) {
            int nr = startRow + direction.rowDelta();
            int nc = maze.wrapColumn(startRow, startCol + direction.colDelta());
            if (!maze.isWalkable(nr, nc)) continue;
            int value = distance[nr][nc];
            if (value < 0) continue;
            if (direction == previousDirection.opposite() && hasAlternative(maze, startRow, startCol)) continue;
            if (value > bestDistance) {
                bestDistance = value;
                best = direction;
            }
        }
        return best;
    }

    /**
     * Finds the walkable tile closest to a requested target.
     * Uses Manhattan distance; ties are resolved by row/column order.
     */
    public static int[] nearestWalkable(Maze maze, int targetRow, int targetCol) {
        int bestRow = -1;
        int bestCol = -1;
        int bestDistance = Integer.MAX_VALUE;

        for (int row = 0; row < maze.getRows(); row++) {
            for (int col = 0; col < maze.getCols(); col++) {
                if (!maze.isWalkable(row, col)) continue;
                int distance = Math.abs(row - targetRow) + Math.abs(col - targetCol);
                if (distance < bestDistance) {
                    bestDistance = distance;
                    bestRow = row;
                    bestCol = col;
                }
            }
        }
        return bestRow < 0 ? new int[]{targetRow, targetCol} : new int[]{bestRow, bestCol};
    }

    private static boolean hasAlternative(Maze maze, int row, int col) {
        int count = 0;
        for (Direction d : DIRECTIONS) {
            if (maze.isWalkable(row + d.rowDelta(), maze.wrapColumn(row, col + d.colDelta()))) count++;
        }
        return count > 1;
    }

    private static int[][] distancesFrom(Maze maze, int sourceRow, int sourceCol) {
        int rows = maze.getRows(), cols = maze.getCols();
        int[][] distance = new int[rows][cols];
        for (int r = 0; r < rows; r++) java.util.Arrays.fill(distance[r], -1);
        if (!maze.isWalkable(sourceRow, sourceCol)) return distance;

        Queue<Node> queue = new ArrayDeque<>();
        queue.add(new Node(sourceRow, sourceCol));
        distance[sourceRow][sourceCol] = 0;
        while (!queue.isEmpty()) {
            Node n = queue.remove();
            for (Direction d : DIRECTIONS) {
                int nr = n.row() + d.rowDelta();
                int nc = maze.wrapColumn(n.row(), n.col() + d.colDelta());
                if (!maze.isWalkable(nr, nc) || distance[nr][nc] != -1) continue;
                distance[nr][nc] = distance[n.row()][n.col()] + 1;
                queue.add(new Node(nr, nc));
            }
        }
        return distance;
    }

    private static Direction directionFromDelta(int dr, int dc) {
        for (Direction d : DIRECTIONS) {
            if (d.rowDelta() == dr && d.colDelta() == dc) return d;
        }
        return Direction.NONE;
    }
}
