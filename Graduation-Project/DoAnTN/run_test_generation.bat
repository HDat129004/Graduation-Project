@echo off
setlocal enabledelayedexpansion

echo.
echo ==========================================
echo DoAnTN - Automatic Test Generation
echo ==========================================
echo.

cd /d "%~dp0"

echo [1/3] Compiling the project...
call mvn clean compile
if !errorlevel! neq 0 (
    echo [ERROR] Compilation failed!
    exit /b 1
)

echo.
echo [2/3] Running tests...
call mvn test
if !errorlevel! neq 0 (
    echo [ERROR] Tests failed!
    exit /b 1
)

echo.
echo [3/3] Generating tests for example class...
call mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java target/generated-tests"

echo.
echo ==========================================
echo [SUCCESS] All done! Check target/generated-tests for generated test files.
echo ==========================================
echo.
pause

