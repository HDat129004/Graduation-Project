package com.doantn.analyzer;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.RecordDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * MethodInspector: Parse a single .java file and print all methods (name, return type, parameters)
 * in a simple tree structure for quick inspection.
 *
 * Usage (from project root):
 * mvn -Dexec.mainClass="com.doantn.analyzer.MethodInspector" -Dexec.args="/full/path/To/MyClass.java" exec:java
 */
public class MethodInspector {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: MethodInspector <path-to-.java-file>");
            System.exit(2);
        }

        Path filePath = Path.of(args[0]);
        File file = filePath.toFile();

        if (!file.exists() || !file.isFile()) {
            System.err.println("File not found: " + filePath);
            System.exit(3);
        }

        try (FileInputStream in = new FileInputStream(file)) {
            CompilationUnit cu = StaticJavaParser.parse(in);

            System.out.println("Source: " + file.getAbsolutePath());

            // Print top-level types and their methods
            List<TypeDeclaration<?>> types = cu.getTypes();
            if (types.isEmpty()) {
                System.out.println("(no top-level types found in the file)");
            }

            for (int ti = 0; ti < types.size(); ti++) {
                TypeDeclaration<?> td = types.get(ti);
                boolean isLastType = (ti == types.size() - 1);

                String typeKind = typeKind(td);
                printLine((isLastType ? "└─ " : "├─ ") + typeKind + ": " + td.getNameAsString());

                List<MethodDeclaration> methods = td.getMethods();
                if (methods.isEmpty()) {
                    printLine((isLastType ? "   └─ " : "│  └─ ") + "(no methods)");
                } else {
                    for (int mi = 0; mi < methods.size(); mi++) {
                        MethodDeclaration m = methods.get(mi);
                        boolean isLastMethod = (mi == methods.size() - 1);

                        String methodPrefix = (isLastType ? "   " : "│  ") + (isLastMethod ? "└─ " : "├─ ");
                        String methodSignature = m.getNameAsString() + " : " + m.getType().toString();
                        printLine(methodPrefix + methodSignature);

                        // Parameters
                        List<Parameter> params = m.getParameters();
                        if (params.isEmpty()) {
                            printLine((isLastType ? "      " : "│     ") + "└─ (no parameters)");
                        } else {
                            for (int pi = 0; pi < params.size(); pi++) {
                                Parameter p = params.get(pi);
                                boolean isLastParam = (pi == params.size() - 1);
                                String paramPrefix = (isLastType ? "      " : "│     ") + (isLastParam ? "└─ " : "├─ ");
                                String paramText = p.getNameAsString() + " : " + p.getType().toString();
                                printLine(paramPrefix + paramText);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Error parsing file: " + e.getMessage());
            e.printStackTrace();
            System.exit(4);
        }
    }

    private static String typeKind(TypeDeclaration<?> td) {
        if (td instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration c = (ClassOrInterfaceDeclaration) td;
            return (c.isInterface() ? "Interface" : "Class");
        } else if (td instanceof EnumDeclaration) {
            return "Enum";
        } else if (td instanceof RecordDeclaration) {
            return "Record";
        } else {
            return "Type";
        }
    }

    private static void printLine(String text) {
        System.out.println(text);
    }
}
