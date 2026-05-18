package com.doantn.example;

/**
 * Ví dụ 2: Xác Nhận Mật Khẩu (PasswordValidator)
 *
 * Mục đích kiểm thử:
 * - Nhiều điều kiện if lồng nhau với String và int.
 * - Ném IllegalArgumentException khi đầu vào null.
 * - Hàm getStrength() có 3 nhánh trả về mức độ mạnh yếu.
 * - Phù hợp để kiểm tra kỹ thuật phân tích giá trị biên trên độ dài chuỗi.
 */
public class PasswordValidator {

    private static final int MIN_LENGTH = 8;
    private static final int STRONG_LENGTH = 12;

    /**
     * Kiểm tra mật khẩu có hợp lệ không.
     * Mật khẩu hợp lệ phải có ít nhất 8 ký tự.
     *
     * @throws IllegalArgumentException nếu mật khẩu là null.
     */
    public boolean isValid(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Mật khẩu không được null");
        }
        return password.length() >= MIN_LENGTH;
    }

    /**
     * Đánh giá độ mạnh của mật khẩu.
     * Trả về: "yeu" (< 8), "trung_binh" (8–11), "manh" (>= 12).
     */
    public String getStrength(String password) {
        if (password == null || password.length() < MIN_LENGTH) {
            return "yeu";
        } else if (password.length() < STRONG_LENGTH) {
            return "trung_binh";
        } else {
            return "manh";
        }
    }

    /**
     * Kiểm tra mật khẩu có chứa ít nhất một chữ số không.
     *
     * @throws IllegalArgumentException nếu mật khẩu là null.
     */
    public boolean containsDigit(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Mật khẩu không được null");
        }
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Đếm số ký tự đặc biệt trong mật khẩu (!@#$%^&*).
     */
    public int countSpecialChars(String password) {
        if (password == null) {
            return 0;
        }
        int count = 0;
        String specialChars = "!@#$%^&*";
        for (char c : password.toCharArray()) {
            if (specialChars.indexOf(c) >= 0) {
                count++;
            }
        }
        return count;
    }
}
