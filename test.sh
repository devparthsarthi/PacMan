#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
javac -encoding UTF-8 -cp out -d out src/test/java/com/pacman/algorithms/PathFinderTest.java
java -cp out com.pacman.algorithms.PathFinderTest
