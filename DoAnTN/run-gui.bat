@echo off
REM DoAnTN - Automatic Test Generation UI Launcher
REM Simple launcher that builds the project if needed and runs the GUI

setlocal enabledelayedexpansion
cd /d "%~dp0"

echo.
echo ==========================================
echo DoAnTN - Build and Launch
echo ==========================================
echo.

REM Check if Java is available
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java is not installed or not in PATH
    echo Please install Java 17 or later
    pause
    exit /b 1
)

REM Try to build with Maven if available
where mvn >nul 2>&1
if errorlevel 1 (
    echo [INFO] Maven not found, attempting to use embedded/system Maven...
    REM Try to download and setup Maven automatically
    call :setup_maven
) else (
    echo [OK] Maven found in PATH
)

REM Build the project
echo [1/2] Building project...
mvn clean package -DskipTests -q
if errorlevel 1 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)

echo [OK] Build successful

REM Check if JAR was created
if not exist "target\doantn-0.1.0-SNAPSHOT.jar" (
    echo [ERROR] JAR file not created
    pause
    exit /b 1
)

echo [2/2] Launching GUI...
java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui

goto :eof

:setup_maven
REM This section would setup Maven if needed
REM For now, just return
exit /b 0
