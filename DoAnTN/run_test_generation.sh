#!/usr/bin/env bash

echo "=========================================="
echo "DoAnTN - Automatic Test Generation"
echo "=========================================="
echo ""

cd "$(dirname "$0")"

echo "[1/3] Compiling the project..."
mvn clean compile
if [ $? -ne 0 ]; then
    echo "[ERROR] Compilation failed!"
    exit 1
fi

echo ""
echo "[2/3] Running tests..."
mvn test
if [ $? -ne 0 ]; then
    echo "[ERROR] Tests failed!"
    exit 1
fi

echo ""
echo "[3/3] Generating tests for example class..."
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java target/generated-tests"

echo ""
echo "=========================================="
echo "[SUCCESS] All done! Check target/generated-tests for generated test files."
echo "=========================================="

