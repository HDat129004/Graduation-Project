@echo off
REM Commit DoAnTN project to GitHub

cd /d "%~dp0"

echo.
echo ==========================================
echo Committing DoAnTN to GitHub
echo ==========================================
echo.

REM Check if git is initialized
if not exist .git (
    echo [1/5] Initializing git repository...
    call git init
    echo 55 Git repository initialized
) else (
    echo [1/5] Git repository already exists
)

echo.
echo [2/5] Adding all files...
call git add .
echo 55 All files added

echo.
echo [3/5] Committing changes...
call git commit -m "Initial commit: DoAnTN - Automatic Unit Test Generator for Java"

echo 55 Changes committed

echo.
echo [4/5] Setting up remote repository...
REM Check if remote exists
git remote get-url origin >nul 2>&1
if errorlevel 1 (
    echo Adding remote: https://github.com/HDat129004/Graduation-Project.git
    call git remote add origin https://github.com/HDat129004/Graduation-Project.git
    echo 55 Remote repository added
) else (
    echo Remote already configured
    git remote get-url origin
)

echo.
echo [5/5] Pushing to GitHub...
call git branch -M main
call git push -u origin main

echo.
echo ==========================================
echo 55 Successfully pushed to GitHub!
echo ==========================================
echo.
echo Repository: https://github.com/HDat129004/Graduation-Project
echo.
pause

