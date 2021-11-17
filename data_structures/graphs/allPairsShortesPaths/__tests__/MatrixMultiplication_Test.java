package data_structures.graphs.allPairsShortesPaths.__tests__;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.jupiter.api.*;

import data_structures.graphs.allPairsShortesPaths.MatrixMultiplication;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MatrixMultiplication_Test {
  int[] matrices = { 10, 100, 5, 50, 1 };

  @Test
  void MatrixMultiplication() {
    assertEquals(1750, MatrixMultiplication.matrixChainOrder(matrices));
  }

  @Test
  void printOptimalParenthesis() {
    assertEquals("(1, 2)", MatrixMultiplication.printOptimalParenthesis(matrices, 1, 2));
  }

  @Test
  void throws_on_null() {
    assertThrows(NullPointerException.class, () -> MatrixMultiplication.matrixChainOrder(null));
  }

}
