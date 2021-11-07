package data_structures.graphs.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.BFS;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BFS_Test {
  Graph G;
  BFS.Node[] nodes;
  int[] path = { 0, 1, 4, 8 };
  
  @BeforeEach
  void setup() {
    G = new Graph(9, true);

    G.addEdge(0, 1);
    G.addEdge(0, 2);
    G.addEdge(0, 3);
    G.addEdge(2, 3);
    G.addEdge(2, 6);
    G.addEdge(3, 6);
    G.addEdge(3, 7);
    G.addEdge(1, 4);
    G.addEdge(1, 5);
    G.addEdge(4, 8);
  }

  @Test
  void bfs() {
    assertDoesNotThrow(() -> BFS.run(G, 0));
  }

  @Test
  void printPath() {
    assertEquals("0 -> 1 -> 4 -> 8", BFS.printPath(G, 0, 8));
  }

  @Test
  void nodes_printPath() {
    nodes = BFS.run(G, 0);
    assertEquals("0 -> 1 -> 4 -> 8", BFS.printPath(nodes, 0, 8));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 1 to 3", BFS.printPath(G, 1, 3));
  }

  @Test
  void arrayPath() {
    assertArrayEquals(path, BFS.arrayPath(G, 0, 8));
  }

  @Test
  void nodes_arrayPath() {
    nodes = BFS.run(G, 0);
    assertArrayEquals(path, BFS.arrayPath(nodes, 0, 8));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, BFS.arrayPath(G, 1, 3));
  }
}
