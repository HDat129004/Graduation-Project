package com.doantn.sample.tests;

import com.doantn.sample.Calculator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
  @Test
  public void test_divide() {
    // TODO: This test was auto-generated.
    // Please verify the default expected values and input data.
    Calculator calculator = new Calculator();
    double a = 0.0;
    double b = 0.0;
    double expected = 0.0;
    double actual = calculator.divide(a, b);
    Assertions.assertEquals(expected, actual);
  }
}
