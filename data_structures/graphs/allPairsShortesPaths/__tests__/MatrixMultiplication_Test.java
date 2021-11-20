package data_structures.graphs.allPairsShortesPaths.__tests__;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.allPairsShortesPaths.MatrixMultiplication;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class MatrixMultiplication_Test {
  Graph G = new Graph(9, true, true);
  int[] matrices = { 10, 100, 5, 50, 1 };
  int[][] table;

  @BeforeEach
  void setup() {
    G.addEdge(1, 2, 3);
    G.addEdge(1, 3, 8);
    G.addEdge(1, 5, -4);
    G.addEdge(2, 4, 1);
    G.addEdge(2, 5, 7);
    G.addEdge(3, 2, 4);
    G.addEdge(4, 1, 2);
    G.addEdge(4, 3, -5);
    G.addEdge(5, 4, 6);
    G.addEdge(7, 8, 4);
  }

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

  @Test
  void matrixMultiplicationPaths() {
    assertEquals("1 -> 5 -> 4 -> 3", MatrixMultiplication.printPath(G, 1, 3));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 0 to 5", MatrixMultiplication.printPath(G, 0, 5));
  }

  @Test 
  void array_path() {
    int[] path = { 1, 5, 4, 3 };
    assertArrayEquals(path, MatrixMultiplication.arrayPath(G, 1, 3));
  }

}
