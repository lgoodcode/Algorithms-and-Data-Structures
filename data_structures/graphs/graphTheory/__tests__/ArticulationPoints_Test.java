package data_structures.graphs.graphTheory.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.graphTheory.ArticulationPoints;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ArticulationPoints_Test {
  Graph G = new Graph(23);
  Integer[] AP = { 4, 7, 15, 17, 14, 13, 10, 3 };

  @BeforeEach
  void setup() {
    G.addEdge(0, 1);
    G.addEdge(0, 2);
    G.addEdge(0, 3);
    G.addEdge(1, 2);
    G.addEdge(1, 3);
    G.addEdge(2, 3);
    G.addEdge(3, 4);
    G.addEdge(4, 5);
    G.addEdge(4, 6);
    G.addEdge(5, 6);
    G.addEdge(4, 7);
    G.addEdge(4, 8);
    G.addEdge(7, 8);
    G.addEdge(7, 9);
    G.addEdge(4, 10);
    G.addEdge(10, 11);
    G.addEdge(10, 12);
    G.addEdge(11, 13);
    G.addEdge(12, 13);
    G.addEdge(13, 14);
    G.addEdge(14, 15);
    G.addEdge(15, 16);
    G.addEdge(15, 17);
    G.addEdge(17, 18);
    G.addEdge(17, 19);
    G.addEdge(18, 19);
    G.addEdge(15, 20);
    G.addEdge(15, 21);
    G.addEdge(14, 20);
    G.addEdge(20, 22);
    G.addEdge(21, 22);
    G.addEdge(20, 21);
  }

  @Test
  void no_instantiation() {
    assertThrows(NoClassDefFoundError.class, () -> new ArticulationPoints());
  }

  @Test
  void articulationPoints() {
    assertArrayEquals(AP, ArticulationPoints.compute(G));
  }
}
