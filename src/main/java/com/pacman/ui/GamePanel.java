package com.pacman.ui;

import com.pacman.config.GameConfig;
import com.pacman.entities.Ghost;
import com.pacman.game.Direction;
import com.pacman.game.GameEngine;
import com.pacman.game.GameState;
import com.pacman.storage.HighScoreStore;
import com.pacman.world.Maze;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public final class GamePanel extends JPanel implements ActionListener, KeyListener {
    private static final Color BACKGROUND = new Color(7, 10, 24);
    private static final Color WALL = new Color(30, 91, 210);
    private static final Color WALL_EDGE = new Color(91, 171, 255);
    private static final Color PAC_YELLOW = new Color(255, 219, 35);

    private final GameEngine engine = new GameEngine();
    private final AssetManager assets = new AssetManager();
    private final Timer timer = new Timer(GameConfig.TICK_MS, this);
    private int highScore = HighScoreStore.load();
    private int animationFrame;

    public GamePanel() {
        int width = engine.getMaze().getCols() * GameConfig.TILE_SIZE;
        int height = engine.getMaze().getRows() * GameConfig.TILE_SIZE + GameConfig.HUD_HEIGHT;
        setPreferredSize(new Dimension(width, height));
        setBackground(BACKGROUND);
        setFocusable(true);
        addKeyListener(this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D g = (Graphics2D) graphics.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawHud(g);
        drawMaze(g);
        drawActors(g);
        drawOverlay(g);
        g.dispose();
    }

    private void drawHud(Graphics2D g) {
        g.setColor(new Color(12, 18, 39));
        g.fillRect(0, 0, getWidth(), GameConfig.HUD_HEIGHT);
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        g.setColor(PAC_YELLOW);
        g.drawString("PAC-MAN", 14, 20);
        g.setFont(new Font("SansSerif", Font.PLAIN, 12));
        g.setColor(Color.WHITE);
        g.drawString("SCORE  " + engine.getScore(), 14, 38);
        g.drawString("HIGH  " + Math.max(highScore, engine.getScore()), 150, 30);
        g.drawString("LEVEL  " + engine.getLevel(), 290, 30);
        g.drawString("LIVES  " + engine.getLives(), 390, 30);
    }

    private void drawMaze(Graphics2D g) {
        Maze maze = engine.getMaze();
        int size = GameConfig.TILE_SIZE;
        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                int x = c * size;
                int y = GameConfig.HUD_HEIGHT + r * size;
                if (maze.isWall(r, c)) {
                    var wallImage = assets.get("wall.png");
                    if (wallImage != null) g.drawImage(wallImage, x, y, size, size, this);
                    else {
                        g.setColor(WALL);
                        g.fillRoundRect(x + 1, y + 1, size - 2, size - 2, 8, 8);
                        g.setColor(WALL_EDGE);
                        g.setStroke(new BasicStroke(1.2f));
                        g.drawRoundRect(x + 3, y + 3, size - 6, size - 6, 6, 6);
                    }
                } else if (maze.hasPowerPellet(r, c)) {
                    g.setColor(new Color(255, 245, 180));
                    int pulse = animationFrame % 2 == 0 ? 11 : 8;
                    g.fillOval(x + (size - pulse) / 2, y + (size - pulse) / 2, pulse, pulse);
                } else if (maze.hasPellet(r, c)) {
                    g.setColor(new Color(255, 231, 190));
                    g.fillOval(x + size / 2 - 2, y + size / 2 - 2, 4, 4);
                }
            }
        }
    }

    private void drawActors(Graphics2D g) {
        int size = GameConfig.TILE_SIZE;
        var player = engine.getPacMan();
        String playerAsset = switch (player.getDirection()) {
            case UP -> "pacmanUp.png";
            case DOWN -> "pacmanDown.png";
            case LEFT, NONE -> "pacmanLeft.png";
            case RIGHT -> "pacmanRight.png";
        };
        int px = player.getCol() * size;
        int py = GameConfig.HUD_HEIGHT + player.getRow() * size;
        drawImageOrOval(g, playerAsset, px, py, size, size, PAC_YELLOW);

        for (Ghost ghost : engine.getGhosts()) {
            if (ghost.isRespawning()) continue;
            int x = ghost.getCol() * size;
            int y = GameConfig.HUD_HEIGHT + ghost.getRow() * size;
            if (engine.getPowerTicks() > 0) {
                drawImageOrOval(g, "scaredGhost.png", x, y, size, size, new Color(50, 90, 230));
            } else {
                drawImageOrOval(g, ghost.getAssetName(), x, y, size, size, ghost.getFallbackColor());
            }
        }
    }

    private void drawImageOrOval(Graphics2D g, String name, int x, int y, int w, int h, Color fallback) {
        var image = assets.get(name);
        if (image != null) g.drawImage(image, x, y, w, h, this);
        else {
            g.setColor(fallback);
            g.fillOval(x + 2, y + 2, w - 4, h - 4);
        }
    }

    private void drawOverlay(Graphics2D g) {
        GameState state = engine.getState();
        if (state == GameState.PLAYING) return;
        g.setColor(new Color(0, 0, 0, 190));
        g.fillRect(0, GameConfig.HUD_HEIGHT, getWidth(), getHeight() - GameConfig.HUD_HEIGHT);

        String title;
        String subtitle;
        switch (state) {
            case MENU -> {
                title = "PAC-MAN";
                subtitle = "PRESS ENTER TO START";
            }
            case PAUSED -> {
                title = "PAUSED";
                subtitle = "PRESS P TO RESUME";
            }
            case LEVEL_COMPLETE -> {
                title = "LEVEL " + (engine.getLevel() - 1) + " CLEAR!";
                subtitle = "PRESS ENTER FOR NEXT LEVEL";
            }
            case GAME_OVER -> {
                title = "GAME OVER";
                subtitle = "PRESS R TO PLAY AGAIN";
            }
            case VICTORY -> {
                title = "YOU WIN!";
                subtitle = "PRESS R TO PLAY AGAIN";
            }
            default -> {
                title = "";
                subtitle = "";
            }
        }
        g.setFont(new Font("SansSerif", Font.BOLD, 34));
        g.setColor(PAC_YELLOW);
        drawCentered(g, title, GameConfig.HUD_HEIGHT + (getHeight() - GameConfig.HUD_HEIGHT) / 2 - 12);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        g.setColor(Color.WHITE);
        drawCentered(g, subtitle, GameConfig.HUD_HEIGHT + (getHeight() - GameConfig.HUD_HEIGHT) / 2 + 22);
        if (state == GameState.MENU) {
            g.setColor(new Color(190, 205, 235));
            g.setFont(new Font("SansSerif", Font.PLAIN, 12));
            drawCentered(g, "ARROWS / WASD  MOVE     P  PAUSE     R  RESTART", getHeight() - 35);
        }
    }

    private void drawCentered(Graphics2D g, String text, int y) {
        FontMetrics metrics = g.getFontMetrics();
        g.drawString(text, (getWidth() - metrics.stringWidth(text)) / 2, y);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        animationFrame++;
        if (engine.getState() == GameState.PLAYING) engine.tick();
        if (engine.getState() == GameState.LEVEL_COMPLETE) {
            // The transition is deliberately user-controlled via Enter.
        }
        if (engine.getScore() > highScore) {
            highScore = engine.getScore();
            HighScoreStore.saveIfHigher(highScore);
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_UP, KeyEvent.VK_W -> engine.setPlayerDirection(Direction.UP);
            case KeyEvent.VK_DOWN, KeyEvent.VK_S -> engine.setPlayerDirection(Direction.DOWN);
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> engine.setPlayerDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> engine.setPlayerDirection(Direction.RIGHT);
            case KeyEvent.VK_P -> engine.togglePause();
            case KeyEvent.VK_ENTER -> {
                if (engine.getState() == GameState.MENU) engine.startNewGame();
                else if (engine.getState() == GameState.LEVEL_COMPLETE) engine.continueAfterLevel();
            }
            case KeyEvent.VK_R -> engine.restartAfterEnd();
            case KeyEvent.VK_ESCAPE -> {
                if (engine.getState() == GameState.PLAYING) engine.togglePause();
            }
            default -> { }
        }
        repaint();
    }

    @Override public void keyReleased(KeyEvent event) {}
    @Override public void keyTyped(KeyEvent event) {}

    public void shutdown() { timer.stop(); }
}
