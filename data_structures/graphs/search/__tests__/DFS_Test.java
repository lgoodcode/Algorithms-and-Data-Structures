package data_structures.graphs.search.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.search.DFS;
import data_structures.graphs.search.DFS_Stack;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DFS_Test {
  Graph G;
  DFS.Node[] nodes;
  DFS_Stack.Node[] nodes2;
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
  void dfs() {
    assertDoesNotThrow(() -> DFS.run(G, 0));
  }

  @Test
  void printPath() {
    assertEquals("0 -> 1 -> 4 -> 8", DFS.printPath(G, 0, 8));
  }

  @Test
  void nodes_printPath() {
    nodes = DFS.run(G, 0);
    assertEquals("0 -> 1 -> 4 -> 8", DFS.printPath(nodes, 0, 8));
  }

  @Test
  void no_path() {
    assertEquals("No path exists from 1 to 3", DFS.printPath(G, 1, 3));
  }

  @Test
  void arrayPath() {
    assertArrayEquals(path, DFS.arrayPath(G, 0, 8));
  }

  @Test
  void nodes_arrayPath() {
    nodes = DFS.run(G, 0);
    assertArrayEquals(path, DFS.arrayPath(nodes, 0, 8));
  }

  @Test
  void no_array_path() {
    int[] noPath = { -1 };
    assertArrayEquals(noPath, DFS.arrayPath(G, 1, 3));
  }

  @Nested
  class DFS_Stack_implementation {
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
    void dfs_stack() {
      assertDoesNotThrow(() -> DFS_Stack.run(G, 0));
    }
  
    @Test
    void printPath() {
      assertEquals("0 -> 1 -> 4 -> 8", DFS_Stack.printPath(G, 0, 8));
    }
  
    @Test
    void nodes_printPath() {
      nodes2 = DFS_Stack.run(G, 0);
      assertEquals("0 -> 1 -> 4 -> 8", DFS_Stack.printPath(nodes2, 0, 8));
    }
  
    @Test
    void no_path() {
      assertEquals("No path exists from 1 to 3", DFS_Stack.printPath(G, 1, 3));
    }
  
    @Test
    void arrayPath() {
      assertArrayEquals(path, DFS_Stack.arrayPath(G, 0, 8));
    }
  
    @Test
    void nodes_arrayPath() {
      nodes2 = DFS_Stack.run(G, 0);
      assertArrayEquals(path, DFS_Stack.arrayPath(nodes2, 0, 8));
    }
  
    @Test
    void no_array_path() {
      int[] noPath = { -1 };
      assertArrayEquals(noPath, DFS_Stack.arrayPath(G, 1, 3));
    }
  }
}
