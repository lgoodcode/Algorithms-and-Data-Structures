package algorithms.__tests__;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import algorithms.dynamic.MatrixChainOrder;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Matrix_Test {
  int[] m = {10, 100, 5, 50, 1};

  @Test
  void MatrixChainOrder() {
    assertEquals(1750, MatrixChainOrder.compute(m));
  }

  @Test 
  void solutions() {
    assertDoesNotThrow(() -> MatrixChainOrder.printTableSolution(m));
  }
}
