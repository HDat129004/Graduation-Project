package com.doantn.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorAITest {

    private final Calculator calculator = new Calculator();

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
    void testSubtractPositiveNumbers() {
        int result = calculator.subtract(20, 10);
        assertEquals(10, result, "20 - 10 should be 10");
    }

    @Test
    void testSubtractResultingInNegative() {
        int result = calculator.subtract(5, 15);
        assertEquals(-10, result, "5 - 15 should be -10");
    }

    @Test
    void testMultiplyNumbers() {
        int result = calculator.multiply(5, 6);
        assertEquals(30, result, "5 * 6 should be 30");
    }

    @Test
    void testMultiplyByZero() {
        int result = calculator.multiply(10, 0);
        assertEquals(0, result, "10 * 0 should be 0");
    }

    @Test
    void testDivideValidNumbers() {
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
    void testIsPositiveWithZero() {
        assertFalse(calculator.isPositive(0), "0 should not be positive");
    }

    @Test
    void testIsPositiveWithNegativeNumber() {
        assertFalse(calculator.isPositive(-5), "-5 should not be positive");
    }

    @Test
    void testGetNumberTypePositive() {
        String result = calculator.getNumberType(100);
        assertEquals("duong", result, "Positive number should return 'duong'");
    }

    @Test
    void testGetNumberTypeNegative() {
        String result = calculator.getNumberType(-100);
        assertEquals("am", result, "Negative number should return 'am'");
    }

    @Test
    void testGetNumberTypeZero() {
        String result = calculator.getNumberType(0);
        assertEquals("khong", result, "Zero should return 'khong'");
    }

    @Test
    void testBoundaryValuePositive() {
        assertTrue(calculator.isPositive(Integer.MAX_VALUE));
        assertEquals("duong", calculator.getNumberType(Integer.MAX_VALUE));
    }

    @Test
    void testBoundaryValueNegative() {
        assertFalse(calculator.isPositive(Integer.MIN_VALUE));
        assertEquals("am", calculator.getNumberType(Integer.MIN_VALUE));
    }
}