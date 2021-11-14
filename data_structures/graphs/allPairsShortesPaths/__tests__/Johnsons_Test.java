package data_structures.graphs.allPairsShortesPaths.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.allPairsShortesPaths.Johnsons;
import data_structures.graphs.singleSourceShortestPaths.SSSP;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Johnsons_Test {
  Graph G = new Graph(9, true, true);
  SSSP.Node[][] nodes;

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
  void johnsons() {
    assertNotNull(Johnsons.run(G));
  }

  @Test
  void throws_on_invalid_graph() {
    assertThrows(IllegalArgumentException.class, () -> Johnsons.run(new Graph(1, false, false)));
  }

  @Test
  void no_negative_weight_cycle() {
    assertFalse(Johnsons.hasNegativeWeightCycle(G));
  }

  @Test
  void prints_path() {
    assertEquals("1 -> 5 -> 4 -> 3", Johnsons.printPath(G, 1, 3));
  }

  @Test
  void nodes_prints_path() {
    nodes = Johnsons.table(G);
    assertEquals("1 -> 5 -> 4 -> 3", Johnsons.printPath(nodes, 1, 3));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 0 to 5", Johnsons.printPath(G, 0, 5));
  }

  @Test 
  void array_path() {
    int[] path = { 1, 5, 4, 3 };
    assertArrayEquals(path, Johnsons.arrayPath(G, 1, 3));
  }

  @Test 
  void nodes_array_path() {
    int[] path = { 1, 5, 4, 3 };
    nodes = Johnsons.table(G);
    assertArrayEquals(path, Johnsons.arrayPath(nodes, 1, 3));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, Johnsons.arrayPath(G, 0, 5));
  }

  @Test
  void negative_weight_cycle_detection() {
    Graph W = new Graph(9, true, true);

    W.addEdge(1, 2, 3);
    W.addEdge(1, 3, 8);
    W.addEdge(1, 5, -4);
    W.addEdge(2, 4, 1);
    W.addEdge(2, 5, 7);
    W.addEdge(3, 2, 4);
    W.addEdge(4, 1, -2);
    W.addEdge(4, 3, -5);
    W.addEdge(5, 4, -3);

    assertTrue(Johnsons.hasNegativeWeightCycle(W));

  }
}
