package com.doantn.app;

import com.doantn.analyzer.AnalyzerService;
import com.doantn.generator.TestGenerator;
import com.doantn.model.ClassModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * App: Main entry point for the DoAnTN test generation tool.
 *
 * Usage:
 *   java -jar doantn.jar <source-file-or-directory> [output-directory]
 *
 * Examples:
 *   java -jar doantn.jar src/main/java/com/example/MyClass.java
 *   java -jar doantn.jar src/main/java target/generated-tests
 */
public class App {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("DoAnTN - Auto-generate Unit Tests v0.1.0");
        System.out.println("========================================");
        System.out.println();

        if (args.length == 0) {
            printUsage();
            System.exit(1);
        }

        String sourcePath = args[0];
        String outputDir = args.length > 1 ? args[1] : "target/generated-tests";

        File sourceFile = new File(sourcePath);

        if (!sourceFile.exists()) {
            System.err.println("[ERROR] Source path does not exist: " + sourcePath);
            System.exit(2);
        }

        try {
            AnalyzerService analyzer = new AnalyzerService();
            TestGenerator generator = new TestGenerator(outputDir);

            System.out.println("[INFO] Analyzing source: " + sourcePath);
            System.out.println("[INFO] Output directory: " + outputDir);
            System.out.println();

            if (sourceFile.isFile() && sourcePath.endsWith(".java")) {
                // Single file analysis
                analyzeAndGenerateForFile(sourcePath, analyzer, generator);
            } else if (sourceFile.isDirectory()) {
                // Directory analysis
                analyzeAndGenerateForDirectory(sourcePath, analyzer, generator);
            } else {
                System.err.println("[ERROR] Invalid source path. Must be a .java file or directory.");
                System.exit(3);
            }

            System.out.println();
            System.out.println("========================================");
            System.out.println("[SUCCESS] Test generation completed!");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("[ERROR] Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }

    private static void analyzeAndGenerateForFile(String filePath, AnalyzerService analyzer, TestGenerator generator) {
        ClassModel classModel = analyzer.analyze(filePath);
        if (classModel != null) {
            generator.generateTests(classModel);
        }
    }

    private static void analyzeAndGenerateForDirectory(String dirPath, AnalyzerService analyzer, TestGenerator generator) {
        List<ClassModel> classModels = analyzer.analyzeDirectory(dirPath);
        if (!classModels.isEmpty()) {
            System.out.println("[INFO] Found " + classModels.size() + " Java classes to analyze");
            System.out.println();
            generator.generateTests(classModels);
        } else {
            System.out.println("[WARN] No Java source files found in: " + dirPath);
        }
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar doantn.jar <source-file-or-directory> [output-directory]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <source-file-or-directory>  Path to a single .java file or source directory");
        System.out.println("  [output-directory]          Output directory for generated tests (default: target/generated-tests)");
        System.out.println();
        System.out.println("Examples:");
        System.out.println("  java -jar doantn.jar src/main/java/com/example/MyClass.java");
        System.out.println("  java -jar doantn.jar src/main/java target/generated-tests");
        System.out.println("  java -jar doantn.jar C:\\Project\\src");
    }
}
