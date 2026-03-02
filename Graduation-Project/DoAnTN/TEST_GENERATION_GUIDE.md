đã# Hướng Dẫn Sinh Test Case Tự Động

## 🎯 Tổng Quan

DoAnTN hiện đã có khả năng **sinh test case tự động** cho các Java class. Hệ thống sẽ:

1. **Phân tích** file Java source code
2. **Trích xuất** thông tin class và method
3. **Sinh** JUnit 5 test files tự động

## 📋 Cài Đặt

### Yêu Cầu
- Java 17+
- Maven 3.6.0+

### Cấu Trúc Dự Án Mới

```
DoAnTN/
├── src/
│   ├── main/java/com/doantn/
│   │   ├── app/
│   │   │   └── App.java                (Main entry point) ✨ NEW
│   │   ├── analyzer/
│   │   │   ├── AnalyzerService.java    ✨ UPDATED
│   │   │   ├── MethodInspector.java
│   │   │   ├── ConditionCollector.java
│   │   │   └── ConditionCollectorRunner.java
│   │   ├── generator/
│   │   │   ├── TestGenerator.java      ✨ UPDATED
│   │   │   └── JavaPoetTestGenerator.java
│   │   ├── model/
│   │   │   ├── ClassModel.java         ✨ NEW
│   │   │   ├── MethodModel.java        ✨ NEW
│   │   │   └── ParameterModel.java     ✨ NEW
│   │   ├── example/
│   │   │   └── Calculator.java         ✨ NEW (Example class)
│   │   └── utils/
│   ├── test/java/
│   │   └── com/doantn/
│   │       ├── TestGenerationTest.java ✨ NEW
│   │       ├── AppTest.java
│   │       └── analyzer/
│   │           └── MethodInspectorTest.java
│   └── ...
├── pom.xml
├── run_test_generation.bat             ✨ NEW (Windows)
├── run_test_generation.sh              ✨ NEW (Linux/Mac)
├── RUN_GUIDE.md                        (Hướng dẫn cũ)
└── ...
```

## 🚀 Cách Sử Dụng

### Cách 1: Chạy với File Lẻ

```bash
# Windows
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"

# Linux/Mac
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"
```

### Cách 2: Chạy với Folder

```bash
# Windows
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java target/generated-tests"

# Linux/Mac
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java target/generated-tests"
```

### Cách 3: Sử Dụng Script (Khuyến Nghị)

#### Windows
```bash
run_test_generation.bat
```

#### Linux/Mac
```bash
bash run_test_generation.sh
```

### Cách 4: Từ IDE (IntelliJ IDEA)

1. Mở `App.java`
2. Click chuột phải vào `main` method
3. Chọn "Run 'App.main()'"
4. Nhập arguments: `src/main/java/com/doantn/example/Calculator.java`

## 📊 Output Ví Dụ

Khi chạy với `Calculator.java`:

```
==========================================
DoAnTN - Auto-generate Unit Tests v0.1.0
==========================================

[INFO] Analyzing source: src/main/java/com/doantn/example/Calculator.java
[INFO] Output directory: target/generated-tests

[INFO] Analyzed: Calculator with 6 public methods

[INFO] Generating tests for: Calculator
[OK] Generated test for: Calculator.add
[OK] Generated test for: Calculator.subtract
[OK] Generated test for: Calculator.multiply
[OK] Generated test for: Calculator.divide
[OK] Generated test for: Calculator.isPositive
[OK] Generated test for: Calculator.getNumberType

==========================================
[SUCCESS] Test generation completed!
==========================================
```

## 📁 Output Files

Generated test files sẽ được lưu trong `target/generated-tests/`:

```
target/generated-tests/
├── com/
│   └── doantn/
│       └── example/
│           └── tests/
│               ├── CalculatorTest.java (for add method)
│               ├── CalculatorTest.java (for subtract method)
│               ├── CalculatorTest.java (for multiply method)
│               ├── CalculatorTest.java (for divide method)
│               ├── CalculatorTest.java (for isPositive method)
│               └── CalculatorTest.java (for getNumberType method)
```

## 💻 Dùng Lập Trình

### Cách sử dụng AnalyzerService

```java
import com.doantn.analyzer.AnalyzerService;
import com.doantn.model.ClassModel;

AnalyzerService analyzer = new AnalyzerService();

// Phân tích single file
ClassModel model = analyzer.analyze("src/main/java/com/example/MyClass.java");

// Phân tích directory
List<ClassModel> models = analyzer.analyzeDirectory("src/main/java");
```

### Cách sử dụng TestGenerator

```java
import com.doantn.generator.TestGenerator;

TestGenerator generator = new TestGenerator("target/generated-tests");

// Generate tests for single class
generator.generateTests(classModel);

// Generate tests for multiple classes
generator.generateTests(classModels);
```

### Ví Dụ Hoàn Chỉnh

```java
import com.doantn.analyzer.AnalyzerService;
import com.doantn.generator.TestGenerator;

public class MyTestGenerator {
    public static void main(String[] args) throws Exception {
        // 1. Tạo analyzer
        AnalyzerService analyzer = new AnalyzerService();
        
        // 2. Phân tích source code
        var classModel = analyzer.analyze("src/main/java/com/example/MyClass.java");
        
        // 3. Tạo generator
        TestGenerator generator = new TestGenerator("my-generated-tests");
        
        // 4. Sinh test files
        generator.generateTests(classModel);
        
        System.out.println("Done!");
    }
}
```

## 🧪 Chạy Generated Tests

Sau khi sinh test files, bạn có thể chạy chúng:

```bash
# Copy generated tests vào src/test/java
cp -r target/generated-tests/com src/test/java/

# Chạy tests
mvn test
```

## 🔍 Tính Năng

### ✅ Những Gì Đã Implement

- ✅ **AnalyzerService**: Phân tích Java source files sử dụng JavaParser
- ✅ **ClassModel, MethodModel, ParameterModel**: Domain models để lưu thông tin
- ✅ **TestGenerator**: Tự động sinh JUnit 5 test files bằng JavaPoet
- ✅ **App.java**: CLI tool để chạy full pipeline
- ✅ **Support single file và directory analysis**
- ✅ **Tự động generate default test values cho các types**
- ✅ **Support primitive types, String, và custom objects**

### 📋 TODO (Phát triển thêm)

- [ ] Support private/protected/package-private methods
- [ ] Improved default value generation (mocking objects)
- [ ] Parameterized tests generation
- [ ] Branch coverage analysis
- [ ] Mock/Stub generation cho dependencies
- [ ] Template customization
- [ ] GUI tool
- [ ] IntelliJ IDEA plugin
- [ ] Eclipse plugin

## 🚨 Troubleshooting

### Lỗi: "File not found"
- Kiểm tra đường dẫn file/folder có tồn tại không
- Sử dụng absolute path hoặc relative path từ project root

### Lỗi: "No types found in file"
- File không chứa class declaration
- Kiểm tra file có đúng cấu trúc Java không

### Lỗi: "No public methods found"
- Class chỉ có private/protected methods
- Sinh test chỉ support public methods hiện tại

### Compilation failed
- Kiểm tra Java 17+ cài đặt: `java -version`
- Kiểm tra pom.xml dependencies valid

## 📚 Tham Khảo

- **JavaParser**: https://javaparser.org/
- **JavaPoet**: https://square.github.io/javapoet/
- **JUnit 5**: https://junit.org/junit5/

## 📝 Ví Dụ Thực Tế

### Ví dụ 1: Sinh test cho Calculator class

```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"
```

**Kết quả**: Test files được sinh trong `target/generated-tests/com/doantn/example/tests/`

### Ví dụ 2: Sinh test cho toàn bộ folder

```bash
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java my-tests"
```

**Kết quả**: Test files được sinh cho tất cả Java files trong `src/main/java` → output vào `my-tests/`

## ✅ Kiểm Tra Hoạt Động

```bash
# 1. Compile project
mvn clean compile

# 2. Chạy unit tests
mvn test

# 3. Sinh test cho example class
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"

# 4. Xem files được sinh
dir target\generated-tests /s
# hoặc (Linux)
ls -R target/generated-tests
```

---

**Status**: ✅ Chức năng sinh test tự động đã hoàn thành!

