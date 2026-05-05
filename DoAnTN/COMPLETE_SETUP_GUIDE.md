# DoAnTN Application - Complete Setup Guide
# Hướng Dẫn Cài Đặt Đầy Đủ

## Quick Start (3 Bước)

### Step 1: Install Maven (One-time setup)

**Windows (Easiest - using Chocolatey):**
```powershell
# Run PowerShell as Administrator, then:
choco install maven -y
```

If Chocolatey not installed:
```powershell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

**Or Manual Setup:**
1. Download Maven from: https://maven.apache.org/download.cgi (select Binary zip)
2. Extract to: `C:\Program Files\Maven`
3. Add `C:\Program Files\Maven\bin` to your Windows PATH environment variable
4. Restart your computer
5. Verify: Open Command Prompt and type `mvn -version`

**macOS:**
```bash
brew install maven
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get install maven
```

### Step 2: Build the Application

Open Command Prompt (Windows) or Terminal (Mac/Linux) in the DoAnTN folder:

```bash
mvn clean package -DskipTests
```

Wait for it to complete. You should see: `BUILD SUCCESS`

### Step 3: Run the Application

**Windows:**
- Double-click: `START_APP.bat`

**Mac/Linux:**
```bash
bash START_APP.sh
```

---

## ✅ After Setup

The app will:
1. ✓ Compile the project automatically
2. ✓ Create an executable JAR file
3. ✓ Launch a GUI window where you can:
   - Select a Java file to analyze
   - Choose an output directory for generated tests
   - Select generation method (AST/AI/Both)
   - Click "Generate Tests"

---

## 🎯 App Features Explained

### Method Selection

1. **AST Only** 
   - Fast and lightweight
   - Analyzes code structure using Abstract Syntax Tree
   - No internet connection needed
   - Good for basic test generation

2. **AI Only** (requires Gemini API key)
   - Uses Google's AI to understand code logic
   - Generates more intelligent test cases
   - Needs API key: https://makersuite.google.com/app/apikey

3. **Compare AST + AI**
   - Uses both methods and compares results
   - Shows strengths of each approach
   - Requires API key for AI

### Output

Generated test files appear in your specified output directory with:
- ✓ JUnit 5 test cases
- ✓ Automatic assertions
- ✓ Multiple test scenarios
- ✓ Parameter variations

---

## 🔍 Verification

After running the app:
1. Check your output directory for generated test files
2. Files should be named: `[ClassName]Test.java`
3. Open in an IDE to verify they compile
4. Run: `mvn test` to execute the tests

---

## ❓ FAQ

**Q: Do I need to do this setup every time?**
A: No! Setup is one-time. After that, just double-click `START_APP.bat`

**Q: Can I use AI features without API key?**
A: Yes! Choose "AST only" - it works without any API key

**Q: The app didn't generate tests. Why?**
A: Check:
- The Java file path is correct
- Output directory exists or is valid
- Java syntax is valid in the source file

**Q: How do I get a Gemini API key?**
A: Go to https://makersuite.google.com/app/apikey and create one

---

## 📞 Troubleshooting

### "Maven is not recognized"
- Maven not installed properly
- Restart computer after installation
- Verify: Open new Command Prompt and type `mvn -version`

### "Build failed"
- Run: `mvn clean package -DskipTests` manually in Command Prompt
- Check error messages
- Ensure Java 17+ is installed

### "App won't launch"
- Ensure JAR file exists in `target/doantn-0.1.0-SNAPSHOT.jar`
- Try: `java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui`

---

## 🎓 Project Info

- **Language**: Java 17
- **Testing Framework**: JUnit 5
- **Build Tool**: Maven
- **AST Analysis**: JavaParser
- **Code Generation**: JavaPoet
- **AI Integration**: Google Gemini API (optional)

Enjoy using DoAnTN! 🚀
