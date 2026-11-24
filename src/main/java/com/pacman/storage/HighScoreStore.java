package com.pacman.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class HighScoreStore {
    private static final Path FILE = Path.of(
            System.getProperty("user.home"), ".pacman-java", "highscore.txt");

    private HighScoreStore() {}

    public static int load() {
        try {
            if (Files.exists(FILE)) return Integer.parseInt(Files.readString(FILE).trim());
        } catch (IOException | NumberFormatException ignored) {
            // A missing or invalid score file should never prevent the game from starting.
        }
        return 0;
    }

    public static void saveIfHigher(int score) {
        if (score <= load()) return;
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, Integer.toString(score));
        } catch (IOException ignored) {
            // High-score persistence is best-effort.
        }
    }
}
