package data_structures.graphs.flowNetworks.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.flowNetworks.FlowNetwork;
import data_structures.graphs.flowNetworks.EdmondKarp;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class EdmondKarp_Test {
  FlowNetwork G = new FlowNetwork(10);
  String[] stringPaths = {
    "12: 0 -> 1 -> 3 -> 5",
    "4: 0 -> 2 -> 4 -> 5",
    "7: 0 -> 2 -> 4 -> 3 -> 5"
  };
  Integer[][] arrayPaths = {
    { 12, 0, 1, 3, 5 },
    { 4, 0, 2, 4, 5 },
    { 7, 0, 2, 4, 3, 5 }
  };

  @BeforeEach
  void setup() {
    G.addEdge(0, 1, 16);
    G.addEdge(0, 2, 13);
    G.addEdge(2, 1, 4);
    G.addEdge(1, 3, 12);
    G.addEdge(2, 4, 14);
    G.addEdge(3, 2, 9);
    G.addEdge(3, 5, 20);
    G.addEdge(4, 5, 4);
    G.addEdge(4, 3, 7); 
  }

  @Test
  void throws_when_instantiated() {
    assertThrows(NoClassDefFoundError.class, () -> new EdmondKarp());
  }

  @Test
  void edmondKarp() {
    assertEquals(23, EdmondKarp.maxFlow(G, 0, 5));
  }

  @Test
  void string_paths() {
    assertArrayEquals(stringPaths, EdmondKarp.maxFlowPaths(G, 0, 5));
    String[] p = EdmondKarp.maxFlowPaths(G, 0, 5);
    assertEquals(stringPaths[0], p[0]);
    assertEquals(stringPaths[1], p[1]);
    assertEquals(stringPaths[2], p[2]);
  }

  @Test
  void no_string_path() {
    assertEquals(0, EdmondKarp.maxFlowPaths(G, 0, 0).length);
  }

  @Test
  void array_paths() {
    assertArrayEquals(arrayPaths, EdmondKarp.maxFlowArray(G, 0, 5));
    Integer[][] arr = EdmondKarp.maxFlowArray(G, 0, 5);
    assertArrayEquals(arrayPaths[0], arr[0]);
    assertArrayEquals(arrayPaths[1], arr[1]);
    assertArrayEquals(arrayPaths[2], arr[2]);
  }

  @Test
  void no_array_path() {
    assertArrayEquals(new Integer[0][], EdmondKarp.maxFlowArray(G, 0, 0));
  }
}
