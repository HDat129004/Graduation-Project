# 🚀 Quick Start - Sinh Test Tự Động

## ⚡ Bắt Đầu Nhanh (2 phút)

### Windows
```bash
# 1. Mở Command Prompt
# 2. Di chuyển vào project
cd C:\Users\Admin\Desktop\DATN\DoAnTN

# 3. Chạy script
run_test_generation.bat
```

### Linux/Mac
```bash
# 1. Mở Terminal
# 2. Di chuyển vào project
cd ~/Desktop/DATN/DoAnTN

# 3. Chạy script
bash run_test_generation.sh
```

## 📝 Cách Sử Dụng

### Cách 1: Sinh test cho 1 file (Đơn giản nhất)

```bash
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"
```

**Output**: Test files trong `target/generated-tests/`

### Cách 2: Sinh test cho toàn bộ folder

```bash
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java target/my-tests"
```

### Cách 3: Từ IDE (IntelliJ)

1. Mở `src/main/java/com/doantn/app/App.java`
2. Click phải → Run 'App.main()'
3. Nhập arguments:
   ```
   src/main/java/com/doantn/example/Calculator.java
   ```

## 🎯 Ví Dụ Thực Tế

**Folder structure:**
```
DoAnTN/
├── src/main/java/com/doantn/
│   └── example/
│       └── Calculator.java         ← Class muốn test
└── target/
    └── generated-tests/            ← Output test files
```

**Chạy:**
```bash
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"
```

**Kết quả:**
```
target/generated-tests/
└── com/doantn/example/tests/
    ├── CalculatorTest.java  (test for add method)
    ├── CalculatorTest.java  (test for subtract method)
    ├── CalculatorTest.java  (test for multiply method)
    ├── CalculatorTest.java  (test for divide method)
    ├── CalculatorTest.java  (test for isPositive method)
    └── CalculatorTest.java  (test for getNumberType method)
```

## 🔍 Mỗi File Test Chứa

```java
public class CalculatorTest {
    @Test
    public void test_add() {
        Calculator calculator = new Calculator();
        int a = 0;
        int b = 0;
        int expected = 0;
        int actual = calculator.add(a, b);
        Assertions.assertEquals(expected, actual);
    }
}
```

## 📚 Documentation

- **Chi tiết đầy đủ**: `TEST_GENERATION_GUIDE.md`
- **Báo cáo kỹ thuật**: `AUTO_TEST_GENERATION_REPORT.txt`

## ✅ Kiểm Tra Hoạt Động

```bash
# 1. Build project
mvn clean compile

# 2. Run tests
mvn test

# 3. Generate tests cho Calculator
mvn exec:java -Dexec.mainClass="com.doantn.app.App" -Dexec.args="src/main/java/com/doantn/example/Calculator.java"

# 4. Check output
dir target\generated-tests /s
```

## 🎓 Có Thể Làm Gì

✅ Sinh test cho Java class tự động  
✅ Extract method information automatically  
✅ Generate JUnit 5 tests with assertions  
✅ Support primitive & custom types  
✅ Single file hoặc directory analysis  
✅ CLI tool ready to use  

## ❓ Câu Hỏi Thường Gặp

**Q: Tôi chạy script nhưng không thấy output?**  
A: Kiểm tra `target/generated-tests/` folder

**Q: Làm sao test được generate cho method của tôi?**  
A: Method phải là `public`. Private/protected methods không được hỗ trợ hiện tại.

**Q: Có thể tùy chỉnh output directory không?**  
A: Có! Cách 2 ở trên cho phép chỉ định folder.

**Q: Generated tests có chạy được ngay không?**  
A: Có! Chúng là JUnit 5 tests hoàn chỉnh với default assertions.

---

**Status**: ✅ Ready to use!

👉 **Bắt đầu**: Chạy `run_test_generation.bat` hoặc `bash run_test_generation.sh`

