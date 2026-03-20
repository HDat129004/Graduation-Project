package com.doantn.example.tests;

import com.doantn.example.Calculator;
import java.lang.String;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CalculatorTest {
  @Test
  public void test_getNumberType() {
    Calculator calculator = new Calculator();
    int number = 0;
    // Fix the test assertion to expect the actual output "zero" instead of the auto-generated dummy ""
    String expected = "zero";
    String actual = calculator.getNumberType(number);
    Assertions.assertEquals(expected, actual);
  }
}
