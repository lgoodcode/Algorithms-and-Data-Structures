package data_structures.graphs.maxBipartiteMatching.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.flowNetworks.FlowNetwork;
import data_structures.graphs.maxBipartiteMatching.FordFulkersonMatching;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class FordFulkersonMatching_Test {
  FlowNetwork G = new FlowNetwork(10);

  @BeforeEach
  void setup() {
    G.addEdge(1, 6, 1);
    G.addEdge(2, 6, 1);
    G.addEdge(2, 8, 1);
    G.addEdge(3, 7, 1);
    G.addEdge(3, 8, 1);
    G.addEdge(3, 9, 1);
    G.addEdge(4, 8, 1);
    G.addEdge(5, 8, 1); 
  }

  @Test
  void throws_when_instantiated() {
    assertThrows(NoClassDefFoundError.class, () -> new FordFulkersonMatching());
  }

  @Test
  void total() {
    assertEquals(3, FordFulkersonMatching.totalMatches(G, 5, 9));
  }

}
