package com.pacman.entities;

import com.pacman.game.Direction;
import java.awt.Color;

public final class Ghost extends Entity {
    private final int startRow;
    private final int startCol;
    private final String assetName;
    private final Color fallbackColor;
    private final GhostPersonality personality;
    private int respawnTicks;

    public Ghost(int row, int col, String assetName, Color fallbackColor,
                 GhostPersonality personality) {
        super(row, col);
        this.startRow = row;
        this.startCol = col;
        this.assetName = assetName;
        this.fallbackColor = fallbackColor;
        this.personality = personality;
    }

    public String getAssetName() { return assetName; }
    public Color getFallbackColor() { return fallbackColor; }
    public GhostPersonality getPersonality() { return personality; }
    public boolean isRespawning() { return respawnTicks > 0; }

    public void setRespawnTicks(int ticks) { respawnTicks = ticks; }
    public void tickRespawn() { if (respawnTicks > 0) respawnTicks--; }

    public void reset() {
        row = startRow;
        col = startCol;
        direction = Direction.NONE;
        nextDirection = Direction.NONE;
        respawnTicks = 0;
    }
}
