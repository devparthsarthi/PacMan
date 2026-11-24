

https://github.com/user-attachments/assets/9414643f-4ea2-41c6-95bf-d54b3a74d8b3


# Pac-Man – Java Edition

A classic Pac-Man arcade game recreated using **Core Java, Java Swing, and Data Structures & Algorithms**. The project features an interactive maze, intelligent ghosts with different personalities, power-ups, collision detection, and pathfinding algorithms.

## Description

Pac-Man is a 2D maze-based arcade game in which the player controls Pac-Man, collects dots, navigates through a maze, and avoids enemy ghosts.

The game uses Object-Oriented Programming to organize its components and game logic. Each ghost has a unique AI personality, creating different movement strategies and making gameplay more challenging.

This project demonstrates the practical implementation of Java programming, OOP principles, game development, and Data Structures & Algorithms.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java | Core programming language |
| Java Swing | Game window and graphical interface |
| Java AWT | Graphics rendering and keyboard input |
| Data Structures & Algorithms | Maze navigation and pathfinding |
| Git & GitHub | Version control and project hosting |

## Features

### Gameplay
- Interactive Pac-Man gameplay.
- Maze-based movement and navigation.
- Dot collection and scoring system.
- Ghost collision detection.
- Power mode to temporarily change ghost behavior.
- Start, pause, resume, game-over, and victory states.
- Restart functionality.

### Ghost AI and Personalities

Each ghost follows a different movement strategy:

| Ghost | Personality | Behavior |
|---|---|---|
| Red | Chaser | Directly pursues Pac-Man. |
| Pink | Ambusher | Targets a position ahead of Pac-Man. |
| Blue | Trickster | Uses projected targeting based on Pac-Man and the Red ghost. |
| Orange | Patroller | Chases when distant and retreats when close. |

Ghost movement speed is balanced to make gameplay more manageable.

### Technical Features
- Object-oriented project architecture.
- BFS-based pathfinding.
- Grid-based maze navigation.
- Keyboard-controlled movement.
- Asset management.
- High-score storage.
- Modular package organization.

## Data Structures & Algorithms Used

| Concept | Application |
|---|---|
| 2D Arrays / Grid | Representing the maze |
| Breadth-First Search (BFS) | Finding paths through the maze |
| Queue | Managing BFS traversal |
| Graph Traversal | Navigating connected maze positions |
| Manhattan Distance | Calculating distances between entities |
| Object-Oriented Programming | Organizing game components |

## Project Structure

```text
PacMan/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/pacman/
│   │           ├── algorithms/
│   │           ├── config/
│   │           ├── entities/
│   │           ├── game/
│   │           ├── storage/
│   │           ├── ui/
│   │           └── world/
│   └── test/
├── assets/
├── run.bat
├── test.bat
├── test.sh
└── README.md
```

## How the Game Works

1. The application initializes the game window and loads the maze and game assets.
2. The player starts the game and controls Pac-Man using the keyboard.
3. Pac-Man navigates through the maze and collects dots.
4. The game engine updates player and ghost positions.
5. Ghost AI calculates movement targets based on individual personalities.
6. Pathfinding helps ghosts navigate around maze obstacles.
7. Collision detection checks interactions between Pac-Man and ghosts.
8. Power-ups temporarily change ghost behavior.
9. The game updates the score and checks victory or game-over conditions.

## How to Run

### Prerequisites

- Java Development Kit (JDK 17 or later recommended).
- Windows, macOS, or Linux.
- Visual Studio Code or another Java-compatible IDE (optional).

### Method 1: Clone and Run on Windows

**1. Clone the repository**

```bash
git clone https://github.com/devparthsarthi/PacMan.git
```

**2. Open the project directory**

```bash
cd PacMan
```

**3. Run the game**

```powershell
.\run.bat
```

### Method 2: Run Using JAR

If a compiled JAR file is available:

```bash
java -jar PacMan-10-10.jar
```

### Verify Java Installation

```bash
java -version
javac -version
```

Make sure the JDK is installed and configured correctly.

## Controls

| Key | Action |
|---|---|
| Arrow Keys | Move Pac-Man |
| W / A / S / D | Alternative movement controls |
| P | Pause / Resume |
| Enter | Start / Continue |
| R | Restart |
| Esc | Pause |

## Future Scope

The project can be extended with:

- Multiple maze layouts and difficulty levels.
- More advanced ghost AI and pathfinding strategies.
- Improved animations and sound effects.
- Multiplayer mode.
- Additional power-ups and special abilities.
- Leaderboards and persistent player profiles.
- Customizable game settings.
- Performance optimization and expanded automated testing.

## Learning Outcomes

Through this project, the following concepts were explored:

- Java OOP principles and modular programming.
- GUI development using Swing and AWT.
- BFS and graph traversal.
- Grid-based game design.
- Collision detection and game-state management.
- Basic AI behavior and decision-making.
- Git and GitHub version control.

## Author

**Parthsarthi Sharma**

GitHub: [@devparthsarthi](https://github.com/devparthsarthi)



**Built with Java, Data Structures & Algorithms, and a little arcade nostalgia.**
