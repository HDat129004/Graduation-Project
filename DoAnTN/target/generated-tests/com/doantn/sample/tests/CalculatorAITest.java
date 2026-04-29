package com.doantn.sample.tests;

import com.doantn.sample.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

/**
 * GHI CHÚ BÁO CÁO:
 * Đây là file kiểm thử được sinh ra bởi TRÍ TUỆ NHÂN TẠO.
 * Thầy có thể so sánh: AI không cần dùng hàm defaultValueForType(0),
 * mà AI tự hiểu 'add' là phép cộng nên truyền 5+5=10,
 * tự hiểu 'divide' chia cho 0 sẽ văng lỗi ArithmeticException.
 */
public class CalculatorAITest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("AI Test: Cộng hai số dương")
    public void testAdd() {
        Assertions.assertEquals(10, calculator.add(5, 5));
    }

    @Test
    @DisplayName("AI Test: Chia cho số 0 phải văng lỗi Ngoại lệ")
    public void testDivideByZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        }, "Ngoại lệ IllegalArgumentException phải được ném ra khi chia cho 0");
    }

    @Test
    @DisplayName("AI Test: Phân loại số thông minh")
    public void testGetNumberType() {
        Assertions.assertEquals("positive", calculator.getNumberType(99));
        Assertions.assertEquals("negative", calculator.getNumberType(-10));
        Assertions.assertEquals("zero", calculator.getNumberType(0));
    }
}