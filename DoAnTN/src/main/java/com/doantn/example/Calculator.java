package com.doantn.example;

/**
 * Ví dụ 1: Máy Tính Cơ Bản (Calculator)
 *
 * Mục đích kiểm thử:
 * - Phép chia ném IllegalArgumentException khi mẫu số bằng 0 (kiểm tra nhánh throw).
 * - Hàm getNumberType() có 3 nhánh if/else if/else (dương / âm / bằng 0).
 * - Tất cả hàm đều có tham số số nguyên → thử nghiệm với BVA và dữ liệu ngẫu nhiên.
 */
public class Calculator {

    /** Cộng hai số nguyên. */
    public int add(int a, int b) {
        return a + b;
    }

    /** Trừ hai số nguyên. */
    public int subtract(int a, int b) {
        return a - b;
    }

    /** Nhân hai số nguyên. */
    public int multiply(int a, int b) {
        return a * b;
    }

    /**
     * Chia hai số thực.
     * Ném IllegalArgumentException nếu số chia bằng 0.
     */
    public double divide(double a, double b) {
        if (b == 0) {
            throw new IllegalArgumentException("Không thể chia cho số 0");
        }
        return a / b;
    }

    /** Kiểm tra một số có dương hay không. */
    public boolean isPositive(int number) {
        return number > 0;
    }

    /**
     * Phân loại số: "duong", "am", hoặc "khong".
     * Bao gồm 3 nhánh rẽ: if / else if / else.
     */
    public String getNumberType(int number) {
        if (number > 0) {
            return "duong";
        } else if (number < 0) {
            return "am";
        } else {
            return "khong";
        }
    }
}
