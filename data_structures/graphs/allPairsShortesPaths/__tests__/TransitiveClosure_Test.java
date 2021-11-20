package data_structures.graphs.allPairsShortesPaths.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.allPairsShortesPaths.TransitiveClosure;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TransitiveClosure_Test {
  Graph G = new Graph(9, true, true);
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
  void no_instantiation() {
    assertThrows(NoClassDefFoundError.class, () -> new TransitiveClosure());
  }

  @Test
  void TransitiveClosure() {
    assertNotNull(TransitiveClosure.run(G));
  }

  @Test
  void throws_on_invalid_graph() {
    assertThrows(IllegalArgumentException.class, () -> TransitiveClosure.run(new Graph(1, false, false)));
  }

  @Test
  void prints_path() {
    assertEquals("1 -> 3", TransitiveClosure.printPath(G, 1, 3));
  }

  @Test
  void table_prints_path() {
    table = TransitiveClosure.run(G);
    assertEquals("1 -> 3", TransitiveClosure.printPath(table, 1, 3));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 0 to 5", TransitiveClosure.printPath(G, 0, 5));
  }

  @Test 
  void array_path() {
    int[] path = { 1, 3 };
    assertArrayEquals(path, TransitiveClosure.arrayPath(G, 1, 3));
  }

  @Test 
  void table_array_path() {
    int[] path = { 1, 3 };
    table = TransitiveClosure.run(G);
    assertArrayEquals(path, TransitiveClosure.arrayPath(table, 1, 3));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, TransitiveClosure.arrayPath(G, 0, 5));
  }

}
