# DoAnTN - Automated Setup Script
# This script will:
# 1. Install Maven if not already installed
# 2. Build the project
# 3. Create shortcuts to run the app
# 4. Show how to launch the application

$ProjectDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$MavenVersion = "3.9.6"

Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "DoAnTN - Automated Setup Script" -ForegroundColor Cyan
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""

# Check if running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")

# Check Java
Write-Host "Step 1: Checking Java..." -ForegroundColor Yellow
try {
    $JavaVersion = java -version 2>&1 | Select-String "version" | ForEach-Object { $_.ToString() }
    Write-Host "✓ Java found: $JavaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ Java not found!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Please install Java 17 from: https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to continue"
    exit 1
}

# Check Maven
Write-Host ""
Write-Host "Step 2: Checking Maven..." -ForegroundColor Yellow

$MavenPath = $null

# Check if Maven is in PATH
try {
    $MavenVersion = mvn -version 2>&1 | Select-String "Apache Maven" | ForEach-Object { $_.ToString() }
    if ($MavenVersion) {
        Write-Host "✓ Maven found in PATH" -ForegroundColor Green
        $MavenPath = "mvn"
    }
} catch {
    # Maven not in PATH
}

# Check common Maven locations
if (-not $MavenPath) {
    $CommonPaths = @(
        "C:\Program Files\Maven\bin\mvn.cmd",
        "C:\Program Files (x86)\Maven\bin\mvn.cmd",
        "$env:MAVEN_HOME\bin\mvn.cmd"
    )
    
    foreach ($path in $CommonPaths) {
        if (Test-Path $path) {
            Write-Host "✓ Maven found at: $path" -ForegroundColor Green
            $MavenPath = $path
            break
        }
    }
}

# If Maven still not found, offer to install via Chocolatey
if (-not $MavenPath) {
    Write-Host "✗ Maven not found in standard locations" -ForegroundColor Red
    Write-Host ""
    
    # Check if Chocolatey is available
    $ChocolateyPath = "$env:ProgramData\chocolatey\bin\choco.exe"
    if (Test-Path $ChocolateyPath) {
        Write-Host "Chocolatey found! Installing Maven..." -ForegroundColor Yellow
        
        if ($isAdmin) {
            & $ChocolateyPath install maven -y
            if ($LASTEXITCODE -eq 0) {
                Write-Host "✓ Maven installed successfully" -ForegroundColor Green
                $MavenPath = "mvn"
                $env:Path += ";C:\Program Files\Maven\bin"
            } else {
                Write-Host "✗ Failed to install Maven via Chocolatey" -ForegroundColor Red
            }
        } else {
            Write-Host "This script needs to be run as Administrator to install Maven via Chocolatey" -ForegroundColor Red
            Write-Host ""
            Write-Host "Option 1: Re-run this script as Administrator" -ForegroundColor Yellow
            Write-Host "Option 2: Install Maven manually from https://maven.apache.org/download.cgi" -ForegroundColor Yellow
            Write-Host ""
            Read-Host "Press Enter to continue"
            exit 1
        }
    } else {
        Write-Host "Chocolatey not found. Please install Maven manually:" -ForegroundColor Yellow
        Write-Host "  1. Download from: https://maven.apache.org/download.cgi" -ForegroundColor Yellow
        Write-Host "  2. Extract to: C:\Program Files\Maven" -ForegroundColor Yellow
        Write-Host "  3. Add to PATH: C:\Program Files\Maven\bin" -ForegroundColor Yellow
        Write-Host "  4. Restart computer" -ForegroundColor Yellow
        Write-Host ""
        Read-Host "Press Enter to continue"
        exit 1
    }
}

# Build the project
Write-Host ""
Write-Host "Step 3: Building DoAnTN project..." -ForegroundColor Yellow
Write-Host ""

cd $ProjectDir
& mvn clean package -DskipTests

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "✗ Build failed!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Troubleshooting:" -ForegroundColor Yellow
    Write-Host "1. Ensure Java 17+ is installed: java -version" -ForegroundColor Yellow
    Write-Host "2. Ensure Maven is properly installed: mvn -version" -ForegroundColor Yellow
    Write-Host "3. Check the error messages above" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to continue"
    exit 1
}

# Verify JAR file
Write-Host ""
if (Test-Path "$ProjectDir\target\doantn-0.1.0-SNAPSHOT.jar") {
    Write-Host "✓ Build successful! JAR file created." -ForegroundColor Green
} else {
    Write-Host "✗ Build completed but JAR file not found!" -ForegroundColor Red
    Read-Host "Press Enter to continue"
    exit 1
}

# Create desktop shortcut
Write-Host ""
Write-Host "Step 4: Creating shortcuts..." -ForegroundColor Yellow

$DesktopPath = [Environment]::GetFolderPath("Desktop")
$ShortcutPath = Join-Path $DesktopPath "DoAnTN - Auto-generate Tests.lnk"

try {
    $WshShell = New-Object -ComObject WScript.Shell
    $Shortcut = $WshShell.CreateShortcut($ShortcutPath)
    $Shortcut.TargetPath = "$ProjectDir\START_APP.bat"
    $Shortcut.WorkingDirectory = $ProjectDir
    $Shortcut.Description = "DoAnTN - Automatic Unit Test Generation"
    $Shortcut.Save()
    Write-Host "✓ Created desktop shortcut: DoAnTN - Auto-generate Tests" -ForegroundColor Green
} catch {
    Write-Host "⚠ Could not create desktop shortcut" -ForegroundColor Yellow
}

# Final message
Write-Host ""
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host "✓ Setup Complete!" -ForegroundColor Green
Write-Host "==========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "You can now launch the application by:" -ForegroundColor Yellow
Write-Host "  1. Double-clicking: START_APP.bat" -ForegroundColor Yellow
Write-Host "  2. Desktop shortcut: DoAnTN - Auto-generate Tests" -ForegroundColor Yellow
Write-Host "  3. Running this command:" -ForegroundColor Yellow
Write-Host "     java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui" -ForegroundColor Yellow
Write-Host ""
Write-Host "For detailed guide, see: COMPLETE_SETUP_GUIDE.md" -ForegroundColor Cyan
Write-Host ""

Read-Host "Press Enter to launch the application now"

java -jar "$ProjectDir\target\doantn-0.1.0-SNAPSHOT.jar" --gui
