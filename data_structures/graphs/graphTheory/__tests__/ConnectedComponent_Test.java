package data_structures.graphs.graphTheory.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.graphTheory.ConnectedComponent;
import data_structures.sets.DisjointSet;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ConnectedComponent_Test {
  Graph G = new Graph(23);
  Integer[][] basic = {
    { 0, 1, 2, 3 },
    { 4, 5, 6 },
    { 7, 8 },
    { 9 }
  };
  Integer[][] CC = {
    { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 },
    { 10, 11, 13, 12 },
    { 14, 15, 17, 18, 19, 20, 21, 22 },
    { 16 }
  };

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
    // Removes the bridge from the lower half to the upper half of vertices
    // G.addEdge(4, 10);
    G.addEdge(10, 11);
    G.addEdge(10, 12);
    G.addEdge(11, 13);
    G.addEdge(12, 13);
    // Removes the bridge, making the previous vertices a little island :)
    // G.addEdge(13, 14);
    G.addEdge(14, 15);
    // Removes the bridge making 16 a single, unreachable vertex, which
    // itself forms a connected component of itself
    // G.addEdge(15, 16);
    G.addVertex(16);
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
    assertThrows(NoClassDefFoundError.class, () -> new ConnectedComponent());
  }

  @Test
  void connectedComponent_basic() {
    Graph H = new Graph(10);
    H.addEdge(0, 1);
    H.addEdge(0, 2);
    H.addEdge(1, 2);
    H.addEdge(1, 3);
    H.addEdge(4, 5);
    H.addEdge(4, 6);
    H.addEdge(7, 8);
    H.addVertex(9);

    assertArrayEquals(basic, ConnectedComponent.compute(H));
  }

  @Test
  void ConnectedComponent() {
    assertArrayEquals(CC, ConnectedComponent.compute(G));
  }

  @Test
  void CC_disjointSet() {
    DisjointSet<Integer>[] CC_disjoint = ConnectedComponent.computeDisjointSets(G);

    assertTrue(ConnectedComponent.sameComponent(CC_disjoint[0], CC_disjoint[8]));
    assertFalse(ConnectedComponent.sameComponent(CC_disjoint[0], CC_disjoint[10]));
  }
}
