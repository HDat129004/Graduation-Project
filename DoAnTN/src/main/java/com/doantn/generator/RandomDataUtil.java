package com.doantn.generator;

import java.util.Random;
import java.util.UUID;

/**
 * RandomDataUtil: Tiện ích hỗ trợ sinh dữ liệu ngẫu nhiên cho các ca kiểm thử (Random Testing).
 */
public class RandomDataUtil {
    private static final Random RANDOM = new Random();

    // ==========================================
    // 1. NHÓM SINH DỮ LIỆU SỐ (NUMBERS)
    // ==========================================

    public static int randomInt() {
        return RANDOM.nextInt(2000) - 1000; // -1000 đến 999
    }

    public static int randomPositiveInt() {
        return RANDOM.nextInt(1000) + 1; // 1 đến 1000
    }

    public static int randomNegativeInt() {
        return -(RANDOM.nextInt(1000) + 1); // -1000 đến -1
    }

    public static int randomIntInRange(int min, int max) {
        if (min >= max) return min;
        return RANDOM.nextInt((max - min) + 1) + min;
    }

    public static long randomLong() {
        return RANDOM.nextLong() % 100000L;
    }

    public static double randomDouble() {
        return RANDOM.nextDouble() * 2000.0 - 1000.0;
    }

    public static float randomFloat() {
        return RANDOM.nextFloat() * 2000.0f - 1000.0f;
    }

    public static short randomShort() {
        return (short) (RANDOM.nextInt(200) - 100);
    }

    public static byte randomByte() {
        return (byte) (RANDOM.nextInt(256) - 128);
    }

    // ==========================================
    // 2. NHÓM SINH KÝ TỰ & LOGIC
    // ==========================================

    public static char randomChar() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        return chars.charAt(RANDOM.nextInt(chars.length()));
    }

    public static boolean randomBoolean() {
        return RANDOM.nextBoolean();
    }

    // ==========================================
    // 3. NHÓM SINH DỮ LIỆU CHỮ (STRINGS)
    // ==========================================

    public static String randomString() {
        return "random_" + UUID.randomUUID().toString().substring(0, 8);
    }

    public static String randomEmptyOrNullString() {
        return RANDOM.nextBoolean() ? "" : null;
    }

    public static String randomVietnameseString() {
        String[] words = {
            "Nguyễn Văn A", "Trần Thị B", "Lê Hoàng C", "Phạm Minh D", 
            "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Cần Thơ", 
            "Đại học Bách Khoa", "Đồ án tốt nghiệp", "Kiểm thử tự động", "Trí tuệ nhân tạo"
        };
        return words[RANDOM.nextInt(words.length)];
    }

    public static String randomSpecialCharString() {
        String[] specials = {
            "!@#$%^&*()_+", "<>?:\"{}|~`", "DROP TABLE users;--", "<script>alert('test')</script>", 
            "Xin chào\nThế giới\t!", "    khoảng trắng    "
        };
        return specials[RANDOM.nextInt(specials.length)];
    }

    public static String randomEmail() {
        String[] domains = {"gmail.com", "yahoo.com", "outlook.com", "hust.edu.vn", "company.org"};
        String user = "user" + RANDOM.nextInt(9999);
        return user + "@" + domains[RANDOM.nextInt(domains.length)];
    }

    public static String randomPhoneNumber() {
        String[] prefixes = {"090", "091", "098", "097", "086", "088", "070", "033"};
        StringBuilder sb = new StringBuilder(prefixes[RANDOM.nextInt(prefixes.length)]);
        for (int i = 0; i < 7; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    // ==========================================
    // 4. HÀM TỔNG HỢP (REFLECTION SUPPORT)
    // ==========================================

    public static Object randomObject(Class<?> clazz) {
        if (clazz == int.class || clazz == Integer.class) {
            // Ngẫu nhiên chọn giữa số âm, số dương, hoặc số 0 để tăng độ phủ các trường hợp biên
            int caseType = RANDOM.nextInt(3);
            return caseType == 0 ? 0 : (caseType == 1 ? randomPositiveInt() : randomNegativeInt());
        }
        if (clazz == long.class || clazz == Long.class) return randomLong();
        if (clazz == double.class || clazz == Double.class) return randomDouble();
        if (clazz == float.class || clazz == Float.class) return randomFloat();
        if (clazz == short.class || clazz == Short.class) return randomShort();
        if (clazz == byte.class || clazz == Byte.class) return randomByte();
        if (clazz == char.class || clazz == Character.class) return randomChar();
        if (clazz == boolean.class || clazz == Boolean.class) return randomBoolean();
        if (clazz == String.class) {
            // Ngẫu nhiên chọn các kịch bản chuỗi khác nhau: chuỗi thường, tiếng Việt, email, số điện thoại, ký tự đặc biệt
            int strType = RANDOM.nextInt(6);
            switch (strType) {
                case 0: return randomVietnameseString();
                case 1: return randomEmail();
                case 2: return randomPhoneNumber();
                case 3: return randomSpecialCharString();
                case 4: return randomEmptyOrNullString() == null ? "" : ""; // Đảm bảo trả về chuỗi rỗng thay vì null để tránh NPE khi nối chuỗi
                default: return randomString();
            }
        }
        return null;
    }
}
