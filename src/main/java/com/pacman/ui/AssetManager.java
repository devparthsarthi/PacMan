package com.pacman.ui;

import java.awt.Image;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;

public final class AssetManager {
    private final Map<String, Image> images = new HashMap<>();

    public AssetManager() {
        String[] names = {
            "wall.png", "pacmanUp.png", "pacmanDown.png", "pacmanLeft.png",
            "pacmanRight.png", "redGhost.png", "pinkGhost.png", "blueGhost.png",
            "orangeGhost.png", "scaredGhost.png", "powerFood.png", "cherry.png", "cherry2.png"
        };
        for (String name : names) {
            var url = getClass().getResource("/assets/" + name);
            if (url != null) images.put(name, new ImageIcon(url).getImage());
        }
    }

    public Image get(String name) { return images.get(name); }
}
