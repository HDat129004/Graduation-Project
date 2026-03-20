package com.doantn.analyzer;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodInspectorTest {

    @Test
    public void testInspectSimpleClass() throws Exception {
        String src = "package com.doantn.testsample;\n" +
                "public class Sample {\n" +
                "    public int add(int a, int b) { return a + b; }\n" +
                "    public void noop() {}\n" +
                "}\n";

        File temp = File.createTempFile("Sample", ".java");
        temp.deleteOnExit();
        try (FileWriter fw = new FileWriter(temp)) {
            fw.write(src);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        try {
            System.setOut(new PrintStream(baos));
            MethodInspector.main(new String[]{temp.getAbsolutePath()});
        } finally {
            System.setOut(oldOut);
        }

        String out = baos.toString();
        // Basic assertions to ensure method names and types appear
        assertTrue(out.contains("add : int"), "Expected method 'add' to be present in output. Output:\n" + out);
        assertTrue(out.contains("noop : void"), "Expected method 'noop' to be present in output. Output:\n" + out);
    }
}
