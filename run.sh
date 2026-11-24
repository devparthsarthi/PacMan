#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
mkdir -p out
find src/main/java -name "*.java" > sources.txt
javac -encoding UTF-8 -d out @sources.txt
cp -R src/main/resources/. out/
java -cp out com.pacman.App
