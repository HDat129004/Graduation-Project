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
    void testAddPositiveNumbers() {
        assertEquals(10, calculator.add(7, 3));
    }

    @Test
    void testAddNegativeNumbers() {
        assertEquals(-10, calculator.add(-7, -3));
    }

    @Test
    void testSubtractPositiveNumbers() {
        assertEquals(4, calculator.subtract(7, 3));
    }

    @Test
    void testSubtractToNegative() {
        assertEquals(-4, calculator.subtract(3, 7));
    }

    @Test
    void testMultiplyPositiveNumbers() {
        assertEquals(21, calculator.multiply(7, 3));
    }

    @Test
    void testMultiplyByZero() {
        assertEquals(0, calculator.multiply(7, 0));
    }

    @Test
    void testDividePositiveNumbers() {
        assertEquals(2.5, calculator.divide(5.0, 2.0));
    }

    @Test
    void testDivideByZeroThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        });
    }

    @Test
    void testIsPositiveTrue() {
        assertTrue(calculator.isPositive(1));
    }

    @Test
    void testIsPositiveFalseWithZero() {
        assertFalse(calculator.isPositive(0));
    }

    @Test
    void testIsPositiveFalseWithNegative() {
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