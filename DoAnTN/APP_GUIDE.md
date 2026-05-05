# 🚀 DoAnTN - Tự Động Sinh Unit Test cho Java

## ⚡ Khởi Động Nhanh

### Windows
Chỉ cần **double-click vào file**: `START_APP.bat`

### Mac/Linux
```bash
bash START_APP.sh
```

---

## 📋 Yêu Cầu Tiên Quyết

### 1. Java 17+ 
Kiểm tra: 
```bash
java -version
```
Nếu chưa có, download từ: https://www.oracle.com/java/technologies/downloads/

### 2. Maven 3.8+
Kiểm tra:
```bash
mvn -version
```

#### Cách Cài Đặt Maven

**Windows (sử dụng Chocolatey):**
```bash
choco install maven
```

**Windows (Manual):**
1. Download từ: https://maven.apache.org/download.cgi
2. Giải nén vào: `C:\Program Files\Maven`
3. Thêm `C:\Program Files\Maven\bin` vào biến môi trường PATH

**Mac:**
```bash
brew install maven
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get install maven
```

---

## 📱 Tính Năng Ứng Dụng

✅ **Giao Diện GUI Đơn Giản**
- Chọn file Java để phân tích
- Chọn thư mục output cho test
- Tùy chọn API key cho AI

✅ **Các Phương Pháp Sinh Test**
1. **AST Only**: Phân tích cấu trúc mã nguồn bằng Abstract Syntax Tree
2. **AI Only**: Sử dụng Google Gemini API để sinh test
3. **Compare AST + AI**: So sánh kết quả từ cả hai phương pháp

✅ **Kết Quả**
- Generate JUnit 5 test files tự động
- Tạo test cases cho tất cả methods
- Assertions được tạo tự động

---

## 🔧 Cách Sử Dụng

### Bước 1: Chạy Ứng Dụng
- **Windows**: Double-click `START_APP.bat`
- **Mac/Linux**: Run `bash START_APP.sh`

### Bước 2: Cấu Hình
1. Nhập **Source Path**: Đường dẫn tới file Java cần test (hoặc thư mục)
2. Nhập **Output Directory**: Nơi lưu test file (mặc định: `src/test/java`)
3. (Optional) Dán **Gemini API Key** nếu chọn chế độ AI

### Bước 3: Chọn Phương Pháp
- **AST only**: Nhanh, không cần API, dựa trên phân tích cấu trúc
- **AI only**: Cần API key, kết quả thông minh hơn
- **Compare AST + AI**: So sánh cả hai (cần API key)

### Bước 4: Sinh Test
Click nút **"Generate Tests"** để bắt đầu

---

## 🆘 Troubleshooting

### Lỗi: "Java is not installed"
- ✅ Cài Java 17+ từ https://www.oracle.com/java/technologies/downloads/
- ✅ Khởi động lại máy tính
- ✅ Chạy lại ứng dụng

### Lỗi: "Maven is not installed"
Cài Maven theo hướng dẫn ở trên

### Lỗi: "Build failed"
```bash
# Thử build thủ công
mvn clean package -DskipTests
```

---

## 📧 Hỗ Trợ

Nếu gặp vấn đề, vui lòng kiểm tra:
1. Phiên bản Java: `java -version`
2. Phiên bản Maven: `mvn -version`
3. Đường dẫn project hợp lệ
4. Quyền ghi vào thư mục output

---

**Chúc bạn sử dụng DoAnTN vui vẻ!** 🎉
