@echo off
setlocal enabledelayedexpansion

echo.
echo ==========================================
echo DoAnTN - Automatic Test Generation (AST + AI)
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
echo [2/3] Generating tests for example class (Using both AST and AI)...
java -jar target/doantn-0.1.0-SNAPSHOT.jar src/main/java/com/doantn/example/Calculator.java src/test/java

echo.
echo [3/3] Running tests and generating JaCoCo coverage report...
call mvn test
if !errorlevel! neq 0 (
    echo [WARN] Some tests failed, but continuing to generate coverage report...
)

echo.
echo ==========================================
echo [SUCCESS] All done!
echo [OUTPUT] Check src/test/java for generated test files (AST and AI).
echo [REPORT] Coverage report generated at: target/site/jacoco/index.html
echo ==========================================
echo.
pause
