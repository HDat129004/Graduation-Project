package com.doantn.analyzer;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Runner that sets up a basic SymbolSolver and runs ConditionCollector on a file.
 * Usage: java -cp ... com.doantn.analyzer.ConditionCollectorRunner <path-to-file> [<src-root>]
 */
public class ConditionCollectorRunner {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: ConditionCollectorRunner <path-to-.java> [src-root]");
            System.exit(1);
        }

        Path filePath = Paths.get(args[0]);
        Path srcRoot = (args.length >= 2) ? Paths.get(args[1]) : Paths.get("src/main/java");

        // Configure a basic CombinedTypeSolver: Reflection + source (if exists)
        CombinedTypeSolver typeSolver = new CombinedTypeSolver();
        typeSolver.add(new ReflectionTypeSolver());
        if (srcRoot.toFile().exists()) {
            try {
                typeSolver.add(new JavaParserTypeSolver(srcRoot.toFile()));
            } catch (Exception e) {
                System.err.println("Warning: failed to add JavaParserTypeSolver for " + srcRoot + ": " + e.getMessage());
            }
        }

        JavaSymbolSolver symbolSolver = new JavaSymbolSolver(typeSolver);
        StaticJavaParser.getConfiguration().setSymbolResolver(symbolSolver);

        List<ConditionCollector.BranchCondition> collector = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
            CompilationUnit cu = StaticJavaParser.parse(fis);
            ConditionCollector visitor = new ConditionCollector();
            visitor.visit(cu, collector);
        }

        System.out.println("Collected " + collector.size() + " branch conditions:\n");
        for (ConditionCollector.BranchCondition bc : collector) {
            System.out.println(bc.toString());
            System.out.println();
        }
    }
}
