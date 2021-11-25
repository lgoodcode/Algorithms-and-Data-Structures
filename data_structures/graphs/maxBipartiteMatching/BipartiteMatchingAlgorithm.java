package data_structures.graphs.maxBipartiteMatching;

import data_structures.graphs.Graph;

public abstract class BipartiteMatchingAlgorithm {
  protected static final boolean TOTAL = false;
  protected static final boolean MATCHES = true;

  // Prevent this class from being instantiated
  public BipartiteMatchingAlgorithm() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Checks to ensure the graph is directed and weighted for the Bipartite
   * matching algorithm.
   *
   * @param graph the graph to check
   *
   * @throws IllegalArgumentException if the graph is not directed and weighted
   */
  protected static void checkGraph(Graph graph) {
    if (!graph.isDirected())
      throw new IllegalArgumentException("Graph must be directed.");
    if (!graph.isWeighted())
      throw new IllegalArgumentException("Graph must be weighted.");
  }

  /**
   * Prints the matches from the results of the maximum bipartite matching
   * algorithm where the indices are the vertices and the values are the matching
   * vertices.
   *
   * @param matches the array of matches
   * @return the string of matches
   */
  public static String printMatches(int[] matches) {
    StringBuilder sb = new StringBuilder("{\n");

    for (int i = 0, len = matches.length; i < len; i++) {
      if (matches[i] != Graph.NIL)
        sb.append("\s\s" + i + " -> " + matches[i] + "\n");
    }

    return sb.toString() + "}";
  }
}
