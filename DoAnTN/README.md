# DoAnTN - Công Cụ Tự Động Sinh Unit Test cho Java

## 🎯 Đề Tài

**Tiêu Đề:**
```
Nghiên cứu và xây dựng công cụ tự động sinh Unit Test 
dựa trên phân tích cấu trúc mã nguồn cho ngôn ngữ Java
```

## 📝 Mô Tả

DoAnTN là một công cụ hiện đại để **tự động sinh Unit Test** cho các class Java bằng cách **phân tích cấu trúc mã nguồn** sử dụng Abstract Syntax Tree (AST).

## ✨ Tính Năng Chính

✅ **Phân Tích Mã Nguồn Java**
- Parse Java source files using JavaParser
- Extract class, method, parameter information
- Analyze code structure and conditions (IF, SWITCH, FOR)

✅ **Sinh Test Tự Động**
- Generate JUnit 5 test files automatically
- Support multiple parameter types (primitives, String, objects)
- Automatic assertion generation

✅ **CLI Tool**
- Simple command-line interface
- Support single file and directory analysis
- Configurable output directory

✅ **Production Ready**
- Complete implementation
- Comprehensive documentation
- Unit & integration tests

## 🚀 Quick Start

**Windows:**
```bash
run_test_generation.bat
```

**Linux/Mac:**
```bash
bash run_test_generation.sh
```

Or manually:
```bash
mvn exec:java -Dexec.mainClass="com.doantn.app.App" \
  -Dexec.args="src/main/java/com/doantn/example/Calculator.java"
```

## �️ Graphical User Interface (GUI)

DoAnTN now includes a user-friendly GUI for easier interaction.

**To run the GUI:**
```bash
# Build the project first
mvn clean package

# Run with GUI
java -jar target/doantn-0.1.0-SNAPSHOT.jar --gui
```

The GUI provides:
- Browse buttons for selecting source files/directories
- Input fields for source path and output directory
- Real-time output display
- One-click test generation

## �📚 Documentation

- **[QUICK_START.md](QUICK_START.md)** - Quick start (2 minutes)
- **[TEST_GENERATION_GUIDE.md](TEST_GENERATION_GUIDE.md)** - Complete guide
- **[THESIS_ALIGNMENT_VERIFICATION.md](THESIS_ALIGNMENT_VERIFICATION.md)** - Verify thesis alignment
- **[INDEX.md](INDEX.md)** - Documentation index

## 🛠 Technologies

- **Language:** Java 17
- **AST Analysis:** JavaParser 3.25.4
- **Code Generation:** JavaPoet 1.13.0
- **Test Framework:** JUnit 5
- **Build Tool:** Maven

## ✅ Status

**Overall:** ✅ Production Ready

See [THESIS_ALIGNMENT_VERIFICATION.md](THESIS_ALIGNMENT_VERIFICATION.md) for complete verification.
