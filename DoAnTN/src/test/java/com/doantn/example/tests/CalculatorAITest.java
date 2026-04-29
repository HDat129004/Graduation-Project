package com.doantn.example.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.doantn.example.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculatorAITest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    void testAdd() {
        int result = calculator.add(5, 3);
        assertEquals(8, result, "5 + 3 should be 8");
    }

    @Test
    void testSubtract() {
        int result = calculator.subtract(10, 4);
        assertEquals(6, result, "10 - 4 should be 6");
    }

    @Test
    void testMultiply() {
        int result = calculator.multiply(4, 5);
        assertEquals(20, result, "4 * 5 should be 20");
    }

    @Test
    void testDivide() {
        double result = calculator.divide(10.0, 2.0);
        assertEquals(5.0, result, "10.0 / 2.0 should be 5.0");
    }

    @Test
    void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        }, "Dividing by zero should throw IllegalArgumentException");
    }

    @Test
    void testIsPositiveWithPositiveNumber() {
        assertTrue(calculator.isPositive(5), "5 should be positive");
    }

    @Test
    void testIsPositiveWithNegativeNumber() {
        assertFalse(calculator.isPositive(-2), "-2 should not be positive");
    }

    @Test
    void testIsPositiveWithZero() {
        assertFalse(calculator.isPositive(0), "0 should not be positive");
    }

    @Test
    void testGetNumberTypePositive() {
        assertEquals("positive", calculator.getNumberType(10), "10 should return 'positive'");
    }

    @Test
    void testGetNumberTypeNegative() {
        assertEquals("negative", calculator.getNumberType(-5), "-5 should return 'negative'");
    }

    @Test
    void testGetNumberTypeZero() {
        assertEquals("zero", calculator.getNumberType(0), "0 should return 'zero'");
    }
}