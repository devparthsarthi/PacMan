@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
echo Compiling PAC-MAN...
dir /s /b src\main\java\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if errorlevel 1 (
  echo.
  echo Compilation failed. Check that JDK 17 or newer is installed.
  pause
  exit /b 1
)
xcopy /E /I /Y src\main\resources out >nul
echo Starting PAC-MAN...
java -cp out com.pacman.App
