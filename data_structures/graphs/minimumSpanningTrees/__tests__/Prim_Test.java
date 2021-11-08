package data_structures.graphs.minimumSpanningTrees.__tests__;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.minimumSpanningTrees.Prim;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Prim_Test {
  Graph G = new Graph(9, true, true);

  @Test
  void prim() {
    G.addEdge(0, 1, 4);
    G.addEdge(0, 7, 8);
    G.addEdge(1, 7, 11);
    G.addEdge(1, 2, 8);
    G.addEdge(2, 8, 2);
    G.addEdge(7, 8, 7);
    G.addEdge(7, 6, 1);
    G.addEdge(6, 8, 6);
    G.addEdge(2, 5, 4);
    G.addEdge(2, 3, 7);
    G.addEdge(3, 5, 14);
    G.addEdge(3, 4, 9);
    G.addEdge(4, 5, 10);
    G.addEdge(5, 6, 2);

    Prim.run(G, 0);
  }
}
