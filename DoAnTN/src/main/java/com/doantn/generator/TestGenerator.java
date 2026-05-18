package com.doantn.generator;

import com.doantn.model.ClassModel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * TestGenerator: Mô-đun điều phối toàn bộ quá trình sinh mã kiểm thử tự động.
 *
 * <p>Trách nhiệm:
 * <ul>
 *   <li>Nhận các đối tượng Model đã được phân tích từ Analyzer.</li>
 *   <li>Sinh mã kiểm thử trực tiếp bằng StringBuilder (không dùng JavaPoet) theo 3 loại ca kiểm thử:
 *       giá trị biên mặc định, dữ liệu ngẫu nhiên, và phủ nhánh rẽ từ parser AST.</li>
 *   <li>Gọi AITestGenerator để sinh kiểm thử bổ sung bằng Trí tuệ Nhân tạo Gemini (tùy chọn).</li>
 * </ul>
 */
public class TestGenerator {

    public enum GenerationMode {
        AST_ONLY,
        AI_ONLY,
        BOTH
    }

    private final String outputDir;
    private final AITestGenerator aiGenerator;

    public TestGenerator(String outputDir, String apiKey) {
        this.outputDir = outputDir;
        this.aiGenerator = new AITestGenerator(outputDir, apiKey);
        
        // Tạo thư mục đầu ra nếu nó chưa tồn tại
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (Exception e) {
            System.err.println("[LỖI] Không thể tạo thư mục đầu ra: " + e.getMessage());
        }
    }

    /**
     * Sinh bộ ca kiểm thử cho một class đơn lẻ.
     *
     * @param classModel Mô hình class đã được phân tích từ mã nguồn gốc.
     * @param mode       Chế độ sinh test: chỉ AST, chỉ AI, hoặc kết hợp cả hai.
     */
    public void generateTests(ClassModel classModel, GenerationMode mode) {
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
            // Xác định tên gói (package) cho file kiểm thử
            // Nếu class gốc không có package (default) thì dùng package sinh mặc định
            String testPackage = packageName.equals("(default)") ?
                    "com.doantn.generated" :
                    packageName;

            if (mode == GenerationMode.AST_ONLY || mode == GenerationMode.BOTH) {
                generateDirectTestClass(
                        classModel,
                        testPackage,
                        outputDir
                );
                System.out.println("[OK] Đã sinh file test trực tiếp bằng AST & Random Data cho: " + className);
            }

            if (mode == GenerationMode.AI_ONLY || mode == GenerationMode.BOTH) {
                aiGenerator.generateTestUsingAI(
                        classModel.getFilePath(), 
                        className, 
                        packageName
                );
            }

        } catch (Exception e) {
            System.err.println("[LỖI] Không thể sinh test cho class " + className + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sinh bộ ca kiểm thử cho một danh sách nhiều class.
     *
     * @param classModels Danh sách các mô hình class cần sinh test.
     * @param mode        Chế độ sinh test.
     */
    public void generateTests(List<ClassModel> classModels, GenerationMode mode) {
        if (classModels == null || classModels.isEmpty()) {
            System.out.println("[CẢNH BÁO] Không có class nào để sinh test.");
            return;
        }

        System.out.println("[THÔNG TIN] Chuẩn bị sinh test cho " + classModels.size() + " class.");
        for (ClassModel classModel : classModels) {
            generateTests(classModel, mode);
        }
        System.out.println("[THÔNG TIN] Hoàn tất sinh test. Kết quả được lưu tại: " + outputDir);
    }

    private void generateDirectTestClass(ClassModel classModel, String testPackage, String outputDir) throws Exception {
        String className = classModel.getClassName();
        String testClassName = className + "ASTTest";
        StringBuilder sb = new StringBuilder();

        sb.append("package ").append(testPackage).append(";\n\n");
        sb.append("import org.junit.jupiter.api.Assertions;\n");
        sb.append("import org.junit.jupiter.api.Test;\n");
        sb.append("import com.doantn.generator.RandomDataUtil;\n");
        if (!classModel.getPackageName().equals("(default)") && !classModel.getPackageName().equals(testPackage)) {
            sb.append("import ").append(classModel.getPackageName()).append(".").append(className).append(";\n");
        }
        sb.append("\n");
        sb.append("public class ").append(testClassName).append(" {\n\n");

        for (com.doantn.model.MethodModel method : classModel.getMethods()) {
            String mName = method.getMethodName();

            // ===== Ca kiểm thử 1: Giá trị biên / Mặc định =====
            // Mục đích: Kiểm tra hàm hoạt động đúng khi tham số mang giá trị mặc định (0, false, "").
            // Kỹ thuật: Phân tích giá trị biên (Boundary Value Analysis - BVA).
            sb.append("    @Test\n");
            sb.append("    public void test_").append(mName).append("_boundary() {\n");
            sb.append("        try {\n");
            sb.append("            ").append(className).append(" instance = new ").append(className).append("();\n");
            
            StringBuilder argsBoundary = new StringBuilder();
            for (int i = 0; i < method.getParameters().size(); i++) {
                com.doantn.model.ParameterModel p = method.getParameters().get(i);
                if (i > 0) argsBoundary.append(", ");
                argsBoundary.append(defaultValueForType(p.getParamType()));
            }
            
            String callBoundary = "instance." + mName + "(" + argsBoundary.toString() + ")";
            if ("void".equals(method.getReturnType())) {
                sb.append("            ").append(callBoundary).append(";\n");
            } else {
                sb.append("            var actual = ").append(callBoundary).append(";\n");
                sb.append("            Assertions.assertNotNull(actual);\n");
            }
            sb.append("        } catch (Exception e) {\n");
            sb.append("            // Bỏ qua ngoại lệ phát sinh, ghi nhận luồng thực thi cho độ phủ\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            // ===== Ca kiểm thử 2: Dữ liệu ngẫu nhiên (Random Testing) =====
            // Mục đích: Phát hiện lỗi tiềm ẩn với các tổ hợp đầu vào không lường trước.
            // Sử dụng: RandomDataUtil để sinh giá trị ngẫu nhiên theo từng kiểu dữ liệu.
            sb.append("    @Test\n");
            sb.append("    public void test_").append(mName).append("_random() {\n");
            sb.append("        try {\n");
            sb.append("            ").append(className).append(" instance = new ").append(className).append("();\n");
            
            StringBuilder argsRandom = new StringBuilder();
            for (int i = 0; i < method.getParameters().size(); i++) {
                com.doantn.model.ParameterModel p = method.getParameters().get(i);
                if (i > 0) argsRandom.append(", ");
                argsRandom.append(randomCallForType(p.getParamType()));
            }
            
            String callRandom = "instance." + mName + "(" + argsRandom.toString() + ")";
            if ("void".equals(method.getReturnType())) {
                sb.append("            ").append(callRandom).append(";\n");
            } else {
                sb.append("            var actual = ").append(callRandom).append(";\n");
                sb.append("            Assertions.assertNotNull(actual);\n");
            }
            sb.append("        } catch (Exception e) {\n");
            sb.append("            // Bỏ qua ngoại lệ phát sinh, ghi nhận luồng thực thi cho độ phủ\n");
            sb.append("        }\n");
            sb.append("    }\n\n");

            // ===== Ca kiểm thử 3: Phủ nhánh rẽ (Branch Coverage) =====
            // Mục đích: Đảm bảo mỗi nhánh if/else/throw trong hàm được thực thi ít nhất một lần.
            // Nguồn dữ liệu: Các điều kiện rẽ nhánh được ConditionCollector trích xuất từ AST.
            if (method.getBranchConditions() != null) {
                int branchIdx = 1;
                for (com.doantn.model.BranchCondition bc : method.getBranchConditions()) {
                    sb.append("    @Test\n");
                    sb.append("    public void test_").append(mName).append("_branch_").append(branchIdx++).append("() {\n");
                    sb.append("        // Nhánh điều kiện: ").append(bc.getParamName()).append(" ").append(bc.getOperator()).append(" ").append(bc.getBoundaryValue()).append("\n");
                    
                    if (bc.getExceptionType() != null) {
                        String excClass = bc.getExceptionType().contains(".") ? bc.getExceptionType() : "java.lang." + bc.getExceptionType();
                        sb.append("        Assertions.assertThrows(").append(excClass).append(".class, () -> {\n");
                        sb.append("            ").append(className).append(" instance = new ").append(className).append("();\n");
                        
                        StringBuilder argsBranch = new StringBuilder();
                        for (int i = 0; i < method.getParameters().size(); i++) {
                            com.doantn.model.ParameterModel p = method.getParameters().get(i);
                            if (i > 0) argsBranch.append(", ");
                            if (p.getParamName().equals(bc.getParamName())) {
                                argsBranch.append(formatLiteral(p.getParamType(), bc.getBoundaryValue()));
                            } else {
                                argsBranch.append(defaultValueForType(p.getParamType()));
                            }
                        }
                        sb.append("            instance.").append(mName).append("(").append(argsBranch.toString()).append(");\n");
                        sb.append("        });\n");
                    } else {
                        sb.append("        try {\n");
                        sb.append("            ").append(className).append(" instance = new ").append(className).append("();\n");
                        StringBuilder argsBranch = new StringBuilder();
                        for (int i = 0; i < method.getParameters().size(); i++) {
                            com.doantn.model.ParameterModel p = method.getParameters().get(i);
                            if (i > 0) argsBranch.append(", ");
                            if (p.getParamName().equals(bc.getParamName())) {
                                argsBranch.append(formatLiteral(p.getParamType(), bc.getBoundaryValue()));
                            } else {
                                argsBranch.append(defaultValueForType(p.getParamType()));
                            }
                        }
                        String callBranch = "instance." + mName + "(" + argsBranch.toString() + ")";
                        if ("void".equals(method.getReturnType())) {
                            sb.append("            ").append(callBranch).append(";\n");
                        } else {
                            sb.append("            var actual = ").append(callBranch).append(";\n");
                            sb.append("            Assertions.assertNotNull(actual);\n");
                        }
                        sb.append("        } catch (Exception e) {\n");
                        sb.append("            // Bỏ qua ngoại lệ, ghi nhận nhánh đã được thực thi\n");
                        sb.append("        }\n");
                    }
                    sb.append("    }\n\n");
                }
            }
        }

        sb.append("}\n");

        String packagePath = testPackage.replace('.', '/');
        java.nio.file.Path targetDir = java.nio.file.Paths.get(outputDir, packagePath);
        java.nio.file.Files.createDirectories(targetDir);
        java.nio.file.Path targetFile = targetDir.resolve(testClassName + ".java");
        java.nio.file.Files.writeString(targetFile, sb.toString(), java.nio.charset.StandardCharsets.UTF_8);
    }

    /**
     * Trả về giá trị mặc định (biên) cho từng kiểu dữ liệu nguyên thủy và String.
     * Dùng cho Ca kiểm thử số 1 (Boundary Value Analysis).
     */
    private String defaultValueForType(String type) {
        switch (type) {
            case "int":    return "0";
            case "long":   return "0L";
            case "double": return "0.0";
            case "float":  return "0.0f";
            case "short":  return "(short)0";
            case "byte":   return "(byte)0";
            case "char":   return "'\\u0000'";
            case "boolean": return "false";
            case "String":
            case "java.lang.String": return "\"\"";
            default: return "null"; // Kiểu đối tượng phức tạp: truyền null
        }
    }

    /**
     * Trả về lời gọi phương thức RandomDataUtil tương ứng cho từng kiểu dữ liệu.
     * Dùng cho Ca kiểm thử số 2 (Random Testing).
     */
    private String randomCallForType(String type) {
        switch (type) {
            case "int":    return "RandomDataUtil.randomInt()";
            case "long":   return "RandomDataUtil.randomLong()";
            case "double": return "RandomDataUtil.randomDouble()";
            case "float":  return "RandomDataUtil.randomFloat()";
            case "short":  return "RandomDataUtil.randomShort()";
            case "byte":   return "RandomDataUtil.randomByte()";
            case "char":   return "RandomDataUtil.randomChar()";
            case "boolean": return "RandomDataUtil.randomBoolean()";
            case "String":
            case "java.lang.String": return "RandomDataUtil.randomString()";
            default: return "null"; // Kiểu phức tạp: không sinh ngẫu nhiên được
        }
    }

    /**
     * Định dạng giá trị hằng số theo cú pháp Java cho từng kiểu dữ liệu.
     * Ví dụ: long cần hậu tố "L", float cần "f", String cần dấu ngoặc kép.
     */
    private String formatLiteral(String type, String value) {
        if (value == null || value.equals("null")) return "null";
        // Kiểu String: đảm bảo có dấu ngoặc kép bao quanh
        if (type.equals("String") || type.equals("java.lang.String")) {
            if (value.startsWith("\"") && value.endsWith("\"")) return value;
            return "\"" + value.replace("\"", "\\\"") + "\"";
        }
        // Kiểu long: cần thêm hậu tố 'L'
        if (type.equals("long") && !value.endsWith("L")) return value + "L";
        // Kiểu float: cần thêm hậu tố 'f'
        if (type.equals("float") && !value.endsWith("f")) return value + "f";
        return value;
    }
}
