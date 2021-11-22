package data_structures.graphs.flowNetworks.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.flowNetworks.FlowNetwork;
import data_structures.graphs.flowNetworks.FordFulkerson;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FordFulkerson_Test {
  FlowNetwork G = new FlowNetwork(10);

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
    assertThrows(NoClassDefFoundError.class, () -> new FordFulkerson());
  }

  @Test
  void fordFulkerson() {
    assertEquals(23, FordFulkerson.maxFlow(G, 0, 5));
  }
}
