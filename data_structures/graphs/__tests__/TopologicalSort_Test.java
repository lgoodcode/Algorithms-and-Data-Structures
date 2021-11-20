package data_structures.graphs.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.TopologicalSort;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class TopologicalSort_Test {
  Graph G = new Graph(9, true, true);

  @BeforeEach
  void setup() {
    G.addEdge(0, 1, 10);
    G.addEdge(0, 2, 5);
    G.addEdge(1, 3, 1);
    G.addEdge(1, 2, 2);
    G.addEdge(3, 4, 4);
    G.addEdge(2, 1, 3);
    G.addEdge(2, 3, 9);
    G.addEdge(2, 4, 2);
    G.addEdge(4, 3, 6);
    G.addEdge(4, 0, 7);
  }

  @Test
  void no_instantiation() {
    assertThrows(NoClassDefFoundError.class, () -> new TopologicalSort());
  }

  @Test
  void sort() {
    assertDoesNotThrow(() -> TopologicalSort.run(G, 0));
  }
}
