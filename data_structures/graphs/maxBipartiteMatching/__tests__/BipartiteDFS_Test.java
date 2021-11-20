package data_structures.graphs.maxBipartiteMatching.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.maxBipartiteMatching.BipartiteDFS;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class BipartiteDFS_Test {
  Graph G = new Graph(10);

  @BeforeEach
  void setup() {
    G.addEdge(1, 6);
    G.addEdge(2, 6);
    G.addEdge(2, 8);
    G.addEdge(3, 7);
    G.addEdge(3, 8);
    G.addEdge(3, 9);
    G.addEdge(4, 8);
    G.addEdge(5, 8); 
  }

  @Test
  void throws_when_instantiated() {
    assertThrows(NoClassDefFoundError.class, () -> new BipartiteDFS());
  }

  @Test
  void total() {
    assertEquals(3, BipartiteDFS.totalMatches(G));
  }

  @Test
  void matches() {
    int[] matches = BipartiteDFS.matches(G);
    int numMatches = 0;

    for (int x : matches)
      if (x != Graph.NIL)
        numMatches++;

    assertEquals(3, numMatches);
  }

  @Test
  void range_total() {
    assertEquals(3, BipartiteDFS.totalMatchesRange(G, 5, 9));
  }

  @Test
  void range_matches() {
    int[] matches = BipartiteDFS.matchesRange(G, 5, 9);
    int numMatches = 0;

    for (int x : matches)
      if (x != Graph.NIL)
        numMatches++;

    assertEquals(3, numMatches);
  }
}
