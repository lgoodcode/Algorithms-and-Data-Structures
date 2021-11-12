package data_structures.graphs.search;

import data_structures.graphs.Graph;

/**
 * DFS(G)
 * 1   for each vertex u of G.V
 * 2       u.color = WHITE
 * 3       u.p = NIL
 * 4   time = 0
 * 5   for each vertex u of G.V
 * 6       if u.color == WHITE
 * 7           DFS-Visit(G, u)
 *
 *
 * DFS-Visit(G, u)
 * 1   time = time + 1
 * 2   u.d = time
 * 3   u.color = GRAY
 * 4   for each vertex v of G.Adj[u]
 * 5       if v.color == WHITE
 * 6           v.p = u
 * 7           DFS-Visit(G, v)
 * 8   u.color = BLACK
 * 9   time = time + 1
 * 10  u.f = time
 */

/**
 * Depth-first Search searches "deeper" in the graph whenever possible. DFS
 * explores edges out of the most recently discovered vertex {@code v} that
 * still has unexplored edges leaving it.
 *
 * <p>
 * Once all of the edges of {@code v} have been explored, the search
 * "backtracks" to explore edges leaving the vertex from which {@code v} was
 * discovered. This process continues until we have discovered all the vertices
 * that are reachable from the original source vertex. If any undiscovered
 * vertices remain then the DFS selects one of them as a new source, and repeats
 * the search from that source. The algorithm repeats this entire process until
 * it has discovered every vertex.
 * </p>
 *
 * <p>
 * DFS produces a "predecessor subgraph", similar to how DFS produces a tree,
 * since it searches all possible paths for a given vertex before continuing to
 * the next.mThe predecessor subgraph may be composed of several trees because
 * the search may repeat from multiple sources. The predecessor subgraph of a
 * DFS forms a "Depth-first Forest" comprising of several "depth-first trees".
 * The edges {@code E.p} are "tree edges".
 * </p>
 *
 * <p>
 * DFS also "timestamps" each vertex with {@code v.d} to record when {@code v}
 * is first discovered (and grayed) and the second {@code v.f} records when the
 * search finishes examining the adajency list of {@code v} and blackens
 * {@code v}.
 * </p>
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code (-)(V + E)}</h3>
 *
 * <p>
 * The loops on lines 1-3 and lines 5-7 take time (-)(V), exclusive of the time
 * to execute the calls to DFS-Visit. The DFS-Visit procedure is called exactly
 * once for each vertex v of V. since the vertex u where DFS-Visit is invoked
 * must be white and the first thing DFS-Visit does it paint vertex u gray.
 * </p>
 *
 * <p>
 * Total cost of executing lines 4-7 of DFS-Visit is (-)(E), thus resulting in
 * the running time of (-)(V + E).
 * </p>
 */
public final class DFS {
  /**
   * Color constant used to flag a vertex as "undiscovered"
   */
  private static final int WHITE = 0;

  /**
   * Color constant used to flag a vertex as "discovered"
   */
  private static final int GRAY = 1;

  /**
   * Vertex node of the Depth-first Search. Used to hold the attributes of DFS.
   */
  public static final class Node extends Graph.Vertex {
    /**
     * The status of the vertex, either undiscovered "WHITE" or discovered "GRAY".
     */
    protected int color;

    /**
     * The number of vertices visited before this node was compeleted.
     */
    protected int finish;

    /**
     * Constructs an empty basic DFS node.
     */
    private Node(int vertex) {
      super(vertex);
      color = WHITE;
      finish = Integer.MIN_VALUE;
    }
  }

  /**
   * Runs the Depth-first Search algorithm on the supplied graph matrix and start
   * vertex to serve as the root of the DFS tree.
   * 
   * <p>
   * This uses a single element array to hold the {@code time} value so that it
   * can be passed through each recursive call.
   * </p>
   *
   * <p>
   * This implementation omits the use of the "black" color, which marks a vertex
   * as completed, which isn't necessary because the vertex won't be revisted as
   * long as it isn't white.
   * </p>
   * 
   * @param graph       the graph matrix
   * @param startVertex the starting vertex
   * @return the {@link DFS.Node} array results
   *
   * @throws IllegalArgumentException if the start vertex is negative or greater
   *                                  than the graph length
   */
  public static Node[] run(Graph graph, int startVertex) {
    graph.checkVertex(startVertex);
    return _run(graph, startVertex);
  }

  private static Node[] _run(Graph G, int s) {
    int[] V = G.getVertices(), time = { 0 };
    Node[] VTS = new Node[V.length];
    int i, u;

    // Initialize DFS vertex nodes
    for (i = 0; i < V.length; i++)
      VTS[i] = new Node(V[i]);

    for (i = 0; i < V.length; i++) {
      u = V[i];

      if (VTS[u].color == WHITE)
        visit(G, VTS, u, time);
    }

    return VTS;
  }

  private static void visit(Graph G, Node[] VTS, int u, int[] time) {
    VTS[u].distance = ++time[0];
    VTS[u].color = GRAY;

    Graph.Edge[] edges = G.getEdges(u);
    int i, v;

    for (i = 0; i < edges.length; i++) {
      v = edges[i].getVertices()[1];

      if (VTS[v].color == WHITE) {
        VTS[v].predecessor = u;
        visit(G, VTS, v, time);
      }
    }

    VTS[u].finish = ++time[0];
  }

  /**
   * Runs the DFS algorithm and returns the path string for the start and end
   * vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = _run(graph, startVertex);
    return Graph.printPath(results, startVertex, endVertex);
  }

  /**
   * Returns the path string for the start and end vertices using the results of
   * the DFS algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static String printPath(Node[] nodes, int startVertex, int endVertex) {
    return Graph.printPath(nodes, startVertex, endVertex);
  }

  /**
   * Runs the DFS algorithm and returns the array of path vertices for the start
   * and end vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = _run(graph, startVertex);
    return Graph.arrayPath(results, startVertex, endVertex);
  }

  /**
   * Returns the array of path vertices for the start and end vertices using the
   * results of the DFS algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static int[] arrayPath(Node[] nodes, int startVertex, int endVertex) {
    return Graph.arrayPath(nodes, startVertex, endVertex);
  }

}
