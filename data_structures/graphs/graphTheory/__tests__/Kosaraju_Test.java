package data_structures.graphs.graphTheory.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.graphTheory.Kosaraju;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Kosaraju_Test {
  Graph G = new Graph(10, true);
  Integer[][] SCC = {
    { 0, 4, 1 },
    { 2, 3 },
    { 6, 5 },
    { 7 }
  };

  @BeforeEach
  void setup() {
    G.addEdge(0, 1);
    G.addEdge(1, 2);
    G.addEdge(1, 5);
    G.addEdge(1, 4);
    G.addEdge(2, 3);
    G.addEdge(2, 6);
    G.addEdge(3, 2);
    G.addEdge(3, 7);
    G.addEdge(4, 0);
    G.addEdge(4, 5);
    G.addEdge(5, 6);
    G.addEdge(6, 5);
    G.addEdge(6, 7);
  }

  @Test
  void no_instantiation() {
    assertThrows(NoClassDefFoundError.class, () -> new Kosaraju());
  }

  @Test
  void kosaraju() {
    assertArrayEquals(SCC, Kosaraju.compute(G));
  }

}