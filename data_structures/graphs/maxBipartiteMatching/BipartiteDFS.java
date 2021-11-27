package data_structures.graphs.maxBipartiteMatching;

import static java.util.Arrays.fill;

import data_structures.graphs.Graph;

/**
 * Uses Depth-first search algorithm to visit all the possible matches.
 */
public final class BipartiteDFS extends BipartiteMatchingAlgorithm {
  private static final boolean WHITE = false;
  private static final boolean GRAY = true;

  private static class Node extends Graph.Vertex {
    boolean color;

    Node(int vertex) {
      super(vertex);
      color = WHITE;
    }

    boolean visited() {
      return color == GRAY;
    }
  }

  public BipartiteDFS() {
    super();
  }

  /**
   * Finds the maximum cardinality of bipartite matches for each pair of vertices
   * {@code (i, j)} of each edge {@code E}.
   *
   * @param graph the graph to find the maximum bipartite matchings of
   * @return the maximum cardinality of matches
   */
  public static int totalMatches(Graph graph) {
    checkGraph(graph);
    return run(TOTAL, graph);
  }

  /**
   * Finds the maximum cardinality of bipartite matches for each pair of vertices
   * {@code (i, j)} of each edge {@code E}. The array of matches uses the indices
   * to represent the vertices.
   *
   * @param graph the graph to find the maximum bipartite matchings of
   * @return the array of matches
   */
  public static int[] matches(Graph graph) {
    checkGraph(graph);
    return run(MATCHES, graph);
  }

  /**
   * Runs the maximum bipartite matching algorithm on the specified directed graph
   * where the edges are possible pairs and finds the maximum cardinality of
   * possible pairs. Will return a string representing the pairs.
   *
   * @param graph the directed graph to run the algorithm on
   * @return the string of matches
   *
   * @throws IllegalArgumentException if the specified graph is not directed
   */
  public static String printMatches(Graph graph) {
    return printMatches(matches(graph));
  }

  @SuppressWarnings("unchecked")
  private static <T> T run(boolean type, Graph G) {
    int maxMatches = 0, n = G.getRows();
    Node[] VTS = new Node[n];
    int[] V = G.getVertices(), M = new int[n];

    // Initialize matches of each vertex to NIL
    fill(M, Graph.NIL);

    // Initialize nodes to unvisited
    for (int u : V)
      VTS[u] = new Node(u);

    // For every vertex u of set L of G
    for (int u : V) {
      // Find a match for a vertex v of set R of G
      if (!VTS[u].visited() && findMatch(G, VTS, M, n, u))
        maxMatches++;
    }

    return type == TOTAL ? (T) Integer.valueOf(maxMatches) : (T) M;
  }

  private static boolean findMatch(Graph G, Node[] VTS, int[] M, int n, int u) {
    for (int v : G.getAdjacentVertices(u)) {
      if (!VTS[v].visited()) {
        VTS[v].color = GRAY;

        if (M[v] == Graph.NIL || findMatch(G, VTS, M, n, v)) {
          VTS[u].color = GRAY;
          M[u] = v;
          return true;
        }
      }
    }
    return false;
  }
}
