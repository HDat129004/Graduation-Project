#!/bin/bash
# DoAnTN - Automatic Test Generation App Launcher
# For Mac/Linux users

PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"

echo ""
echo "=========================================="
echo "  DoAnTN - Auto-generate Unit Tests"
echo "=========================================="
echo ""

# Check if Java is available
if ! command -v java &> /dev/null; then
    echo "[ERROR] Java is not installed or not in PATH"
    echo ""
    echo "Please install Java 17 from:"
    echo "https://www.oracle.com/java/technologies/downloads/"
    echo ""
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | grep -oP 'version "\K[0-9]+' | head -1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "[ERROR] Java 17 or later is required (found version $JAVA_VERSION)"
    exit 1
fi

# Check if Maven is available
if ! command -v mvn &> /dev/null; then
    echo "[ERROR] Maven is not installed or not in PATH"
    echo ""
    echo "Install Maven via:"
    echo "  macOS: brew install maven"
    echo "  Linux: sudo apt-get install maven"
    echo ""
    echo "Or download from: https://maven.apache.org/download.cgi"
    echo ""
    exit 1
fi

echo "[INFO] Java Version: $(java -version 2>&1 | head -1 | cut -d' ' -f3)"
echo "[INFO] Maven Version: $(mvn -version 2>&1 | head -1)"
echo ""
echo "[1/2] Compiling project..."

cd "$PROJECT_DIR"

# Build with Maven
mvn clean package -DskipTests -q

if [ $? -ne 0 ]; then
    echo "[ERROR] Build failed. Please check the errors above."
    exit 1
fi

echo "[OK] Build successful"

# Check if JAR was created
if [ ! -f "target/doantn-0.1.0-SNAPSHOT.jar" ]; then
    echo "[ERROR] JAR file was not created"
    exit 1
fi

echo "[2/2] Launching the GUI application..."
echo ""

# Launch the GUI
java -jar "target/doantn-0.1.0-SNAPSHOT.jar" --gui

if [ $? -ne 0 ]; then
    echo ""
    echo "[ERROR] Failed to launch GUI"
    exit 1
fi
