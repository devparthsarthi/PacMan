package com.pacman.game;

import com.pacman.algorithms.PathFinder;
import com.pacman.config.GameConfig;
import com.pacman.entities.Ghost;
import com.pacman.entities.GhostPersonality;
import com.pacman.entities.PacMan;
import com.pacman.world.Maze;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public final class GameEngine {
    private final Maze maze = new Maze();
    private final PacMan pacMan = new PacMan(15, 9);
    private final List<Ghost> ghosts = new ArrayList<>();
    private GameState state = GameState.MENU;
    private int score;
    private int lives = GameConfig.INITIAL_LIVES;
    private int level = 1;
    private int powerTicks;
    private int ghostMoveCounter;

    public GameEngine() {
        createGhosts();
    }

    private void createGhosts() {
        ghosts.clear();
        ghosts.add(new Ghost(8, 9, "redGhost.png", Color.RED,
                GhostPersonality.RED_CHASER));
        ghosts.add(new Ghost(9, 8, "blueGhost.png", Color.CYAN,
                GhostPersonality.BLUE_TRICKSTER));
        ghosts.add(new Ghost(9, 9, "pinkGhost.png", Color.PINK,
                GhostPersonality.PINK_AMBUSHER));
        ghosts.add(new Ghost(9, 10, "orangeGhost.png", Color.ORANGE,
                GhostPersonality.ORANGE_PATROLLER));
    }

    public void startNewGame() {
        score = 0;
        lives = GameConfig.INITIAL_LIVES;
        level = 1;
        powerTicks = 0;
        maze.resetPellets();
        resetActors();
        state = GameState.PLAYING;
    }

    public void togglePause() {
        if (state == GameState.PLAYING) state = GameState.PAUSED;
        else if (state == GameState.PAUSED) state = GameState.PLAYING;
    }

    public void restartAfterEnd() {
        if (state == GameState.GAME_OVER || state == GameState.VICTORY) startNewGame();
    }

    public void setPlayerDirection(Direction direction) {
        pacMan.setNextDirection(direction);
    }

    public void tick() {
        if (state != GameState.PLAYING) return;

        movePlayer();
        collectItems();
        moveGhosts();
        checkCollisions();

        if (powerTicks > 0) powerTicks--;
        if (maze.getRemainingPellets() == 0 && state == GameState.PLAYING) {
            if (level >= 5) state = GameState.VICTORY;
            else {
                level++;
                maze.resetPellets();
                resetActors();
                state = GameState.LEVEL_COMPLETE;
            }
        }
    }

    public void continueAfterLevel() {
        if (state == GameState.LEVEL_COMPLETE) state = GameState.PLAYING;
    }

    private void movePlayer() {
        Direction desired = pacMan.getNextDirection();
        if (desired != Direction.NONE && canMove(pacMan.getRow(), pacMan.getCol(), desired)) {
            pacMan.setDirection(desired);
        }
        Direction current = pacMan.getDirection();
        if (current != Direction.NONE && canMove(pacMan.getRow(), pacMan.getCol(), current)) {
            moveOneTile(pacMan, current);
        } else if (current != Direction.NONE) {
            pacMan.setDirection(Direction.NONE);
        }
    }

    private void collectItems() {
        int r = pacMan.getRow(), c = pacMan.getCol();
        if (maze.eatPellet(r, c)) score += GameConfig.PELLET_SCORE;
        if (maze.eatPowerPellet(r, c)) {
            score += GameConfig.POWER_PELLET_SCORE;
            powerTicks = GameConfig.POWER_DURATION_TICKS;
        }
    }

    private void moveGhosts() {
        ghostMoveCounter++;

        // Slower ghost movement. A larger interval means fewer ghost moves.
        // Level 1-2: every 7 ticks; Level 3-4: every 6 ticks; Level 5: every 5 ticks.
        int moveInterval = Math.max(5, 7 - ((level - 1) / 2));
        if (ghostMoveCounter % moveInterval != 0) return;

        Ghost redGhost = ghosts.get(0);
        int[] pacmanAhead = getPacManTarget(4);

        for (Ghost ghost : ghosts) {
            if (ghost.isRespawning()) {
                ghost.tickRespawn();
                continue;
            }

            Direction next;
            if (powerTicks > 0) {
                // Every ghost retreats while Pac-Man has a power pellet active.
                next = PathFinder.farthestStep(
                        maze, ghost.getRow(), ghost.getCol(),
                        pacMan.getRow(), pacMan.getCol(), ghost.getDirection());
            } else {
                next = chooseDirectionForGhost(ghost, redGhost, pacmanAhead);
            }

            if (next == Direction.NONE ||
                    !canMove(ghost.getRow(), ghost.getCol(), next)) {
                next = chooseFallbackDirection(ghost);
            }

            if (next != Direction.NONE) {
                ghost.setDirection(next);
                moveOneTile(ghost, next);
            }
        }
    }

    private Direction chooseDirectionForGhost(
            Ghost ghost, Ghost redGhost, int[] pacmanAhead) {

        int targetRow;
        int targetCol;

        switch (ghost.getPersonality()) {
            case RED_CHASER:
                // Red: aggressive direct pursuit.
                targetRow = pacMan.getRow();
                targetCol = pacMan.getCol();
                break;

            case PINK_AMBUSHER:
                // Pink: tries to intercept Pac-Man four tiles ahead.
                targetRow = pacmanAhead[0];
                targetCol = pacmanAhead[1];
                break;

            case BLUE_TRICKSTER:
                // Blue: aims using a vector from Red's position through
                // a point two tiles ahead of Pac-Man.
                int[] bluePivot = getPacManTarget(2);
                targetRow = 2 * bluePivot[0] - redGhost.getRow();
                targetCol = 2 * bluePivot[1] - redGhost.getCol();
                break;

            case ORANGE_PATROLLER:
                // Orange: pursues at a distance, but backs away when close.
                int distance = Math.abs(ghost.getRow() - pacMan.getRow())
                        + Math.abs(ghost.getCol() - pacMan.getCol());
                if (distance <= 6) {
                    return PathFinder.farthestStep(
                            maze, ghost.getRow(), ghost.getCol(),
                            pacMan.getRow(), pacMan.getCol(), ghost.getDirection());
                }
                targetRow = pacMan.getRow();
                targetCol = pacMan.getCol();
                break;

            default:
                targetRow = pacMan.getRow();
                targetCol = pacMan.getCol();
                break;
        }

        int[] validTarget = PathFinder.nearestWalkable(maze, targetRow, targetCol);
        return PathFinder.nextStep(
                maze, ghost.getRow(), ghost.getCol(),
                validTarget[0], validTarget[1]);
    }

    /**
     * Calculates a tile a number of steps ahead of Pac-Man.
     * If Pac-Man is stationary, his current tile is used.
     */
    private int[] getPacManTarget(int tilesAhead) {
        int targetRow = pacMan.getRow();
        int targetCol = pacMan.getCol();
        Direction direction = pacMan.getDirection();

        if (direction == Direction.NONE) {
            return new int[]{targetRow, targetCol};
        }

        for (int i = 0; i < tilesAhead; i++) {
            int nextRow = targetRow + direction.rowDelta();
            int nextCol = maze.wrapColumn(targetRow,
                    targetCol + direction.colDelta());

            if (!maze.isWalkable(nextRow, nextCol)) break;

            targetRow = nextRow;
            targetCol = nextCol;
        }
        return new int[]{targetRow, targetCol};
    }

    private Direction chooseFallbackDirection(Ghost ghost) {
        Direction[] options = {Direction.UP, Direction.LEFT, Direction.DOWN, Direction.RIGHT};
        for (Direction d : options) {
            if (d != ghost.getDirection().opposite() && canMove(ghost.getRow(), ghost.getCol(), d)) return d;
        }
        for (Direction d : options) if (canMove(ghost.getRow(), ghost.getCol(), d)) return d;
        return Direction.NONE;
    }

    private void checkCollisions() {
        for (Ghost ghost : ghosts) {
            if (ghost.isRespawning()) continue;
            if (ghost.getRow() == pacMan.getRow() && ghost.getCol() == pacMan.getCol()) {
                if (powerTicks > 0) {
                    score += GameConfig.GHOST_SCORE;
                    ghost.setPosition(9, 9);
                    ghost.setRespawnTicks(10);
                } else {
                    lives--;
                    if (lives <= 0) {
                        state = GameState.GAME_OVER;
                        return;
                    }
                    powerTicks = 0;
                    resetActors();
                    return;
                }
            }
        }
    }

    private boolean canMove(int row, int col, Direction direction) {
        int nr = row + direction.rowDelta();
        int nc = maze.wrapColumn(row, col + direction.colDelta());
        return maze.isWalkable(nr, nc);
    }

    private void moveOneTile(com.pacman.entities.Entity entity, Direction direction) {
        int nr = entity.getRow() + direction.rowDelta();
        int nc = maze.wrapColumn(entity.getRow(), entity.getCol() + direction.colDelta());
        if (maze.isWalkable(nr, nc)) entity.setPosition(nr, nc);
    }

    private void resetActors() {
        pacMan.reset();
        for (Ghost ghost : ghosts) ghost.reset();
    }

    public Maze getMaze() { return maze; }
    public PacMan getPacMan() { return pacMan; }
    public List<Ghost> getGhosts() { return List.copyOf(ghosts); }
    public GameState getState() { return state; }
    public int getScore() { return score; }
    public int getLives() { return lives; }
    public int getLevel() { return level; }
    public int getPowerTicks() { return powerTicks; }
}
