package data_structures.graphs.singleSourceShortestPaths.__tests__;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.singleSourceShortestPaths.ShorterPathFaster;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ShorterPathFaster_Test {
  Graph G = new Graph(9, true, true);

  @Test
  void bellmanford() {
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

    assertNotNull(ShorterPathFaster.run(G, 0));
  }

  @Test
  void throws_on_invalid_graph() {
    assertThrows(IllegalArgumentException.class, () -> ShorterPathFaster.run(new Graph(1, false, false), 0));
  }
}
