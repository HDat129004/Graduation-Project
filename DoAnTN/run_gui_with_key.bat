@echo off
setlocal enabledelayedexpansion

echo =================================================================
echo  Khoi dong DoAnTN - Giao dien nguoi dung (GUI) - Che do tam thoi
echo =================================================================
echo.
echo  Luu y: File nay se nap API Key truc tiep vao chuong trinh.
echo  Chi su dung de test nhanh, khong nen chia se file nay.
echo.

cd /d "%~dp0"

REM Dat API Key tam thoi
set GEMINI_API_KEY=AIzaSyDzyDQvjgDALjBGELfuHB-_QjWpJhLJ_uc

echo [1/2] Dang xoa ban build cu va bien dich lai tu dau (Clean ^& Build)...
call mvn clean package -DskipTests
if !errorlevel! neq 0 (
    echo [Loi] Build that bai! Vui long kiem tra lai code.
    pause
    exit /b 1
)

echo.
echo [2/2] Dang mo giao dien voi API Key da nap...
java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui

echo.
pause