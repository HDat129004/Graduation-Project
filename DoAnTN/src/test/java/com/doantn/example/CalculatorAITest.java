package com.doantn.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorAITest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAddPositiveNumbers() {
        int result = calculator.add(10, 20);
        assertEquals(30, result, "10 + 20 should be 30");
    }

    @Test
    void testAddNegativeNumbers() {
        int result = calculator.add(-5, -10);
        assertEquals(-15, result, "-5 + -10 should be -15");
    }

    @Test
    void testSubtract() {
        int result = calculator.subtract(50, 20);
        assertEquals(30, result, "50 - 20 should be 30");
    }

    @Test
    void testMultiply() {
        int result = calculator.multiply(5, 6);
        assertEquals(30, result, "5 * 6 should be 30");
    }

    @Test
    void testMultiplyByZero() {
        int result = calculator.multiply(10, 0);
        assertEquals(0, result, "10 * 0 should be 0");
    }

    @Test
    void testDivideValid() {
        double result = calculator.divide(10.0, 4.0);
        assertEquals(2.5, result, 0.0001, "10.0 / 4.0 should be 2.5");
    }

    @Test
    void testDivideByZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0);
        }, "Dividing by zero should throw IllegalArgumentException");
    }

    @Test
    void testIsPositiveWithPositiveNumber() {
        assertTrue(calculator.isPositive(1), "1 should be positive");
    }

    @Test
    void testIsPositiveWithNegativeNumber() {
        assertFalse(calculator.isPositive(-1), "-1 should not be positive");
    }

    @Test
    void testIsPositiveWithZero() {
        assertFalse(calculator.isPositive(0), "0 should not be positive");
    }

    @Test
    void testGetNumberTypePositive() {
        assertEquals("duong", calculator.getNumberType(100), "100 should return 'duong'");
    }

    @Test
    void testGetNumberTypeNegative() {
        assertEquals("am", calculator.getNumberType(-50), "-50 should return 'am'");
    }

    @Test
    void testGetNumberTypeZero() {
        assertEquals("khong", calculator.getNumberType(0), "0 should return 'khong'");
    }
}