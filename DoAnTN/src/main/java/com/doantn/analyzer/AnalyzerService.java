package com.doantn.analyzer;

import com.doantn.model.ClassModel;
import com.doantn.model.MethodModel;
import com.doantn.model.ParameterModel;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * AnalyzerService: Điểm vào (entry point) để phân tích mã nguồn Java và trích xuất thông tin ngữ nghĩa
 * sử dụng thư viện JavaParser + Symbol Solver.
 * 
 * Trách nhiệm của Class này:
 * - Đọc file mã nguồn (.java) và biến nó thành Cây Cú Pháp Trừu Tượng (CompilationUnit / AST).
 * - Lọc ra các thông tin quan trọng như Tên Lớp (Class), Tên Hàm (Method), Tham số (Parameters).
 * - Đóng gói các thông tin này vào các đối tượng Model (ClassModel, MethodModel) để trả về cho phần sinh Code.
 */
public class AnalyzerService {

    public AnalyzerService() {
        // Có thể cấu hình thêm các parser đặc biệt ở đây nếu cần trong tương lai
    }

    /**
     * Phân tích một file mã nguồn Java duy nhất và trích xuất thông tin class/method
     * 
     * @param sourcePath Đường dẫn tuyệt đối hoặc tương đối tới file .java
     * @return ClassModel Chứa toàn bộ thông tin đã trích xuất được (null nếu có lỗi)
     */
    public ClassModel analyze(String sourcePath) {
        try {
            Path filePath = Paths.get(sourcePath);
            File file = filePath.toFile();

            // BƯỚC 1: Kiểm tra tính hợp lệ của file đầu vào
            if (!file.exists() || !file.isFile()) {
                System.err.println("[LỖI] Không tìm thấy file: " + sourcePath);
                return null;
            }

            try (FileInputStream in = new FileInputStream(file)) {
                // BƯỚC 2: Gọi JavaParser để chuyển đổi Text thành Cây AST (Abstract Syntax Tree)
                // Cây AST này chứa mọi thông tin về cấu trúc của đoạn code.
                CompilationUnit cu = StaticJavaParser.parse(in);

                // BƯỚC 3: Trích xuất tên Package (Ví dụ: com.doantn.example)
                String packageName = cu.getPackageDeclaration()
                        .map(pd -> pd.getNameAsString())
                        .orElse("(default)");

                // Lấy danh sách các class/interface có trong file này
                var types = cu.getTypes();
                if (types.isEmpty()) {
                    System.out.println("[CẢNH BÁO] Không tìm thấy class nào trong file: " + sourcePath);
                    return null;
                }

                // BƯỚC 4: Lấy class đầu tiên (thường là public class chính của file)
                var type = types.get(0);
                if (!(type instanceof ClassOrInterfaceDeclaration)) {
                    // Bỏ qua nếu nó là Enum hoặc Record (phiên bản tool hiện tại chỉ hỗ trợ Class/Interface)
                    return null;
                }

                ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) type;
                String className = classDecl.getNameAsString();

                // Tạo đối tượng ClassModel để lưu trữ
                ClassModel classModel = new ClassModel(packageName, className, sourcePath);

                // BƯỚC 5: Trích xuất danh sách các hàm (Methods) trong Class
                List<MethodDeclaration> methods = classDecl.getMethods();
                for (MethodDeclaration method : methods) {
                    // CHÚ Ý: Tool hiện tại chỉ tập trung tự động sinh Test cho các hàm public
                    if (method.isPublic()) {  
                        MethodModel methodModel = new MethodModel(
                                method.getNameAsString(), // Lấy tên hàm (ví dụ: add)
                                method.getType().asString() // Lấy kiểu trả về (ví dụ: int)
                        );

                        // Lưu lại số dòng chứa hàm này (để phục vụ debug hoặc coverage sau này)
                        methodModel.setLineNumber(
                                method.getBegin().map(p -> p.line).orElse(-1)
                        );
                        methodModel.setPublic(true);
                        methodModel.setStatic(method.isStatic());

                        // BƯỚC 6: Trích xuất danh sách Tham Số (Parameters) của từng hàm
                        List<Parameter> params = method.getParameters();
                        for (Parameter param : params) {
                            ParameterModel paramModel = new ParameterModel(
                                    param.getNameAsString(), // Tên biến tham số (ví dụ: a)
                                    param.getType().asString() // Kiểu dữ liệu tham số (ví dụ: int)
                            );
                            methodModel.addParameter(paramModel);
                        }

                        // Thêm hàm vừa phân tích xong vào ClassModel
                        classModel.addMethod(methodModel);
                    }
                }

                System.out.println("[INFO] Phân tích thành công Class: " + className + " (Tìm thấy " + classModel.getMethods().size() + " hàm public)");
                
                // BƯỚC 7: Trả về kết quả cho App.java
                return classModel;

            }
        } catch (Exception e) {
            System.err.println("[LỖI] Có lỗi xảy ra trong quá trình phân tích file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Hàm tiện ích: Phân tích toàn bộ các file Java có trong một thư mục (đệ quy)
     * @param sourceRoot Đường dẫn tới thư mục cha
     * @return Danh sách các ClassModels đã trích xuất
     */
    public List<ClassModel> analyzeDirectory(String sourceRoot) {
        List<ClassModel> results = new ArrayList<>();
        try {
            Files.walk(Paths.get(sourceRoot))
                    .filter(path -> path.toString().endsWith(".java"))
                    .forEach(path -> {
                        ClassModel model = analyze(path.toString());
                        if (model != null) {
                            results.add(model);
                        }
                    });
        } catch (Exception e) {
            System.err.println("[LỖI] Có lỗi khi quét thư mục: " + e.getMessage());
        }
        return results;
    }
}
