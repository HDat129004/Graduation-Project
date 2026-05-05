# 🚀 DoAnTN - Quick Start Guide

## ⚡ TL;DR (Tóm Tắt)

Your **DoAnTN Unit Test Generator** is almost ready! Just one step needed:

1. **Install Maven** (5 minutes)
2. **Double-click `START_APP.bat`**
3. **Done!** The GUI app will launch

---

## 📥 Install Maven (One-time setup)

### Option 1: Easy Way (Recommended)

If your Windows has Chocolatey installed:
```bash
choco install maven -y
```

Then restart your computer and you're done!

### Option 2: Manual Way

1. **Download Maven:**
   - Visit: https://maven.apache.org/download.cgi
   - Download "Binary zip archive" (apache-maven-X.X.X-bin.zip)

2. **Extract the file:**
   - Extract to: `C:\Program Files\Maven`
   - You should see: `C:\Program Files\Maven\bin\mvn.cmd`

3. **Add Maven to Windows PATH:**
   - Press `Win + X` → System → Advanced System Settings
   - Click "Environment Variables"
   - Under "User variables" → Click "New"
     - Variable name: `MAVEN_HOME`
     - Variable value: `C:\Program Files\Maven`
   - Under "User variables" for Path → Click "Edit"
     - Click "New"
     - Add: `C:\Program Files\Maven\bin`
   - Click OK on all windows

4. **Verify Installation:**
   - Open new Command Prompt
   - Type: `mvn -version`
   - Should show Maven version info

5. **Restart your computer**

### Option 3: For Mac Users

```bash
brew install maven
```

### Option 4: For Linux Users

```bash
sudo apt-get install maven  # Ubuntu/Debian
sudo yum install maven      # CentOS/RHEL
```

---

## ▶️ Launch the Application

After Maven is installed:

### Windows
Double-click: **`START_APP.bat`** in the DoAnTN folder

Or run in Command Prompt:
```bash
START_APP.bat
```

Or in PowerShell:
```powershell
.\START_APP.bat
```

### Mac/Linux
```bash
bash START_APP.sh
```

---

## 💡 What Happens When You Launch

1. **Automatic Build**: The app will compile itself (takes 30-60 seconds first time)
2. **GUI Window Opens**: You'll see the DoAnTN interface
3. **Ready to Use**: Select a Java file and generate tests!

---

## 🎯 How to Use the App

### Step 1: Select Source File
- Click "Browse" next to "Source Path"
- Choose a Java file or folder containing Java files

### Step 2: Choose Output Directory  
- Default is: `src/test/java`
- Click "Browse" to change if needed

### Step 3: Choose Generation Method
- **AST only**: Fast, no API key needed ✓ Recommended for first time
- **AI only**: Requires Gemini API key
- **Compare AST + AI**: Uses both methods

### Step 4: Generate Tests
- Click the green **"Generate Tests"** button
- Watch the output in the black console area
- Generated files appear in your output directory

---

## 🔑 Optional: Use AI Features

To use AI-powered test generation:

1. Get free Gemini API key:
   - Go to: https://makersuite.google.com/app/apikey
   - Create a new API key
   - Copy the key

2. In the app:
   - Paste the key in "Gemini API Key" field
   - Select "AI only" or "Compare AST + AI"
   - Click "Generate Tests"

---

## ✅ Verify It Worked

After generating tests:
1. Find the generated files in your output directory
2. They should be named: `[ClassName]Test.java`
3. Example: `CalculatorTest.java` for `Calculator.java`

To run the tests:
```bash
mvn test
```

---

## 🆘 Troubleshooting

### Problem: "Java is not installed"
**Solution:**
- Download Java 17+ from: https://www.oracle.com/java/technologies/downloads/
- Install it
- Restart your computer

### Problem: "Maven is not installed"  
**Solution:**
- Follow the Maven installation steps above
- Restart your computer after installation

### Problem: App won't start
**Solution:**
```bash
# Try running this command directly
java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui
```

### Problem: Build fails
**Solution:**
1. Open Command Prompt in the DoAnTN folder
2. Run: `mvn clean compile`
3. Check error messages
4. Ensure Java 17+ and Maven 3.8+ are installed

### Problem: "API key not working"
**Solution:**
- Verify the key is correct from https://makersuite.google.com/app/apikey
- Key should start with `AIzaSy...`
- Try the AST-only mode first (doesn't need API key)

---

## 📁 File Structure

```
DoAnTN/
├── START_APP.bat          ← Click this to run!
├── START_APP.sh           ← For Mac/Linux
├── SETUP.bat              ← Automated setup
├── src/                   ← Source code
├── target/                ← Build output (created after first run)
│   └── doantn-0.1.0-SNAPSHOT.jar  ← The app JAR file
└── [Other project files]
```

---

## 🎓 Project Details

| Feature | Details |
|---------|---------|
| **Language** | Java 17 |
| **Testing Framework** | JUnit 5 |
| **AST Analysis** | JavaParser |
| **Code Generation** | JavaPoet |
| **AI Integration** | Google Gemini (optional) |
| **Build Tool** | Maven 3.8+ |

---

## 📝 Next Steps

1. ✓ Install Maven
2. ✓ Run `START_APP.bat`
3. ✓ Select a Java file
4. ✓ Click "Generate Tests"
5. ✓ Check the generated test files
6. ✓ Run: `mvn test` to execute tests

---

## 💬 Questions?

Check these files for more info:
- `COMPLETE_SETUP_GUIDE.md` - Detailed setup instructions
- `README.md` - Project overview
- `APP_GUIDE.md` - Application features

---

**Enjoy using DoAnTN! 🎉**

Happy test generation! 🧪
