# Hướng Dẫn Chạy Project DoAnTN

## 📋 Yêu Cầu Hệ Thống

- **Java**: JDK 17 trở lên
- **Maven**: 3.6.0 trở lên
- **Git**: (tùy chọn, cho việc version control)

## ✅ Kiểm Tra Cài Đặt

Mở Command Prompt hoặc PowerShell và chạy:

```bash
# Kiểm tra Java
java -version

# Kiểm tra Maven
mvn --version
```

Cả hai lệnh phải hiển thị version mà không lỗi.

## 🚀 Chạy Project

### Cách 1: Từ Command Line (Khuyến Nghị)

```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN

# Clean (xóa build cũ)
mvn clean

# Compile code
mvn compile

# Chạy tests
mvn test

# Build JAR file
mvn package
```

### Cách 2: Chạy Toàn Bộ Cùng Lúc

```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN
mvn clean compile test package
```

### Cách 3: Sử Dụng Batch Script (Windows)

```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN
check_build.bat
```

### Cách 4: Từ IntelliJ IDEA

1. Mở project DoAnTN
2. View → Tool Windows → Maven
3. Expand "DoAnTN" → Lifecycle
4. Double-click: clean → compile → test → package

### Cách 5: Chạy Các Tool Cụ Thể

**Chạy MethodInspector để phân tích file Java:**

```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN
mvn exec:java -Dexec.mainClass="com.doantn.analyzer.MethodInspector" -Dexec.args="C:\path\to\YourFile.java"
```

**Chạy ConditionCollectorRunner để phân tích điều kiện:**

```bash
cd C:\Users\Admin\Desktop\DATN\DoAnTN
mvn exec:java -Dexec.mainClass="com.doantn.analyzer.ConditionCollectorRunner" -Dexec.args="C:\path\to\YourFile.java"
```

## 📊 Expected Output

### Khi chạy `mvn clean compile test`

```
[INFO] Scanning for projects...
[INFO] Building DoAnTN - Tự động sinh Unit Test cho Java 0.1.0-SNAPSHOT
...
[INFO] BUILD SUCCESS
[INFO] Total time: XX.XXX s
```

### Khi chạy tests

```
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running com.doantn.AppTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.doantn.analyzer.MethodInspectorTest
[INFO] Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] BUILD SUCCESS
```

## 🐛 Troubleshooting

### Lỗi: "maven command not found"
- Maven chưa được cài đặt hoặc chưa được thêm vào PATH
- Cài Apache Maven từ https://maven.apache.org/download.cgi
- Thêm `\apache-maven-X.X.X\bin` vào System Environment Variables PATH

### Lỗi: "Compilation Failure"
- Kiểm tra Java version: `java -version`
- Phải là JDK 17 trở lên
- Nếu là JDK cũ, cập nhật JAVA_HOME environment variable

### Lỗi: "Dependencies not found"
- Kiểm tra internet connection
- Maven sẽ tự động download dependencies từ central repository
- Có thể mất vài phút lần đầu

### Build success nhưng tests fail
- Đọc lỗi chi tiết từ output
- Mỗi test có thể có assertion riêng
- Sửa code theo error message

## 📦 Output Files

Sau khi build thành công:

```
target/
├── classes/          # Compiled .class files
├── test-classes/     # Compiled test files
├── doantn-0.1.0-SNAPSHOT.jar  # JAR file
└── ...
```

## 📝 Next Steps

Sau khi build pass:

1. **Implement App.main()**
   - Đọc input path từ arguments
   - Gọi analyzer
   - Gọi generator

2. **Implement AnalyzerService**
   - Khởi tạo parser
   - Parse source files
   - Return model objects

3. **Thêm thêm tests**
   - Test ConditionCollector
   - Test JavaPoetTestGenerator

4. **Deploy**
   - `mvn package` tạo JAR
   - `java -jar target/doantn-0.1.0-SNAPSHOT.jar <args>`

## 🔗 References

- JavaParser Docs: https://javaparser.org/
- JavaPoet Docs: https://square.github.io/javapoet/
- Maven Guide: https://maven.apache.org/guides/

---

**Status**: ✅ Ready to Build and Run

