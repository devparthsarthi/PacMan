package com.pacman.config;

public final class GameConfig {
    private GameConfig() {}

    public static final int TILE_SIZE = 28;
    public static final int HUD_HEIGHT = 48;
    public static final int TICK_MS = 145;
    public static final int INITIAL_LIVES = 3;
    public static final int PELLET_SCORE = 10;
    public static final int POWER_PELLET_SCORE = 50;
    public static final int GHOST_SCORE = 200;
    public static final int POWER_DURATION_TICKS = 45;
    public static final int GHOST_MOVE_INTERVAL = 1;
    public static final String WINDOW_TITLE = "PAC-MAN | Java Edition";
}
