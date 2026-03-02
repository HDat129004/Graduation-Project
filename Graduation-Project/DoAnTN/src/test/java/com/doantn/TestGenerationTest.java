package com.doantn;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test for the automatic test generation pipeline
 */
public class TestGenerationTest {

    @Test
    public void testGenerateTestsForSimpleClass() throws Exception {
        // Create a temporary Java file to analyze
        String sourceCode = "package com.doantn.sample;\n" +
                "public class Calculator {\n" +
                "    public int add(int a, int b) { return a + b; }\n" +
                "    public int subtract(int a, int b) { return a - b; }\n" +
                "    public int multiply(int a, int b) { return a * b; }\n" +
                "    public double divide(double a, double b) {\n" +
                "        if (b == 0) throw new IllegalArgumentException(\"Cannot divide by zero\");\n" +
                "        return a / b;\n" +
                "    }\n" +
                "}\n";

        File tempDir = new File("target/temp-test-source");
        tempDir.mkdirs();

        File tempFile = new File(tempDir, "Calculator.java");
        tempFile.deleteOnExit();
        tempDir.deleteOnExit();

        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(sourceCode);
        }

        // Run the test generation
        com.doantn.app.App.main(new String[]{tempFile.getAbsolutePath(), "target/generated-tests"});

        // Verify output files exist
        File outputDir = new File("target/generated-tests");
        assertTrue(outputDir.exists(), "Output directory should be created");

        // Check if test files were generated
        File[] generatedTests = outputDir.listFiles();
        assertNotNull(generatedTests, "Generated test files should exist");
        assertTrue(generatedTests.length > 0, "At least one test file should be generated");

        System.out.println("Generated " + generatedTests.length + " test files");
        for (File f : generatedTests) {
            System.out.println("  - " + f.getName());
        }
    }
}

