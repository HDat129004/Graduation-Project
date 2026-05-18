package com.doantn.example;

/**
 * Ví dụ 4: Chuyển Đổi Nhiệt Độ (TemperatureConverter)
 *
 * Mục đích kiểm thử:
 * - Hàm với tham số double (thích hợp cho kiểm thử giá trị biên số thực).
 * - Hàm getState() phân loại trạng thái vật chất theo nhiệt độ Celsius:
 *   đá (< 0), nước (0–99), hơi (>= 100) → 3 nhánh rẽ.
 * - Ném IllegalArgumentException khi nhiệt độ Kelvin âm (vi phạm giới hạn vật lý).
 */
public class TemperatureConverter {

    /**
     * Chuyển Celsius sang Fahrenheit.
     * Công thức: F = C × 9/5 + 32
     */
    public double celsiusToFahrenheit(double celsius) {
        return celsius * 9.0 / 5.0 + 32;
    }

    /**
     * Chuyển Fahrenheit sang Celsius.
     * Công thức: C = (F − 32) × 5/9
     */
    public double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5.0 / 9.0;
    }

    /**
     * Chuyển Celsius sang Kelvin.
     * Ném IllegalArgumentException nếu Kelvin kết quả âm (nhiệt độ tuyệt đối âm).
     */
    public double celsiusToKelvin(double celsius) {
        double kelvin = celsius + 273.15;
        if (kelvin < 0) {
            throw new IllegalArgumentException("Nhiệt độ Kelvin không thể âm");
        }
        return kelvin;
    }

    /**
     * Xác định trạng thái của nước tại nhiệt độ (°C) cho trước.
     * Trả về "da" (< 0°C), "nuoc" (0–99°C), "hoi" (>= 100°C).
     */
    public String getWaterState(double celsius) {
        if (celsius < 0) {
            return "da";
        } else if (celsius < 100) {
            return "nuoc";
        } else {
            return "hoi";
        }
    }

    /**
     * Kiểm tra nhiệt độ có phải là nhiệt độ phòng thoải mái không
     * (khoảng 18°C đến 26°C).
     */
    public boolean isComfortable(double celsius) {
        return celsius >= 18 && celsius <= 26;
    }
}
