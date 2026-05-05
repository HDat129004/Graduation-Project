package com.doantn.generator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class AITestGenerator {

    private static final String DEFAULT_API_KEY = "AIzaSyBBbdVqLMmBaAvjX8SznA9QoaT_5ATK31o";
    private final String apiKey;
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-flash-latest:generateContent";
    private final String outputDir;

    public AITestGenerator(String outputDir, String apiKey) {
        this.outputDir = outputDir;
        this.apiKey = (apiKey != null && !apiKey.isBlank()) ? apiKey.trim() : DEFAULT_API_KEY;
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (Exception e) {
            System.err.println("[AI] Lỗi tạo thư mục đầu ra: " + e.getMessage());
        }
    }

    public void generateTestUsingAI(String sourceFilePath, String className, String packageName) {
        System.out.println("\n[AI GEMINI] ĐANG XỬ LÝ: " + className);

        try {
            String sourceCode = new String(Files.readAllBytes(Paths.get(sourceFilePath)));
            String expectedClassName = className + "AITest";

            // 1. PROMPT: Ép AI viết test thật cơ bản, cấm dùng các hàm phức tạp dễ gây lỗi
            String prompt = "Write a complete JUnit 5 test class for the provided code.\n" +
                    "Package: " + packageName + ".tests\n" +
                    "RULES:\n" +
                    "1. Class name MUST be: " + expectedClassName + "\n" +
                    "2. DO NOT use @ParameterizedTest or @MethodSource.\n" +
                    "3. Use ONLY standard @Test methods for each test case.\n" +
                    "4. Output ONLY raw Java code. No markdown.\n\n" +
                    "Source code:\n" + sourceCode;

            if (apiKey.isEmpty()) {
                System.err.println("[AI] API key chưa được cung cấp. Vui lòng nhập GEMINI_API_KEY hoặc điền API key trong GUI.");
                return;
            }

            // 2. GỌI API
            String generatedCode = callGeminiAPI(prompt);

            if (generatedCode != null && !generatedCode.trim().isEmpty()) {
                // 3. XỬ LÝ CHUỖI (Post-processing)
                generatedCode = cleanGeneratedCode(generatedCode);

                // Đổi tên class để chắc chắn file compile được
                generatedCode = generatedCode.replaceAll(
                        "public\\s+class\\s+[a-zA-Z0-9_]+",
                        "public class " + expectedClassName
                );

                // 4. LƯU FILE
                String testPackageDir = packageName.equals("(default)") ? "" : packageName.replace(".", "/");
                Path testDirPath = Paths.get(outputDir, testPackageDir, "tests");
                Files.createDirectories(testDirPath);

                Path testFilePath = testDirPath.resolve(expectedClassName + ".java");
                Files.write(testFilePath, generatedCode.getBytes());

                System.out.println("[AI] THÀNH CÔNG! File lưu tại: " + testFilePath.toAbsolutePath());
            } else {
                System.out.println("[AI] THẤT BẠI: Không nhận được phản hồi từ AI.");
            }

        } catch (Exception e) {
            System.err.println("[AI LỖI]: " + e.getMessage());
        }
    }

    private String callGeminiAPI(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        JsonObject textPart = new JsonObject();
        textPart.addProperty("text", prompt);
        JsonArray partsArray = new JsonArray();
        partsArray.add(textPart);
        JsonObject contentObj = new JsonObject();
        contentObj.add("parts", partsArray);
        JsonArray contentsArray = new JsonArray();
        contentsArray.add(contentObj);
        JsonObject requestBodyJson = new JsonObject();
        requestBodyJson.add("contents", contentsArray);

        RequestBody body = RequestBody.create(
                requestBodyJson.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Content-Type", "application/json")
                .addHeader("X-goog-api-key", apiKey)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";

            // QUAN TRỌNG: Đoạn này sẽ in ra nguyên nhân chi tiết nếu Google từ chối
            if (!response.isSuccessful()) {
                System.err.println("[AI LỖI HTTP] Mã lỗi: " + response.code());
                System.err.println("[AI CHI TIẾT LỖI]: " + responseBody);
                return null;
            }

            JsonObject jsonResponse = JsonParser.parseString(responseBody).getAsJsonObject();
            return jsonResponse.getAsJsonArray("candidates")
                    .get(0).getAsJsonObject()
                    .getAsJsonObject("content")
                    .getAsJsonArray("parts")
                    .get(0).getAsJsonObject()
                    .get("text").getAsString();
        }
    }

    private String cleanGeneratedCode(String code) {
        return code.replaceAll("```java", "").replaceAll("```", "").trim();
    }
}