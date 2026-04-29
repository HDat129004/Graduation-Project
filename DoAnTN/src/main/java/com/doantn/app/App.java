package com.doantn.app;

import com.doantn.analyzer.AnalyzerService;
import com.doantn.generator.TestGenerator;
import com.doantn.model.ClassModel;

import java.io.File;
import java.util.List;

/**
 * App: Hàm main (Điểm khởi chạy chính) của công cụ DoAnTN.
 * 
 * Luồng hoạt động (Workflow):
 * 1. Nhận đường dẫn file mã nguồn từ giao diện dòng lệnh (CLI).
 * 2. Gọi AnalyzerService để đọc hiểu cấu trúc file đó.
 * 3. Truyền kết quả phân tích sang TestGenerator để tự động sinh code Unit Test.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("DoAnTN - Tự động sinh Unit Test v0.1.0");
        System.out.println("========================================");
        System.out.println();

        String sourcePath;
        String outputDir;

        // KIỂM TRA THAM SỐ ĐẦU VÀO (Arguments)
        // args[0] là file cần test, args[1] là thư mục lưu test sinh ra
        if (args.length > 0) {
            sourcePath = args[0];
            outputDir = args.length > 1 ? args[1] : "src/test/java";
        } else {
            // NẾU KHÔNG CÓ THAM SỐ: Chạy file ví dụ mặc định (Calculator.java)
            System.out.println("[CẢNH BÁO] Không có tham số. Sử dụng cấu hình ví dụ mặc định.");
            
            // Xử lý đường dẫn tương đối (phòng trường hợp người dùng chạy từ thư mục cha)
            File fallbackFile = new File("DoAnTN/src/main/java/com/doantn/example/Calculator.java");
            if (fallbackFile.exists()) {
                 sourcePath = "DoAnTN/src/main/java/com/doantn/example/Calculator.java";
                 outputDir = "DoAnTN/src/test/java";
            } else {
                 sourcePath = "src/main/java/com/doantn/example/Calculator.java";
                 outputDir = "src/test/java";
            }
        }

        File sourceFile = new File(sourcePath);
        
        // KIỂM TRA FILE TỒN TẠI HAY KHÔNG
        if (!sourceFile.exists()) {
            File currentDir = new File(".");
            sourceFile = new File(currentDir, sourcePath);
        }

        if (!sourceFile.exists()) {
            System.err.println("[LỖI] Không tìm thấy đường dẫn: " + sourcePath);
            
            // Cố gắng tìm lần cuối (Trường hợp IDE IntelliJ tự động nhảy ra thư mục cha)
            File desperateAttempt = new File(System.getProperty("user.dir") + "/DoAnTN/" + sourcePath.replace("DoAnTN/", ""));
            if(desperateAttempt.exists()) {
                 sourceFile = desperateAttempt;
            } else {
                 printUsage();
                 System.exit(2);
            }
        }

        // BẮT ĐẦU CHẠY CORE LOGIC (Phân tích & Sinh mã)
        try {
            // Khởi tạo 2 module lõi
            AnalyzerService analyzer = new AnalyzerService();
            TestGenerator generator = new TestGenerator(outputDir);

            System.out.println("[THÔNG TIN] Đang phân tích mã nguồn: " + sourceFile.getAbsolutePath());
            System.out.println("[THÔNG TIN] Thư mục đầu ra (Sinh Test): " + outputDir);
            System.out.println();

            // Nếu đầu vào là một file .java
            if (sourceFile.isFile() && sourceFile.getName().endsWith(".java")) {
                analyzeAndGenerateForFile(sourceFile.getAbsolutePath(), analyzer, generator);
            } 
            // Nếu đầu vào là một thư mục (quét toàn bộ file bên trong)
            else if (sourceFile.isDirectory()) {
                analyzeAndGenerateForDirectory(sourceFile.getAbsolutePath(), analyzer, generator);
            } else {
                System.err.println("[LỖI] Đường dẫn không hợp lệ. Phải là file .java hoặc thư mục.");
                System.exit(3);
            }

            System.out.println();
            System.out.println("========================================");
            System.out.println("[THÀNH CÔNG] Đã sinh Unit Test hoàn tất!");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("[LỖI] Lỗi không xác định: " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }

    /**
     * Hàm hỗ trợ: Xử lý cho 1 file duy nhất
     */
    private static void analyzeAndGenerateForFile(String filePath, AnalyzerService analyzer, TestGenerator generator) {
        // BƯỚC 1: Dùng Analyzer đọc file .java và chuyển thành Model
        ClassModel classModel = analyzer.analyze(filePath);
        
        // BƯỚC 2: Nếu đọc thành công, truyền Model cho Generator để viết code Test
        if (classModel != null) {
            generator.generateTests(classModel);
        }
    }

    /**
     * Hàm hỗ trợ: Xử lý cho cả 1 thư mục
     */
    private static void analyzeAndGenerateForDirectory(String dirPath, AnalyzerService analyzer, TestGenerator generator) {
        List<ClassModel> classModels = analyzer.analyzeDirectory(dirPath);
        if (!classModels.isEmpty()) {
            System.out.println("[THÔNG TIN] Đã tìm thấy " + classModels.size() + " lớp Java để phân tích");
            System.out.println();
            generator.generateTests(classModels);
        } else {
            System.out.println("[CẢNH BÁO] Không tìm thấy file .java nào trong: " + dirPath);
        }
    }

    /**
     * Hàm in ra hướng dẫn sử dụng công cụ
     */
    private static void printUsage() {
        System.out.println("Cách dùng: java -jar doantn.jar <đường-dẫn-file-java> [thư-mục-đầu-ra]");
        System.out.println("Ví dụ: java -jar doantn.jar src/main/java/com/doantn/example/Calculator.java src/test/java");
    }
}
