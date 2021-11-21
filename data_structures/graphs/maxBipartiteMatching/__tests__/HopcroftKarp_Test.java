package data_structures.graphs.maxBipartiteMatching.__tests__;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;

import data_structures.graphs.Graph;
import data_structures.graphs.maxBipartiteMatching.HopcroftKarp;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class HopcroftKarp_Test {
  Graph G = new Graph(10, true, true);

  @BeforeEach
  void setup() {
    G.addEdge(1, 6, 4);
    G.addEdge(2, 6, 3);
    G.addEdge(2, 8, 5);
    G.addEdge(3, 7, 9);
    G.addEdge(3, 8, 4);
    G.addEdge(3, 9, 2);
    G.addEdge(4, 8, 5);
    G.addEdge(5, 8, 2); 
  }

  @Test
  void throws_when_instantiated() {
    assertThrows(NoClassDefFoundError.class, () -> new HopcroftKarp());
  }

  @Test
  void total() {
    assertEquals(3, HopcroftKarp.total(G));
  }

  @Test
  void matches() {
    int[] matches = HopcroftKarp.matches(G);
    int numMatches = 0;

    for (int x : matches)
      if (x != Graph.NIL)
        numMatches++;

    assertEquals(3, numMatches);
  }

  @Test
  void printMatches() {
    assertEquals("{\n"
      + "\s\s1 -> 6\n"
      + "\s\s2 -> 8\n"
      + "\s\s3 -> 7\n"
      + "}", HopcroftKarp.printMatches(G));
  }

  @Test
  void printMatches_from_results() {
    int[] matches = HopcroftKarp.matches(G);

    assertEquals("{\n"
      + "\s\s1 -> 6\n"
      + "\s\s2 -> 8\n"
      + "\s\s3 -> 7\n"
      + "}", HopcroftKarp.printMatches(matches));
  }
}
