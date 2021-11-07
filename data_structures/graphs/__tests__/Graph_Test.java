package data_structures.graphs.__tests__;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;


@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class Graph_Test {
  Graph g;
  int rows = 4;

  @Nested
  class When_New {

    @BeforeEach
    void create_graph() {
      g = new Graph(rows);
    }

    @Test
    void zero_vertices() {
      assertEquals(0, g.getNumVertices());
    }

    @Test
    void zero_edges() {
      assertEquals(0, g.getNumEdges());
    }

    @Test
    void empty_vertices() {
      int[] arr = {};
      assertArrayEquals(arr, g.getVertices());
    }

    @Test
    void empty_edges() {
      int[][] arr = new int[rows][];
      assertArrayEquals(arr, g.getEdges());
    }

    @Test
    void hasVertex_throws() {
      assertThrows(IllegalArgumentException.class, () -> g.hasVertex(-1));
    }

    @Test
    void hasVertex_is_false() {
      assertFalse(g.hasVertex(2));
    }

    @Test
    void hasEdge_is_false() {
      assertFalse(g.hasEdge(2, 3));
    }

    @Test
    void addVertex_throws() {
      assertThrows(IllegalArgumentException.class, () -> g.addVertex(-1));
      assertThrows(IllegalArgumentException.class, () -> g.addVertex(4));
    }

    @Test
    void addEdge_throws() {
      assertThrows(IllegalArgumentException.class, () -> g.addEdge(-1, 4));
    }

    @Test
    void removeVertex_throws() {
      assertThrows(IllegalArgumentException.class, () -> g.removeVertex(-1));
      assertThrows(NoSuchElementException.class, () -> g.removeVertex(2));
    }

    @Test
    void removeEdge_throws() {
      assertThrows(IllegalArgumentException.class, () -> g.removeEdge(-1, 4));
      assertThrows(NoSuchElementException.class, () -> g.removeEdge(2, 1));
    }
  }

  @Nested
  class With_Input_No_Weight {

    @BeforeEach
    void setup() {
      g = new Graph(rows);
    }

    @Test
    void addVertex() {
      assertDoesNotThrow(() -> g.addVertex(1));
    }

    @Test 
    void numVertices() {
      assertDoesNotThrow(() -> g.addVertex(1));
      assertEquals(1, g.getNumVertices());
    }

    @Test 
    void getVertices() {
      int[] V = { 1 };
      assertDoesNotThrow(() -> g.addVertex(1));
      assertEquals(1, g.getNumVertices());
      assertArrayEquals(V, g.getVertices());
    }

    @Test
    void addEdge() {
      assertDoesNotThrow(() -> g.addEdge(1, 3));
    }

    @Test
    void numEdges() {
      assertDoesNotThrow(() -> g.addEdge(1, 3));
      assertEquals(2, g.getNumVertices());
      assertEquals(1, g.getNumEdges());
    }

    @Test
    void getEdges() {
      assertDoesNotThrow(() -> g.addEdge(1, 3));
      assertEquals(2, g.getNumVertices());
      assertEquals(1, g.getNumEdges());
      assertNotNull(g.getEdges());
      assertTrue(g.hasEdge(1, 3));
    }

    @Test
    void addEdge_with_weight_throws() {
      assertThrows(IllegalCallerException.class, () -> g.addEdge(1, 3, 4));
    }

    @Test
    void getEdgeWeight_throws() {
      assertThrows(IllegalCallerException.class, () -> g.getEdgeWeight(1, 3));
    }
  }

  @Nested
  class With_Weights {

    @BeforeEach
    void setup() {
      g = new Graph(rows, false, true);
    }

    @Test
    void addEdge_with_weight() {
      assertDoesNotThrow(() -> g.addEdge(1, 3, 2));
      assertEquals(2, g.getNumVertices());
      assertEquals(1, g.getNumEdges());
    }
    
    @Test
    void getEdgeWeight() {
      assertDoesNotThrow(() -> g.addEdge(1, 3, 2));
      assertEquals(2, g.getEdgeWeight(1, 3));
    }

    @Test
    void getEdgeWeight_null_on_invalid_edge() {
      assertEquals(Graph.NIL, g.getEdgeWeight(1, 2));
    }
  }

  @Nested
  class Removal {

    @BeforeEach
    void setup() {
      g = new Graph(rows);
      
      g.addEdge(1, 3);
      g.addEdge(2, 3);
      g.addEdge(0, 2);
    }

    @Test
    void removeVertex() {
      assertEquals(4, g.getNumVertices());
      assertEquals(3, g.getNumEdges());
      assertDoesNotThrow(() -> g.removeVertex(2));
      assertEquals(3, g.getNumVertices());
      assertEquals(1, g.getNumEdges());
    }

    @Test
    void removeEdge() {
      assertEquals(4, g.getNumVertices());
      assertEquals(3, g.getNumEdges());
      assertDoesNotThrow(() -> g.removeEdge(1, 3));
      assertEquals(4, g.getNumVertices());
      assertEquals(2, g.getNumEdges());
    }
  }

}
