@echo off
cd /d "c:\Users\Admin\Desktop\Test Design\Graduation-Project test\DoAnTN"

echo.
echo ===============================================
echo Starting Maven Build and Test
echo ===============================================
echo.

echo Step 1: Clean and Compile...
call mvn clean compile
if %errorlevel% neq 0 (
    echo COMPILATION FAILED!
    exit /b 1
)

echo.
echo Step 2: Running Tests...
call mvn test
if %errorlevel% neq 0 (
    echo TESTS FAILED!
    exit /b 1
)

echo.
echo Step 3: Building JAR...
call mvn package -DskipTests
if %errorlevel% neq 0 (
    echo BUILD FAILED!
    exit /b 1
)

echo.
echo ===============================================
echo ALL CHECKS PASSED!
echo ===============================================
echo.
pause

