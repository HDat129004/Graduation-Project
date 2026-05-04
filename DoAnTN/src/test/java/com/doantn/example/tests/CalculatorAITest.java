package com.doantn.example.tests;

import com.doantn.example.Calculator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CalculatorAITest {

    private Calculator calculator;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @Test
    public void testAdd() {
        int result = calculator.add(5, 3);
        assertEquals(8, result);
    }

    @Test
    public void testAddNegativeNumbers() {
        int result = calculator.add(-2, -3);
        assertEquals(-5, result);
    }

    @Test
    public void testSubtract() {
        int result = calculator.subtract(10, 4);
        assertEquals(6, result);
    }

    @Test
    public void testSubtractToNegative() {
        int result = calculator.subtract(4, 10);
        assertEquals(-6, result);
    }

    @Test
    public void testMultiply() {
        int result = calculator.multiply(4, 5);
        assertEquals(20, result);
    }

    @Test
    public void testMultiplyByZero() {
        int result = calculator.multiply(10, 0);
        assertEquals(0, result);
    }

    @Test
    public void testDivide() {
        double result = calculator.divide(10.0, 2.0);
        assertEquals(5.0, result);
    }

    @Test
    public void testDivideByZero() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10.0, 0.0);
        });
    }

    @Test
    public void testIsPositiveWithPositiveNumber() {
        assertTrue(calculator.isPositive(5));
    }

    @Test
    public void testIsPositiveWithNegativeNumber() {
        assertFalse(calculator.isPositive(-5));
    }

    @Test
    public void testIsPositiveWithZero() {
        assertFalse(calculator.isPositive(0));
    }

    @Test
    public void testGetNumberTypePositive() {
        String result = calculator.getNumberType(10);
        assertEquals("positive", result);
    }

    @Test
    public void testGetNumberTypeNegative() {
        String result = calculator.getNumberType(-10);
        assertEquals("negative", result);
    }

    @Test
    public void testGetNumberTypeZero() {
        String result = calculator.getNumberType(0);
        assertEquals("zero", result);
    }
}