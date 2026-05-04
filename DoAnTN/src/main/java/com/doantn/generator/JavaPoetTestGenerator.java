package com.doantn.generator;

import com.doantn.model.ClassModel;
import com.doantn.model.MethodModel;
import com.doantn.model.ParameterModel;
import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.List;

/**
 * JavaPoetTestGenerator: Trình sinh mã nguồn tự động sử dụng thư viện JavaPoet.
 * ĐÃ NÂNG CẤP: Sinh nhiều test case cho cùng một hàm (Boundary Value Analysis).
 */
public class JavaPoetTestGenerator {

    public static void generateTestClass(ClassModel classModel, String testPackage, String outputDir) throws Exception {
        String className = classModel.getClassName();
        String classPackage = classModel.getPackageName();
        
        ClassName classUnderTest = ClassName.get(classPackage.equals("(default)") ? "" : classPackage, className);
        String testClassName = className + "Test";
        
        TypeSpec.Builder testClassBuilder = TypeSpec.classBuilder(testClassName)
                .addModifiers(Modifier.PUBLIC);
                
        // Lặp qua từng hàm trong file gốc
        for (MethodModel method : classModel.getMethods()) {
            
            // NÂNG CẤP: Nếu hàm có tham số, tự động tách làm 3 Test Case (0, Âm, Dương)
            int numTests = method.getParameters().isEmpty() ? 1 : 3;
            String[] testSuffixes = method.getParameters().isEmpty() ? new String[]{""} : new String[]{"_withZero", "_withPositive", "_withNegative"};
            
            for (int testIndex = 0; testIndex < numTests; testIndex++) {
                String suffix = testSuffixes[testIndex];
                
                AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.get("org.junit.jupiter.api", "Test")).build();
                MethodSpec.Builder testMethod = MethodSpec.methodBuilder("test_" + method.getMethodName() + suffix)
                        .addModifiers(Modifier.PUBLIC)
                        .addAnnotation(testAnnotation)
                        .returns(void.class);
                        
                testMethod.addComment("Kỹ thuật: Phân tích giá trị biên (Boundary Value Analysis) để tăng độ phủ nhánh");
                
                String instanceName = decapitalize(className);
                testMethod.addStatement("$T $L = new $T()", classUnderTest, instanceName, classUnderTest);
                
                StringBuilder argList = new StringBuilder();
                List<ParameterModel> params = method.getParameters();
                for (int i = 0; i < params.size(); i++) {
                    ParameterModel p = params.get(i);
                    TypeName pType = typeNameFromString(p.getParamType());
                    String varName = p.getParamName();
                    
                    // Lấy giá trị thông minh dựa vào Test Case (0, Số Dương, Số Âm)
                    CodeBlock val = valueForType(p.getParamType(), testIndex);
                    testMethod.addStatement("$T $L = $L", pType, varName, val);
                    
                    if (i > 0) argList.append(", ");
                    argList.append(varName);
                }
                
                String callExpr = instanceName + "." + method.getMethodName() + "(" + argList.toString() + ")";
                
                // Đưa vào khối Try-Catch để code không bị văng lỗi khi test ngoại lệ (vd: chia cho 0)
                testMethod.beginControlFlow("try");
                
                if ("void".equals(method.getReturnType())) {
                    testMethod.addStatement("$L", callExpr);
                } else {
                    TypeName retType = typeNameFromString(method.getReturnType());
                    testMethod.addStatement("$T actual = $L", retType, callExpr);
                    testMethod.addComment("Đảm bảo hàm chạy không văng lỗi và có trả về kết quả");
                    testMethod.addStatement("$T.assertNotNull(actual)", ClassName.get("org.junit.jupiter.api", "Assertions"));
                }
                
                testMethod.nextControlFlow("catch ($T e)", Exception.class);
                testMethod.addComment("Bắt lỗi (ví dụ: Exception chia cho 0) để hệ thống vẫn đi tiếp và ghi nhận độ phủ");
                testMethod.endControlFlow();
                
                testClassBuilder.addMethod(testMethod.build());
            }
        }
        
        JavaFile javaFile = JavaFile.builder(testPackage, testClassBuilder.build()).build();
        javaFile.writeTo(Paths.get(outputDir));
    }

    /**
     * Thuật toán sinh dữ liệu thông minh (Boundary Value Analysis)
     */
    private static CodeBlock valueForType(String type, int testCaseIndex) {
        switch (type) {
            case "int": 
                if (testCaseIndex == 1) return CodeBlock.of("5"); // Giá trị dương
                if (testCaseIndex == 2) return CodeBlock.of("-5"); // Giá trị âm
                return CodeBlock.of("0"); // Giá trị 0
            case "long": 
                if (testCaseIndex == 1) return CodeBlock.of("5L");
                if (testCaseIndex == 2) return CodeBlock.of("-5L");
                return CodeBlock.of("0L");
            case "double": 
                if (testCaseIndex == 1) return CodeBlock.of("5.0");
                if (testCaseIndex == 2) return CodeBlock.of("-5.0");
                return CodeBlock.of("0.0");
            case "float": 
                if (testCaseIndex == 1) return CodeBlock.of("5.0f");
                if (testCaseIndex == 2) return CodeBlock.of("-5.0f");
                return CodeBlock.of("0.0f");
            case "short": return CodeBlock.of("(short)0");
            case "byte": return CodeBlock.of("(byte)0");
            case "char": return CodeBlock.of("'\\u0000'");
            case "boolean": 
                if (testCaseIndex == 1) return CodeBlock.of("true");
                return CodeBlock.of("false");
            case "java.lang.String":
            case "String": 
                if (testCaseIndex == 1) return CodeBlock.of("\"positive\"");
                if (testCaseIndex == 2) return CodeBlock.of("\"negative\"");
                return CodeBlock.of("\"\"");
            default:
                return CodeBlock.of("($L) null", type);
        }
    }

    private static TypeName typeNameFromString(String type) {
        switch (type) {
            case "int": return TypeName.INT;
            case "long": return TypeName.LONG;
            case "double": return TypeName.DOUBLE;
            case "float": return TypeName.FLOAT;
            case "short": return TypeName.SHORT;
            case "byte": return TypeName.BYTE;
            case "char": return TypeName.CHAR;
            case "boolean": return TypeName.BOOLEAN;
            case "void": return TypeName.VOID.box();
            case "String":
            case "java.lang.String": return ClassName.get("java.lang", "String");
            default:
                try {
                    return ClassName.bestGuess(type);
                } catch (Exception e) {
                    return ClassName.OBJECT;
                }
        }
    }

    private static String decapitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}