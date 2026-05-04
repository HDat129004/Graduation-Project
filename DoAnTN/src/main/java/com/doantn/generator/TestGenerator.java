package com.doantn.generator;

import com.doantn.model.ClassModel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * TestGenerator: Module điều phối việc sinh mã Test.
 * 
 * Trách nhiệm:
 * - Nhận các đối tượng Model từ Analyzer.
 * - Gọi đến JavaPoetTestGenerator để thực hiện việc viết code (Thuật toán AST).
 * - MỚI: Gọi đến AITestGenerator để thực hiện việc viết code bằng Trí tuệ nhân tạo (Gemini).
 */
public class TestGenerator {

    private final String outputDir;
    private final AITestGenerator aiGenerator;

    public TestGenerator(String outputDir) {
        this.outputDir = outputDir;
        this.aiGenerator = new AITestGenerator(outputDir);
        
        // Tạo thư mục đầu ra nếu nó chưa tồn tại
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (Exception e) {
            System.err.println("[LỖI] Không thể tạo thư mục đầu ra: " + e.getMessage());
        }
    }

    /**
     * Sinh test cho một ClassModel duy nhất.
     */
    public void generateTests(ClassModel classModel) {
        if (classModel == null) {
            System.err.println("[LỖI] ClassModel bị null, không thể sinh test.");
            return;
        }

        String packageName = classModel.getPackageName();
        String className = classModel.getClassName();

        System.out.println("[THÔNG TIN] Đang sinh bộ test cho Class: " + className);

        if (classModel.getMethods().isEmpty()) {
            System.out.println("[CẢNH BÁO] Không tìm thấy hàm public nào để test trong class: " + className);
            return;
        }

        try {
            // Xác định package cho file test
            String testPackage = packageName.equals("(default)") ?
                    "com.doantn.generated.tests" :
                    packageName + ".tests";

            // 1. SINH TEST BẰNG THUẬT TOÁN AST TRUYỀN THỐNG (JavaPoet)
            JavaPoetTestGenerator.generateTestClass(
                    classModel,
                    testPackage,
                    outputDir
            );
            System.out.println("[OK] Đã sinh file test bằng AST (JavaPoet) cho: " + className);

            // 2. SINH TEST BẰNG TRÍ TUỆ NHÂN TẠO (AI Gemini)
            // Tính năng này dùng để lấy kết quả so sánh với thuật toán truyền thống
            aiGenerator.generateTestUsingAI(
                    classModel.getFilePath(), 
                    className, 
                    packageName
            );

        } catch (Exception e) {
            System.err.println("[LỖI] Không thể sinh test cho class " + className + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sinh test cho một danh sách các Class.
     */
    public void generateTests(List<ClassModel> classModels) {
        if (classModels == null || classModels.isEmpty()) {
            System.out.println("[CẢNH BÁO] Không có class nào để sinh test.");
            return;
        }

        System.out.println("[THÔNG TIN] Chuẩn bị sinh test cho " + classModels.size() + " class.");
        for (ClassModel classModel : classModels) {
            generateTests(classModel);
        }
        System.out.println("[THÔNG TIN] Hoàn tất sinh test. Kết quả được lưu tại: " + outputDir);
    }
}
