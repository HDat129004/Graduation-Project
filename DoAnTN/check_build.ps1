#!/usr/bin/env pwsh
# Script to check if the project builds and tests pass

cd "C:\Users\Admin\Desktop\DATN\DoAnTN"

Write-Host "===============================================" -ForegroundColor Green
Write-Host "Starting Maven Build and Test" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Green
Write-Host ""

# Clean and compile
Write-Host "Step 1: Clean and Compile..." -ForegroundColor Cyan
mvn clean compile
if ($LASTEXITCODE -ne 0) {
    Write-Host "COMPILATION FAILED!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Step 2: Running Tests..." -ForegroundColor Cyan
mvn test
if ($LASTEXITCODE -ne 0) {
    Write-Host "TESTS FAILED!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "Step 3: Building JAR..." -ForegroundColor Cyan
mvn package -DskipTests
if ($LASTEXITCODE -ne 0) {
    Write-Host "BUILD FAILED!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "===============================================" -ForegroundColor Green
Write-Host "ALL CHECKS PASSED!" -ForegroundColor Green
Write-Host "===============================================" -ForegroundColor Green

