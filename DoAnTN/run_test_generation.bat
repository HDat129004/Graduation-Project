@echo off
setlocal enabledelayedexpansion

echo.
echo ==========================================
echo DoAnTN - Automatic Test Generation
echo ==========================================
echo.

cd /d "%~dp0"

echo [1/3] Compiling and packaging the project...
call mvn clean package -DskipTests
if !errorlevel! neq 0 (
    echo [ERROR] Compilation failed!
    exit /b 1
)

echo.
echo [2/3] Generating tests for example class...
java -jar target/doantn-0.1.0-SNAPSHOT.jar src/main/java/com/doantn/example/Calculator.java src/test/java

echo.
echo [3/3] Running tests...
call mvn test
if !errorlevel! neq 0 (
    echo [ERROR] Tests failed!
    exit /b 1
)

echo.
echo ==========================================
echo [SUCCESS] All done! Check src/test/java for generated test files.
echo ==========================================
echo.
pause
