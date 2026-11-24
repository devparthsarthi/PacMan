package com.pacman.entities;

/**
 * Each ghost uses a different targeting strategy.
 */
public enum GhostPersonality {
    RED_CHASER,       // Directly pursues Pac-Man.
    PINK_AMBUSHER,    // Targets the tiles ahead of Pac-Man.
    BLUE_TRICKSTER,   // Uses Pac-Man and the red ghost to calculate a vector target.
    ORANGE_PATROLLER  // Chases from far away, retreats when close.
}
