package com.doantn.generator;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Paths;
import java.util.List;

/**
 * Simple JavaPoet-based test generator. Given method metadata, generates a JUnit 5 test
 * that instantiates the class-under-test and calls the method, producing a sample
 * Assertions.assertEquals (or assertDoesNotThrow for void).
 */
public class JavaPoetTestGenerator {

    public static class ParamModel {
        public final String name;
        public final String type; // e.g. "int", "java.lang.String"

        public ParamModel(String name, String type) {
            this.name = name;
            this.type = type;
        }
    }

    public static class MethodModel {
        public final String name;
        public final String returnType; // e.g. "int" or "java.lang.String" or "void"
        public final List<ParamModel> params;

        public MethodModel(String name, String returnType, List<ParamModel> params) {
            this.name = name;
            this.returnType = returnType;
            this.params = params;
        }
    }

    /**
     * Generate a single test class file under outputDir.
     *
     * @param testPackage package for generated test class (e.g. com.doantn.tests)
     * @param classPackage package of class under test (e.g. com.doantn.app)
     * @param className simple name of class under test (e.g. App)
     * @param method metadata for the method to test
     * @param outputDir directory where generated .java will be written (e.g. "src/test/java")
     */
    public static void generateTest(String testPackage,
                                    String classPackage,
                                    String className,
                                    MethodModel method,
                                    String outputDir) throws Exception {

        ClassName classUnderTest = ClassName.get(classPackage, className);

        String testClassName = className + "Test";

        // Build test method
        AnnotationSpec testAnnotation = AnnotationSpec.builder(ClassName.get("org.junit.jupiter.api", "Test")).build();

        MethodSpec.Builder testMethod = MethodSpec.methodBuilder("test_" + method.name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(testAnnotation)
                .returns(void.class);

        // Instantiate class under test
        String instanceName = decapitalize(className);
        testMethod.addStatement("$T $L = new $T()", classUnderTest, instanceName, classUnderTest);

        // Prepare parameters with default values
        StringBuilder argList = new StringBuilder();
        for (int i = 0; i < method.params.size(); i++) {
            ParamModel p = method.params.get(i);
            TypeName pType = typeNameFromString(p.type);
            String varName = p.name;
            CodeBlock defaultVal = defaultValueForType(p.type);
            testMethod.addStatement("$T $L = $L", pType, varName, defaultVal);
            if (i > 0) argList.append(", ");
            argList.append(varName);
        }

        // Call method and assert based on return type
        String callExpr = instanceName + "." + method.name + "(" + argList.toString() + ")";

        if ("void".equals(method.returnType)) {
            // For void methods, assertDoesNotThrow
            testMethod.addStatement("$T.assertDoesNotThrow(() -> $L)",
                    ClassName.get("org.junit.jupiter.api", "Assertions"), callExpr);
        } else {
            TypeName retType = typeNameFromString(method.returnType);
            CodeBlock expectedVal = defaultValueForType(method.returnType);

            // declare expected
            testMethod.addStatement("$T expected = $L", retType, expectedVal);
            // call actual
            testMethod.addStatement("$T actual = $L", retType, callExpr);
            // Assertions.assertEquals(expected, actual)
            testMethod.addStatement("$T.assertEquals(expected, actual)",
                    ClassName.get("org.junit.jupiter.api", "Assertions"));
        }

        TypeSpec testClass = TypeSpec.classBuilder(testClassName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(testMethod.build())
                .build();

        JavaFile javaFile = JavaFile.builder(testPackage, testClass)
                .build();

        javaFile.writeTo(Paths.get(outputDir));
    }

    private static CodeBlock defaultValueForType(String type) {
        switch (type) {
            case "int": return CodeBlock.of("0");
            case "long": return CodeBlock.of("0L");
            case "double": return CodeBlock.of("0.0");
            case "float": return CodeBlock.of("0.0f");
            case "short": return CodeBlock.of("(short)0");
            case "byte": return CodeBlock.of("(byte)0");
            case "char": return CodeBlock.of("'\\u0000'");
            case "boolean": return CodeBlock.of("false");
            case "java.lang.String":
            case "String": return CodeBlock.of("\"\"" );
            default:
                // For objects, use null
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
                // best guess for fully-qualified or simple names
                return ClassName.bestGuess(type);
        }
    }

    private static String decapitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }
}
