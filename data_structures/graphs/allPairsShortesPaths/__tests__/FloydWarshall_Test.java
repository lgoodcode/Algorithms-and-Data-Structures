package data_structures.graphs.allPairsShortesPaths.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.allPairsShortesPaths.FloydWarshall;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FloydWarshall_Test {
  Graph G = new Graph(9, true, true);
  int[][] table;

  @BeforeEach
  void setup() {
    G.addEdge(0, 1, 3);
    G.addEdge(0, 2, 8);
    G.addEdge(0, 4, -4);
    G.addEdge(1, 3, 1);
    G.addEdge(1, 4, 7);
    G.addEdge(2, 1, 4);
    G.addEdge(3, 0, 2);
    G.addEdge(3, 2, -5);
    G.addEdge(4, 3, 6);
    G.addEdge(7, 8, 4);
  }

  @Test
  void floydWarshall() {
    assertNotNull(FloydWarshall.run(G));
  }

  @Test
  void throws_on_invalid_graph() {
    assertThrows(IllegalArgumentException.class, () -> FloydWarshall.run(new Graph(1, false, false)));
  }

  @Test
  void prints_path() {
    assertEquals("0 -> 4 -> 3 -> 2", FloydWarshall.printPath(G, 0, 2));
  }

  @Test
  void table_prints_path() {
    table = FloydWarshall.table(G);
    assertEquals("0 -> 4 -> 3 -> 2", FloydWarshall.printPath(table, 0, 2));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 0 to 5", FloydWarshall.printPath(G, 0, 5));
  }

  @Test 
  void array_path() {
    int[] path = { 0, 4, 3, 2 };
    assertArrayEquals(path, FloydWarshall.arrayPath(G, 0, 2));
  }

  @Test 
  void table_array_path() {
    int[] path = { 0, 4, 3, 2 };
    table = FloydWarshall.table(G);
    assertArrayEquals(path, FloydWarshall.arrayPath(table, 0, 2));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, FloydWarshall.arrayPath(G, 0, 5));
  }
}
