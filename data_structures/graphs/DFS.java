package data_structures.graphs;

import java.util.Arrays;

import data_structures.queues.Queue;
import data_structures.queues.exceptions.QueueFullException;

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
 * DFS forms a "Depth-First Forest" comprising of several "depth-first trees".
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
 * <h3>Aggregate Analysis</h3>
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
   * Checks if the given vertex is a valid index for the given number of rows of
   * the graph or {@code Node} results.
   *
   * @param rows   the maximum number of rows
   * @param vertex the vertex index to check
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   */
  private static void checkVertex(int rows, int vertex) {
    if (vertex < 0)
      throw new IllegalArgumentException("Vertex cannot be negative.");
    if (vertex >= rows)
      throw new IllegalArgumentException("Vertex cannot be greater than graph length.");
  }

  /**
   * Checks if the given vertex is a valid index for the given number of rows of
   * the graph.
   *
   * @param graph  the graph to validate the vertex with
   * @param vertex the vertex index to check
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   */
  private static void checkVertex(Graph graph, int vertex) {
    checkVertex(graph.rows, vertex);
  }

  /**
   * Checks if the given vertex is a valid index for the {@code Node} results.
   *
   * @param nodes  the DFS results to check against
   * @param vertex the vertex index to check
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the graph length
   */
  private static void checkVertex(Node[] nodes, int vertex) {
    checkVertex(nodes.length, vertex);
  }

  /**
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the DFS tree.
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
   * @param G the graph matrix
   * @param s the starting vertex
   * @return the {@link DFS.Node} array results
   *
   * @throws IllegalArgumentException if the start vertex is negative or greater
   *                                  than the graph length
   */
  private static Node[] _run(Graph G, int s) {
    checkVertex(G, s);

    int[] vertices = G.getVertices(), time = { 0 };
    int u, numVertices = vertices.length;
    Node[] VTS = new Node[numVertices];

    // Initialize DFS vertex nodes
    for (u = 0; u < numVertices; u++)
      VTS[u] = new Node();

    for (u = 0; u < numVertices; u++)
      if (VTS[u].color == WHITE)
        visit(G, VTS, u, time);
    return VTS;
  }

  private static void visit(Graph G, Node[] VTS, int u, int[] time) {
    VTS[u].distance = ++time[0];
    VTS[u].color = GRAY;

    for (int v = 0, len = G.rows; v < len; v++) {
      if (G.hasEdge(u, v) && VTS[v].color == WHITE) {
        VTS[v].parent = u;
        visit(G, VTS, v, time);
      }
    }

    VTS[u].finish = ++time[0];
  }

  /**
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the DFS tree.
   *
   * @param graph the graph matrix
   * @param startVertex the starting vertex
   * @return the {@link DFS.Node} array results
   *
   * @throws IllegalArgumentException if the start vertex is negative or greater
   *                                  than the graph length
   */
  public static Node[] run(Graph graph, int startVertex) {
    return _run(graph, startVertex);
  }

  /**
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the DFS tree. Then runs a up-tracing on
   * the specified end vertex, checking each parent of end vertex until either the
   * start vertex is reached, resulting in a path, or not, and no path exists
   * between the two vertices.
   *
   * @param graph       the graph matrix
   * @param startVertex the starting vertex
   * @param endVertex   the end vertex
   *
   * @return the path string if found, or a no path found message
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    checkVertex(graph, endVertex);
    Node[] nodes = _run(graph, startVertex);
    return Node.printPath(nodes, startVertex, endVertex);
  }

  /**
   * Performs the path tracing for the specified start and end vertex with the
   * given {@link DFS.Node} results. Checks each parent of end vertex until either
   * the start vertex is reached, resulting in a path, or not, and no path exists
   * between the two vertices.
   *
   * @param nodes       the DFS tree results
   * @param startVertex the starting vertex
   * @param endVertex   the end vertex
   *
   * @return the path string if found, or a no path found message
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public static String printPath(Node[] nodes, int startVertex, int endVertex) {
    checkVertex(nodes, endVertex);
    return Node.printPath(nodes, startVertex, endVertex);
  }

  /**
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the DFS tree. Then runs a up-tracing on
   * the specified end vertex, checking each parent of end vertex until either the
   * start vertex is reached, resulting in a path, or not, and no path exists
   * between the two vertices.
   *
   * @param graph       the graph matrix
   * @param startVertex the starting vertex
   * @param endVertex   the end vertex
   *
   * @return the path array if found, or an array of a single {@code -1} element
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    checkVertex(graph, endVertex);
    Node[] nodes = _run(graph, startVertex);
    return Node.arrayPath(nodes, startVertex, endVertex);
  }

  /**
   * Performs the path tracing for the specified start and end vertex with the
   * given {@link DFS.Node} results. Checks each parent of end vertex until either
   * the start vertex is reached, resulting in a path, or not, and no path exists
   * between the two vertices.
   *
   * @param nodes       the DFS tree results
   * @param startVertex the starting vertex
   * @param endVertex   the end vertex
   *
   * @return the path array if found, or an array of a single {@code -1} element
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the graph length
   */
  public static int[] arrayPath(Node[] nodes, int startVertex, int endVertex) {
    checkVertex(nodes, endVertex);
    return Node.arrayPath(nodes, startVertex, endVertex);
  }

  /**
   * Vertex node of the Breadth-First Search. Used to hold the attributes of DFS.
   */
  public static class Node {
    /**
     * The status of the vertex, either undiscovered "WHITE" or discovered "GRAY".
     */
    protected int color;

    /**
     * The number of nodes from the start vertex to the current vertex.
     */
    protected int distance;

    /**
     * The preceding vertex on a given path.
     */
    protected int parent;

    /**
     * The number of vertices visited before this node was compeleted.
     */
    protected int finish;

    /**
     * Constructs an empty basic DFS node.
     */
    protected Node() {
      color = WHITE;
      distance = Integer.MIN_VALUE;
      parent = -1;
      finish = Integer.MIN_VALUE;
    }

    /**
     * Traces the results of the DFS tree with a given start and end vertex, to
     * return a string of the path. Because the DFS produces a forest of subtrees
     * for each individual vertex traveled, this print function differs from the BFS
     * implementation. This will return {@code null} if the start vertex wasn't
     * reached from the end vertex and any node traveled up to this path will check
     * if the path it traveled reached a dead end and returns {@code null} back up
     * the call stack. If there is a path, it will instead pass the string back up.
     *
     * @param N   the DFS tree results
     * @param u   the start vertex
     * @param v   the end vertex
     * @param str the string holding the path or {@code null} if none
     */
    private static String printPathAux(Node[] N, int u, int v, String str) {
      if (u == v)
        return str == null ? null : str + u;
      else if (N[v].parent == -1)
        return null;
      String s = printPathAux(N, u, N[v].parent, str);
      return s == null ? null : s + " -> " + v;
    }

    /**
     * Returns the path string for the start and end vertices using the DFS tree
     * results.
     *
     * @param nodes the DFS tree results
     * @param u     the starting vertex of the path
     * @param v     the end vertex of the path
     * @return the string path if one exists or a no path exists message string
     */
    protected static String printPath(Node[] nodes, int u, int v) {
      String path = printPathAux(nodes, u, v, "");
      return path != null ? path : "No path exists from " + u + " to " + v;
    }

    /**
     * Uses a {@link Queue} to queue the vertices of a path from the specified start
     * and end vertices to build an array of the path of vertices. Because of the
     * DFS forest of subtrees, it could travel a path that results in not a complete
     * path and add the nodes, so it peeks the last queued item to check if it is
     * {@code -1} and if so, doesn't enqueue the vertex.
     *
     * @param N the DFS tree results
     * @param u the starting vertex of the path
     * @param v the end vertex of the path
     * @param Q the queue to hold the vertices of the path
     */
    private static void arrayPathAux(Node[] N, int u, int v, Queue<Integer> Q) {
      try {
        if (u == v)
          Q.enqueue(u);
        else if (N[v].parent == -1)
          Q.enqueue(-1);
        else {
          arrayPathAux(N, u, N[v].parent, Q);

          if (Q.peek() != -1)
            Q.enqueue(v);
        }
      } catch (QueueFullException e) {}
    }

    /**
     * Creates an array of the vertices for the path of the specified start and end
     * vertices. If no path exists, it will return an array with a single {@code -1}
     * element.
     *
     * @param nodes the DFS tree results
     * @param u     the start vertex of the path
     * @param v     the end vertex of the path
     * @return the array of vertices for a path, or a single {@code -1} element if
     *         no path exists
     */
    protected static int[] arrayPath(Node[] nodes, int u, int v) {
      Queue<Integer> Q = new Queue<>(nodes.length);
      int[] arr = new int[nodes.length];
      int i = 0;

      arrayPathAux(nodes, u, v, Q);

      if (Q.isEmpty())
        return Arrays.copyOf(arr, 0);

      while (!Q.isEmpty())
        arr[i++] = Q.dequeue();
      return Arrays.copyOf(arr, i);
    }

  }
}
