@echo off
setlocal enabledelayedexpansion

echo ==========================================
echo Khoi dong DoAnTN - Giao dien nguoi dung (GUI)
echo ==========================================
echo.

cd /d "%~dp0"

echo [1/2] Kiem tra xem da co file .jar chua...
if not exist "target\doantn-0.1.0-SNAPSHOT.jar" (
    echo [Thong bao] Chua tim thay file .jar. Dang thu build project qua Maven...
    call mvn clean package -DskipTests
    if !errorlevel! neq 0 (
        echo [Loi] Khong the build tu dong bang Maven. Vui long chay file SETUP.bat truoc hoac cai dat Maven.
        pause
        exit /b 1
    )
) else (
    echo [OK] Da tim thay file target\doantn-0.1.0-SNAPSHOT.jar
)

echo.
echo [2/2] Dang mo giao dien...
java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui

echo.
pause
