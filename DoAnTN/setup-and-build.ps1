# DoAnTN - Build Setup Script
# This script downloads Maven if needed and builds the project

$ProjectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$MavenDir = Join-Path $ProjectDir ".maven"
$MavenExe = Join-Path $MavenDir "bin/mvn.cmd"
$MavenVersion = "3.9.6"
$MavenUrl = "https://archive.apache.org/dist/maven/maven-3/$MavenVersion/binaries/apache-maven-$MavenVersion-bin.zip"
$MavenZip = Join-Path $MavenDir "maven.zip"

Write-Host "=========================================="
Write-Host "DoAnTN - Build Setup"
Write-Host "=========================================="
Write-Host ""

# Check if Maven is already available
if ((Get-Command mvn -ErrorAction SilentlyContinue) -or (Test-Path $MavenExe)) {
    Write-Host "[OK] Maven is available"
    $MvnCmd = if (Get-Command mvn -ErrorAction SilentlyContinue) { "mvn" } else { $MavenExe }
} else {
    Write-Host "[INFO] Downloading Maven $MavenVersion..."
    
    # Create .maven directory
    if (-not (Test-Path $MavenDir)) {
        New-Item -ItemType Directory -Path $MavenDir | Out-Null
    }
    
    # Download Maven
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        Invoke-WebRequest -Uri $MavenUrl -OutFile $MavenZip -ErrorAction Stop
        Write-Host "[OK] Downloaded Maven"
        
        # Extract Maven
        Write-Host "[INFO] Extracting Maven..."
        Expand-Archive -Path $MavenZip -DestinationPath $MavenDir -Force
        
        # Move to correct location
        $ExtractedDir = Get-ChildItem $MavenDir | Where-Object { $_.Name -like "apache-maven-*" } | Select-Object -First 1
        if ($ExtractedDir) {
            Move-Item -Path (Join-Path $ExtractedDir.FullName "*") -Destination $MavenDir -Force
            Remove-Item $ExtractedDir.FullName -Force
        }
        
        Remove-Item $MavenZip
        Write-Host "[OK] Maven extracted"
    } catch {
        Write-Host "[ERROR] Failed to download Maven: $_"
        exit 1
    }
    
    $MvnCmd = $MavenExe
}

Write-Host ""
Write-Host "[1/2] Building project..."
& $MvnCmd clean package -DskipTests -f "$ProjectDir/pom.xml"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "[OK] Build successful!"
    Write-Host ""
    Write-Host "[2/2] Creating launcher..."
    
    # Create GUI launcher batch
    $LauncherBat = @"
@echo off
setlocal enabledelayedexpansion
cd /d "%~dp0"
java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui
"@
    
    $LauncherBat | Out-File -FilePath "$ProjectDir/run-gui.bat" -Encoding ASCII
    
    # Create GUI launcher PowerShell
    $LauncherPs1 = @"
`$ProjectDir = Split-Path -Parent `$MyInvocation.MyCommand.Path
java -jar "`$ProjectDir/target/doantn-0.1.0-SNAPSHOT.jar" --gui
"@
    
    $LauncherPs1 | Out-File -FilePath "$ProjectDir/run-gui.ps1" -Encoding UTF8
    
    Write-Host "[OK] Launchers created:"
    Write-Host "  - run-gui.bat (for Command Prompt)"
    Write-Host "  - run-gui.ps1 (for PowerShell)"
    Write-Host ""
    Write-Host "=========================================="
    Write-Host "Completed! Run one of the launchers to start the app."
    Write-Host "=========================================="
} else {
    Write-Host ""
    Write-Host "[ERROR] Build failed!"
    exit 1
}
