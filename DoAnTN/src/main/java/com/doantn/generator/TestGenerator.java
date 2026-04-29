package com.doantn.generator;

import com.doantn.model.ClassModel;
import com.doantn.model.MethodModel;
import com.doantn.model.ParameterModel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * TestGenerator: uses JavaPoet to create Java test sources based on the analyzed model.
 * Responsibilities:
 * - Accept model objects (from analyzer)
 * - Build JUnit test classes (methods, imports, assertions)
 * - Emit .java files under a configurable output directory (e.g. target/generated-tests)
 */
public class TestGenerator {

    private String outputDir;

    public TestGenerator() {
        this("target/generated-tests");
    }

    public TestGenerator(String outputDir) {
        this.outputDir = outputDir;
        // Create output directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (Exception e) {
            System.err.println("Failed to create output directory: " + e.getMessage());
        }
    }

    /**
     * Generate tests for a ClassModel
     * @param classModel The analyzed class model
     */
    public void generateTests(ClassModel classModel) {
        if (classModel == null) {
            System.err.println("ClassModel is null");
            return;
        }

        String packageName = classModel.getPackageName();
        String className = classModel.getClassName();
        List<MethodModel> methods = classModel.getMethods();

        System.out.println("[INFO] Generating tests for: " + className);

        if (methods.isEmpty()) {
            System.out.println("[WARN] No public methods found to test in: " + className);
            return;
        }

        for (MethodModel method : methods) {
            try {
                String testPackage = packageName.equals("(default)") ?
                        "com.doantn.generated.tests" :
                        packageName + ".tests";

                // Convert model to JavaPoetTestGenerator format
                var params = convertParameters(method.getParameters());

                JavaPoetTestGenerator.generateTest(
                        testPackage,
                        packageName,
                        className,
                        new JavaPoetTestGenerator.MethodModel(
                                method.getMethodName(),
                                method.getReturnType(),
                                params
                        ),
                        outputDir
                );

                System.out.println("[OK] Generated test for: " + className + "." + method.getMethodName());

            } catch (Exception e) {
                System.err.println("[ERROR] Failed to generate test for " + method.getMethodName() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Generate tests for multiple classes
     * @param classModels List of analyzed class models
     */
    public void generateTests(List<ClassModel> classModels) {
        if (classModels == null || classModels.isEmpty()) {
            System.out.println("[WARN] No class models to generate tests for");
            return;
        }

        System.out.println("[INFO] Generating tests for " + classModels.size() + " classes");
        for (ClassModel classModel : classModels) {
            generateTests(classModel);
        }
        System.out.println("[INFO] Test generation completed. Output: " + outputDir);
    }

    private List<JavaPoetTestGenerator.ParamModel> convertParameters(List<ParameterModel> params) {
        return params.stream()
                .map(p -> new JavaPoetTestGenerator.ParamModel(p.getParamName(), p.getParamType()))
                .toList();
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }
}
