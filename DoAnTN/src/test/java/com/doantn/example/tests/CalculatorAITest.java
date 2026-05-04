package com.doantn.example.tests;

import com.doantn.example.Calculator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorAITest {

    private final Calculator calculator = new Calculator();

    @Test
    void testAdd() {
        assertEquals(5, calculator.add(2, 3));
        assertEquals(-1, calculator.add(2, -3));
        assertEquals(0, calculator.add(0, 0));
    }

    @Test
    void testSubtract() {
        assertEquals(-1, calculator.subtract(2, 3));
        assertEquals(5, calculator.subtract(2, -3));
        assertEquals(0, calculator.subtract(0, 0));
    }

    @Test
    void testMultiply() {
        assertEquals(6, calculator.multiply(2, 3));
        assertEquals(-6, calculator.multiply(2, -3));
        assertEquals(0, calculator.multiply(5, 0));
    }

    @Test
    void testDivide() {
        assertEquals(2.0, calculator.divide(6.0, 3.0), 0.0001);
        assertEquals(-2.5, calculator.divide(5.0, -2.0), 0.0001);
    }

    @Test
    void testDivideByZero() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        });
        assertEquals("Cannot divide by zero", exception.getMessage());
    }

    @Test
    void testIsPositiveTrue() {
        assertTrue(calculator.isPositive(1));
        assertTrue(calculator.isPositive(100));
    }

    @Test
    void testIsPositiveFalse() {
        assertFalse(calculator.isPositive(0));
        assertFalse(calculator.isPositive(-1));
    }

    @Test
    void testGetNumberTypePositive() {
        assertEquals("positive", calculator.getNumberType(5));
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