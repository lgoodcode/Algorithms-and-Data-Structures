package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import algorithms.dynamic.LCS;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LCS_Test {
  String a = "ABCBDAB";
  String b = "BDCABA";
  String output = "BCBA";

  @Test
  void LCS() {
    assertEquals(output, LCS.compute(a, b));
  }

  @Test
  void LCS_iterative() {
    assertEquals(output, LCS.computeIterative(a, b));
  }
}
