@echo off
REM DoAnTN - Automated Setup and Installation
REM This script will install Maven and build the project

setlocal enabledelayedexpansion
cd /d "%~dp0"

echo.
echo ==========================================
echo DoAnTN - Automated Setup
echo ==========================================
echo.

REM Check Java
echo Checking Java...
java -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java not found
    echo Please install Java 17 from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)
echo [OK] Java found

REM Check Maven
echo Checking Maven...
where mvn >nul 2>&1
if errorlevel 1 (
    echo [INFO] Maven not in PATH
    
    REM Try Chocolatey
    where choco >nul 2>&1
    if errorlevel 1 (
        echo [INFO] Chocolatey not found
        echo.
        echo Please install Maven manually:
        echo 1. Download: https://maven.apache.org/download.cgi
        echo 2. Extract to: C:\Program Files\Maven
        echo 3. Add to PATH: C:\Program Files\Maven\bin
        echo 4. Restart computer and try again
        echo.
        pause
        exit /b 1
    ) else (
        echo [INFO] Installing Maven via Chocolatey...
        choco install maven -y
        if errorlevel 1 (
            echo [ERROR] Failed to install Maven
            pause
            exit /b 1
        )
        echo [OK] Maven installed
    )
) else (
    echo [OK] Maven found in PATH
)

REM Build
echo.
echo Building project...
mvn clean package -DskipTests
if errorlevel 1 (
    echo [ERROR] Build failed
    pause
    exit /b 1
)

echo [OK] Build successful
echo.
echo ==========================================
echo Setup Complete!
echo ==========================================
echo.
echo The application is ready. You can now:
echo.
echo Option 1: Double-click START_APP.bat
echo Option 2: Run this command:
echo    java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui
echo.

pause
