package com.doantn.app;

import com.doantn.analyzer.AnalyzerService;
import com.doantn.generator.TestGenerator;
import com.doantn.model.ClassModel;

import java.io.File;
import java.util.List;

/**
 * App: Main entry point for the DoAnTN test generation tool.
 */
public class App {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("DoAnTN - Auto-generate Unit Tests v0.1.0");
        System.out.println("========================================");
        System.out.println();

        String sourcePath;
        String outputDir;
        String apiKey = System.getenv("GEMINI_API_KEY") != null ? System.getenv("GEMINI_API_KEY") : "";
        String modeArg = "both";

        // DEBUG: Print received arguments
        System.out.println("[DEBUG] Arguments received: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("[DEBUG] Arg[" + i + "]: " + args[i]);
        }

        if (args.length > 0) {
            sourcePath = args[0];
            outputDir = args.length > 1 ? args[1] : "src/test/java";
            if (args.length > 2) {
                apiKey = args[2];
            }
            if (args.length > 3) {
                modeArg = args[3];
            }
        } else {
            // Fallback defaults if no arguments provided
            System.out.println("[WARN] No arguments provided. Using default example configuration.");
            // FIX: Make the default path absolute or relative to the DoAnTN module, 
            // handling the case where IDE runs from the root "Graduation-Project" directory.
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
        // Try to resolve relative path if file doesn't exist
        if (!sourceFile.exists()) {
            File currentDir = new File(".");
            System.out.println("[INFO] Current working directory: " + currentDir.getAbsolutePath());
            
            // Try absolute path based on current dir
            sourceFile = new File(currentDir, sourcePath);
        }

        if (!sourceFile.exists()) {
            System.err.println("[ERROR] Source path does not exist: " + sourcePath);
            System.err.println("[ERROR] Absolute path tried: " + sourceFile.getAbsolutePath());
            
            // Final desperate attempt for IDEs running from parent project root
            File desperateAttempt = new File(System.getProperty("user.dir") + "/DoAnTN/" + sourcePath.replace("DoAnTN/", ""));
            if(desperateAttempt.exists()) {
                 System.out.println("[INFO] Found file using fallback path: " + desperateAttempt.getAbsolutePath());
                 sourceFile = desperateAttempt;
            } else {
                 printUsage();
                 System.exit(2);
            }
        }

        try {
            AnalyzerService analyzer = new AnalyzerService();
            TestGenerator generator = new TestGenerator(outputDir, apiKey);
            TestGenerator.GenerationMode mode = parseMode(modeArg);

            System.out.println("[INFO] Analyzing source: " + sourceFile.getAbsolutePath());
            System.out.println("[INFO] Output directory: " + outputDir);
            System.out.println("[INFO] Generation mode: " + mode);
            if (!apiKey.isBlank()) {
                System.out.println("[INFO] API key supplied for AI generation.");
            } else if (mode != TestGenerator.GenerationMode.AST_ONLY) {
                System.out.println("[WARN] No API key supplied. AI generation may fail.");
            }
            System.out.println();

            if (sourceFile.isFile() && sourceFile.getName().endsWith(".java")) {
                // Single file analysis
                analyzeAndGenerateForFile(sourceFile.getAbsolutePath(), analyzer, generator, mode);
            } else if (sourceFile.isDirectory()) {
                // Directory analysis
                analyzeAndGenerateForDirectory(sourceFile.getAbsolutePath(), analyzer, generator, mode);
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

    public static void generateTests(String sourcePath, String outputDir, String apiKey, TestGenerator.GenerationMode mode) throws Exception {
        AnalyzerService analyzer = new AnalyzerService();
        TestGenerator generator = new TestGenerator(outputDir, apiKey);

        System.out.println("[INFO] Analyzing source: " + sourcePath);
        System.out.println("[INFO] Output directory: " + outputDir);
        System.out.println("[INFO] Generation mode: " + mode);
        if (!apiKey.isBlank()) {
            System.out.println("[INFO] API key supplied for AI generation.");
        } else if (mode != TestGenerator.GenerationMode.AST_ONLY) {
            System.out.println("[WARN] No API key supplied. AI generation may fail.");
        }
        System.out.println();

        File sourceFile = new File(sourcePath);
        if (sourceFile.isFile() && sourceFile.getName().endsWith(".java")) {
            // Single file analysis
            analyzeAndGenerateForFile(sourcePath, analyzer, generator, mode);
        } else if (sourceFile.isDirectory()) {
            // Directory analysis
            analyzeAndGenerateForDirectory(sourcePath, analyzer, generator, mode);
        } else {
            throw new IllegalArgumentException("Invalid source path. Must be a .java file or directory.");
        }
    }

    private static void analyzeAndGenerateForFile(String filePath, AnalyzerService analyzer, TestGenerator generator, TestGenerator.GenerationMode mode) {
        ClassModel classModel = analyzer.analyze(filePath);
        if (classModel != null) {
            generator.generateTests(classModel, mode);
        }
    }

    private static void analyzeAndGenerateForDirectory(String dirPath, AnalyzerService analyzer, TestGenerator generator, TestGenerator.GenerationMode mode) {
        List<ClassModel> classModels = analyzer.analyzeDirectory(dirPath);
        if (!classModels.isEmpty()) {
            System.out.println("[INFO] Found " + classModels.size() + " Java classes to analyze");
            System.out.println();
            generator.generateTests(classModels, mode);
        } else {
            System.out.println("[WARN] No Java source files found in: " + dirPath);
        }
    }

    private static TestGenerator.GenerationMode parseMode(String modeArg) {
        if ("ast".equalsIgnoreCase(modeArg)) {
            return TestGenerator.GenerationMode.AST_ONLY;
        }
        if ("ai".equalsIgnoreCase(modeArg)) {
            return TestGenerator.GenerationMode.AI_ONLY;
        }
        return TestGenerator.GenerationMode.BOTH;
    }

    private static void printUsage() {
        System.out.println("Usage: java -jar doantn.jar <source-file-or-directory> [output-directory] [api-key] [mode]");
        System.out.println();
        System.out.println("Arguments:");
        System.out.println("  <source-file-or-directory>  Path to a single .java file or source directory");
        System.out.println("  [output-directory]          Output directory for generated tests (default: src/test/java)");
        System.out.println("  [api-key]                   Optional Gemini API key for AI generation");
        System.out.println("  [mode]                      Optional mode: ast, ai, both (default both)");
    }
}

