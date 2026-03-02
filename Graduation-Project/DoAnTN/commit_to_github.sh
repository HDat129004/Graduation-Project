#!/usr/bin/env bash
# Commit DoAnTN project to GitHub

cd "$(dirname "$0")"

echo "=========================================="
echo "Committing DoAnTN to GitHub"
echo "=========================================="
echo ""

# Check if git is initialized
if [ ! -d .git ]; then
    echo "[1/5] Initializing git repository..."
    git init
    echo "✅ Git repository initialized"
else
    echo "[1/5] Git repository already exists"
fi

echo ""
echo "[2/5] Adding all files..."
git add .
echo "✅ All files added"

echo ""
echo "[3/5] Committing changes..."
git commit -m "Initial commit: DoAnTN - Automatic Unit Test Generator for Java

- Implemented automatic unit test generation for Java
- Source code analysis using JavaParser (AST)
- JUnit 5 test generation using JavaPoet
- Complete CLI tool and programmatic API
- Comprehensive documentation
- Unit and integration tests included
- Example: Calculator class with 6 test methods generated
- Ready for production use"

echo "✅ Changes committed"

echo ""
echo "[4/5] Setting up remote repository..."
# Check if remote exists
if ! git remote get-url origin >/dev/null 2>&1; then
    echo "Adding remote: https://github.com/HDat129004/Graduation-Project.git"
    git remote add origin https://github.com/HDat129004/Graduation-Project.git
    echo "✅ Remote repository added"
else
    echo "Remote already configured"
    git remote get-url origin
fi

echo ""
echo "[5/5] Pushing to GitHub..."
git branch -M main
git push -u origin main

echo ""
echo "=========================================="
echo "✅ Successfully pushed to GitHub!"
echo "=========================================="
echo ""
echo "Repository: https://github.com/HDat129004/Graduation-Project"
echo ""

