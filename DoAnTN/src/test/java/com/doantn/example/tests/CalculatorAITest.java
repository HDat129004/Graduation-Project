package com.doantn.example.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import com.doantn.example.Calculator;

public class CalculatorAITest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAdd() {
        assertEquals(5, calculator.add(2, 3), "2 + 3 should be 5");
        assertEquals(-1, calculator.add(2, -3), "2 + (-3) should be -1");
        assertEquals(0, calculator.add(0, 0), "0 + 0 should be 0");
    }

    @Test
    void testSubtract() {
        assertEquals(1, calculator.subtract(3, 2), "3 - 2 should be 1");
        assertEquals(5, calculator.subtract(2, -3), "2 - (-3) should be 5");
        assertEquals(0, calculator.subtract(10, 10), "10 - 10 should be 0");
    }

    @Test
    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3), "2 * 3 should be 6");
        assertEquals(-6, calculator.multiply(2, -3), "2 * (-3) should be -6");
        assertEquals(0, calculator.multiply(5, 0), "5 * 0 should be 0");
    }

    @Test
    void testDivide() {
        assertEquals(2.0, calculator.divide(6.0, 3.0), "6.0 / 3.0 should be 2.0");
        assertEquals(-2.5, calculator.divide(5.0, -2.0), "5.0 / -2.0 should be -2.5");
    }

    @Test
    void testDivideByZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        });
        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    @Test
    void testIsPositive() {
        assertTrue(calculator.isPositive(1), "1 should be positive");
        assertFalse(calculator.isPositive(-1), "-1 should not be positive");
        assertFalse(calculator.isPositive(0), "0 should not be positive");
    }

    @Test
    void testGetNumberTypePositive() {
        assertEquals("positive", calculator.getNumberType(10));
    }

    @Test
    void testGetNumberTypeNegative() {
        assertEquals("negative", calculator.getNumberType(-5));
    }

    @Test
    void testGetNumberTypeZero() {
        assertEquals("zero", calculator.getNumberType(0));
    }
}