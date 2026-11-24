@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
dir /s /b src\main\java\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if errorlevel 1 exit /b 1
javac -encoding UTF-8 -cp out -d out src\test\java\com\pacman\algorithms\PathFinderTest.java
if errorlevel 1 exit /b 1
java -cp out com.pacman.algorithms.PathFinderTest
