@echo off
REM DoAnTN - Automatic Test Generation App Launcher
REM Windows Batch Script - Double-click to run!

setlocal enabledelayedexpansion
cd /d "%~dp0"

echo.
echo =========================================
echo   DoAnTN - Auto-generate Unit Tests
echo =========================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not in PATH
    echo.
    echo Please download and install Java 17 from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

REM Try different Maven approaches
for /f "delims=" %%A in ('where mvn 2^>nul') do set MAVEN_CMD=%%A
if "!MAVEN_CMD!"=="" (
    REM Try local Maven
    if exist ".maven\bin\mvn.cmd" (
        set "MAVEN_CMD=.\.maven\bin\mvn.cmd"
    ) else if exist "%PROGRAMFILES%\Maven\bin\mvn.cmd" (
        set "MAVEN_CMD=%PROGRAMFILES%\Maven\bin\mvn.cmd"
    ) else (
        echo [ERROR] Maven is not installed
        echo.
        echo Option 1: Install Maven manually and add to PATH
        echo Option 2: Set MAVEN_HOME environment variable
        echo.
        echo Quick install via chocolatey:
        echo   choco install maven
        echo.
        echo Or download from: https://maven.apache.org/download.cgi
        echo.
        pause
        exit /b 1
    )
)

echo [INFO] Using Maven: !MAVEN_CMD!
echo.
echo [1/2] Compiling project...
"!MAVEN_CMD!" clean package -DskipTests -q

if errorlevel 1 (
    echo [ERROR] Build failed. Please check the errors above.
    pause
    exit /b 1
)

echo [OK] Build successful

REM Check if JAR was created
if not exist "target\doantn-0.1.0-SNAPSHOT.jar" (
    echo [ERROR] JAR file was not created
    pause
    exit /b 1
)

echo [2/2] Launching the GUI application...
echo.

REM Launch the GUI
java -jar "target\doantn-0.1.0-SNAPSHOT.jar" --gui

if errorlevel 1 (
    echo.
    echo [ERROR] Failed to launch GUI
    pause
)
