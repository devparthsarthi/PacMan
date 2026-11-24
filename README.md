# PAC-MAN — Java Edition

A complete desktop Pac-Man-style maze game built with **Core Java, Swing and AWT**. It uses the original supplied pixel-art assets and a refactored object-oriented architecture.

## Features
- Four distinct ghost personalities: direct chaser, ambusher, vector-based trickster, and close-range patroller
- Slower ghost movement that gradually speeds up across levels
- Start menu, pause/resume, game-over and victory states
- Keyboard controls: arrows or WASD
- Four ghosts with BFS-based shortest-path pursuit
- Frightened mode after collecting power pellets
- Pellet and power-pellet scoring
- Lives, five levels, and persistent high score
- Tunnel wraparound
- Original image assets with Java-drawn fallbacks
- No third-party runtime dependencies

## Requirements
- JDK 17 or newer (JDK 21 recommended)
- VS Code with Extension Pack for Java (optional)

## Run on Windows
Double-click `run.bat`, or run:
```powershell
.\run.bat
```

## Run on macOS / Linux
```bash
chmod +x run.sh
./run.sh
```

## Compile and run manually
```bash
mkdir -p out
javac -encoding UTF-8 -d out -sourcepath src/main/java src/main/java/com/pacman/App.java
# Copy resources to output classpath
# Windows PowerShell:
Copy-Item -Recurse -Force src/main/resources/* out/
java -cp out com.pacman.App
```

## Controls
| Key | Action |
|---|---|
| Enter | Start / continue after level |
| Arrow keys or WASD | Move |
| P or Esc | Pause |
| R | Restart after game over / victory |

## Architecture
```text
src/
├── main/
│   ├── java/com/pacman/
│   │   ├── App.java
│   │   ├── algorithms/PathFinder.java
│   │   ├── config/GameConfig.java
│   │   ├── entities/{Entity,PacMan,Ghost}.java
│   │   ├── game/{Direction,GameState,GameEngine}.java
│   │   ├── storage/HighScoreStore.java
│   │   ├── ui/{AssetManager,GamePanel}.java
│   │   └── world/Maze.java
│   └── resources/assets/  # supplied PNG sprites
└── test/java/com/pacman/algorithms/PathFinderTest.java
```

## DSA used
- **BFS (Breadth-First Search):** Finds shortest paths for ghost pursuit.
- **Queue (`ArrayDeque`):** Implements BFS frontier.
- **2D arrays:** Maze, visited, distance and first-step tracking.
- **Hash-free graph traversal:** Each walkable maze tile is a graph vertex; valid adjacent tiles are edges.
- **Complexity:** BFS is O(V + E) time and O(V) auxiliary space, where V is walkable tiles and E is at most 4V.

## OOP used
- Abstraction: `Entity` is an abstract base class.
- Inheritance: `PacMan` and `Ghost` extend `Entity`.
- Encapsulation: game state and entity state are managed through classes.
- Enums: `Direction` and `GameState`.
- Separation of concerns: game rules, rendering, assets, maze, pathfinding and persistence are separate.

## Tests
Run the dependency-free pathfinding smoke test:
```bash
javac -encoding UTF-8 -d out -sourcepath src/main/java:src/test/java src/test/java/com/pacman/algorithms/PathFinderTest.java
java -cp out com.pacman.algorithms.PathFinderTest
```
On Windows, replace `:` in the classpath with `;`.

## Notes
This is an educational original implementation inspired by classic maze-chase mechanics. It is not affiliated with or endorsed by the owners of Pac-Man.


## Ghost behavior and speed
| Level | Ghost movement interval |
|---|---|
| 1–2 | One move every 7 game ticks |
| 3–4 | One move every 6 game ticks |
| 5 | One move every 5 game ticks |

Each ghost has a distinct strategy:
- Red (Blinky): follows Pac-Man's current tile.
- Pink (Pinky): aims up to four walkable tiles ahead of Pac-Man.
- Blue (Inky): calculates a vector target using Pac-Man's position and Red's position.
- Orange (Clyde): chases from a distance and retreats when close.
- During power mode, all ghosts try to move away from Pac-Man.
