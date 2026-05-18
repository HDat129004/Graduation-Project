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
 * AnalyzerService: entry point for parsing Java source and extracting semantic information using JavaParser + Symbol Solver.
 * Responsibilities:
 * - Parse source files into CompilationUnit
 * - Resolve symbols/types where necessary
 * - Provide simplified model objects (in `com.doantn.model`) for the generator
 */
public class AnalyzerService {

    public AnalyzerService() {
        // Initialize parser configuration if needed
    }

    /**
     * Analyze a single Java source file and extract class/method information
     * @param sourcePath Path to .java file
     * @return ClassModel containing extracted information
     */
    public ClassModel analyze(String sourcePath) {
        try {
            Path filePath = Paths.get(sourcePath);
            File file = filePath.toFile();

            if (!file.exists() || !file.isFile()) {
                System.err.println("File not found: " + sourcePath);
                return null;
            }

            try (FileInputStream in = new FileInputStream(file)) {
                CompilationUnit cu = StaticJavaParser.parse(in);

                // Get package name
                String packageName = cu.getPackageDeclaration()
                        .map(pd -> pd.getNameAsString())
                        .orElse("(default)");

                // Get class information
                var types = cu.getTypes();
                if (types.isEmpty()) {
                    System.out.println("No types found in file: " + sourcePath);
                    return null;
                }

                // Process first type (main class)
                var type = types.get(0);
                if (!(type instanceof ClassOrInterfaceDeclaration)) {
                    return null;
                }

                ClassOrInterfaceDeclaration classDecl = (ClassOrInterfaceDeclaration) type;
                String className = classDecl.getNameAsString();

                ClassModel classModel = new ClassModel(packageName, className, sourcePath);

                // Extract methods
                List<MethodDeclaration> methods = classDecl.getMethods();
                for (MethodDeclaration method : methods) {
                    if (method.isPublic()) {  // Only extract public methods
                        MethodModel methodModel = new MethodModel(
                                method.getNameAsString(),
                                method.getType().asString()
                        );

                        methodModel.setLineNumber(
                                method.getBegin().map(p -> p.line).orElse(-1)
                        );
                        methodModel.setPublic(true);
                        methodModel.setStatic(method.isStatic());

                        // Extract parameters
                        List<Parameter> params = method.getParameters();
                        for (Parameter param : params) {
                            ParameterModel paramModel = new ParameterModel(
                                    param.getNameAsString(),
                                    param.getType().asString()
                            );
                            methodModel.addParameter(paramModel);
                        }

                        // Extract branch conditions using ConditionCollector
                        List<ConditionCollector.BranchCondition> conditions = new ArrayList<>();
                        ConditionCollector collector = new ConditionCollector();
                        collector.visit(method, conditions);
                        for (ConditionCollector.BranchCondition bc : conditions) {
                            if (bc.kind == ConditionCollector.BranchCondition.Kind.IF) {
                                for (ConditionCollector.AtomicConstraint ac : bc.atomicConstraints) {
                                    boolean isParam = methodModel.getParameters().stream()
                                            .anyMatch(p -> p.getParamName().equals(ac.left));
                                    if (isParam) {
                                        com.doantn.model.BranchCondition modelBc = new com.doantn.model.BranchCondition(
                                                ac.left, ac.op, ac.right
                                        );
                                        modelBc.setExceptionType(bc.exceptionType);
                                        methodModel.addBranchCondition(modelBc);
                                    }
                                }
                            }
                        }

                        classModel.addMethod(methodModel);
                    }
                }

                System.out.println("[INFO] Analyzed: " + className + " with " + classModel.getMethods().size() + " public methods");
                return classModel;

            }
        } catch (Exception e) {
            System.err.println("Error analyzing file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Analyze all Java files in a directory recursively
     * @param sourceRoot Root directory to scan
     * @return List of ClassModels
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
            System.err.println("Error walking directory: " + e.getMessage());
        }
        return results;
    }
}
