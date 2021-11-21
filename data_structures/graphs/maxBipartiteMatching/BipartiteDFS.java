package data_structures.graphs.maxBipartiteMatching;

import static java.util.Arrays.fill;

import data_structures.graphs.Graph;

/**
 * Uses Depth-first search algorithm to visit all the possible matches.
 */
public final class BipartiteDFS {
  private static final boolean WHITE = false;
  private static final boolean GRAY = true;
  private static final boolean TOTAL = false;
  private static final boolean MATCHES = true;

  public static class Node extends Graph.Vertex {
    private boolean color;

    private Node(int vertex) {
      super(vertex);
      color = WHITE;
    }
  }
  
  // Prevent this class from being instantiated
  public BipartiteDFS() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  @SuppressWarnings("unchecked")
  private static <T> T _run(boolean type, Graph G) {
    int n = G.getRows();
    Node[] VTS = new Node[n];
    int[] M = new int[n];
    Integer maxMatches = 0;

    // Initialize matches of each vertex to NIL
    fill(M, Graph.NIL);

    // Initialize nodes to unvisited
    for (int u = 0; u < n; u++)
      VTS[u] = new Node(u);

    // For every vertex u of set R of G
    for (int u = 0; u < n; u++) {
      // Find a match for a vertex v of set R of G
      if (G.hasVertex(u) && VTS[u].color == WHITE && findMatch(G, VTS, M, n, u))
        maxMatches++;
    }

    return type == TOTAL ? (T) maxMatches : (T) M;
  }

  private static boolean findMatch(Graph G, Node[] VTS, int[] M, int n, int u) {
    for (int v = 0; v < n; v++) {
      if (G.hasEdge(u, v) && VTS[v].color == WHITE) {
        VTS[v].color = GRAY;

        if (M[v] == Graph.NIL || findMatch(G, VTS, M, n, v)) {
          M[v] = u;
          // Set the source vertex to visited so it isn't used again
          VTS[u].color = GRAY;
          return true;
        }
      }
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  private static <T> T _runRange(boolean type, Graph G, int l, int r) {
    Node[] VTS = new Node[r];
    int[] M = new int[r];
    Integer maxMatches = 0;

    // Initialize matches of each vertex to NIL
    fill(M, Graph.NIL);

    // Initialize the nodes to unvisited
    for (int i = 0; i < r; i++)
      VTS[i] = new Node(i);

    for (int u = 0; u < l; u++) {
      if (G.hasVertex(u) && findMatchRange(G, VTS, M, l, r, u))
        maxMatches++;
    }

    return type == TOTAL ? (T) maxMatches : (T) M;
  }

  private static boolean findMatchRange(Graph G, Node[] VTS, int[] M, int l, int r, int u) {
    for (int v = l, n = r; v < n; v++) {
      if (G.hasEdge(u, v) && VTS[v].color == WHITE) {
        VTS[v].color = GRAY;

        if (M[v] == Graph.NIL || findMatchRange(G, VTS, M, l, r, v)) {
          M[v] = u;
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Finds the maximum cardinality of bipartite matches for each pair of vertices
   * {@code (i, j)} of each edge {@code E}.
   * 
   * @param graph the graph to find the maximum bipartite matchings of
   * @return the maximum cardinality of matches
   */
  public static int totalMatches(Graph graph) {
    return _run(TOTAL, graph);
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
    return _run(MATCHES, graph);
  }
  
  /**
   * Finds the maximum cardinality of bipartite matches for each pair of vertices
   * {@code (i, j)} of each edge {@code E}. Finds the matches within a range of
   * two sets where the number of vertices is split by the starting vertex of each
   * set.
   * 
   * @param graph the graph to find the maximum bipartite matchings of
   * @param left  the end of the first set of vertices to match
   * @param right the end of the right set of vertices to match
   * @return the maximum cardinality of matches
   */
  public static int totalMatchesRange(Graph graph, int left, int right) {
    if (left > right)
      throw new IllegalArgumentException("The left vertices cannot be less than the right vertices.");
    Graph.checkVertex(graph.getRows(), left);
    Graph.checkVertex(graph.getRows(), right);
    return _runRange(TOTAL, graph, left, right);
  }

  /**
   * Finds the maximum cardinality of bipartite matches for each pair of vertices
   * {@code (i, j)} of each edge {@code E}. Finds the matches within a range of
   * two sets where the number of vertices is split by the starting vertex of each
   * set. Returns the array of matches where the indices represent the vertices.
   * 
   * @param graph the graph to find the maximum bipartite matchings of
   * @param left  the end of the first set of vertices to match
   * @param right the end of the right set of vertices to match
   * @return the maximum cardinality of matches
   */
  public static int[] matchesRange(Graph graph, int left, int right) {
    if (left > right)
      throw new IllegalArgumentException("The left vertices cannot be less than the right vertices.");
    Graph.checkVertex(graph.getRows(), left);
    Graph.checkVertex(graph.getRows(), right);
    return _runRange(MATCHES, graph, left, right);
  }
}
