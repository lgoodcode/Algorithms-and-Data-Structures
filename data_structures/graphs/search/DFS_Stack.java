package data_structures.graphs.search;

import static java.util.Arrays.copyOf;

import data_structures.graphs.Graph;
import data_structures.stacks.Stack;
import data_structures.queues.Queue;


/**
 * Depth-first Search searches "deeper" in the graph whenever possible. DFS
 * explores edges out of the most recently discovered vertex {@code v} that
 * still has unexplored edges leaving it.
 * 
 * <p>
 * This implementation uses a {@code Stack} to eliminate recursion.
 * </p>
 */
public final class DFS_Stack {
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
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the DFS tree.
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

    int[] V = G.getVertices();
    Node[] VTS = new Node[V.length];
    Stack<Integer> S = new Stack<>(V.length);
    int i, u, v, time = 0;
    Graph.Edge[] edges;

    // Initialize DFS vertex nodes
    for (i = 0; i < V.length; i++)
      VTS[i] = new Node(V[i]);

    S.push(s);

    while (!S.isEmpty()) {
      u = S.pop();

      VTS[u].distance = ++time;
      VTS[u].color = GRAY;
      edges = G.getEdges(u);

      for (i = 0; i < edges.length; i++) {
        v = edges[i].getVertices()[1];
  
        if (VTS[v].color == WHITE) {
          VTS[v].predecessor = u;
          S.push(v);
        }
      }
  
      VTS[u].finish = ++time;     
    }

    return VTS;
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
    else if (N[v].predecessor == -1)
      return null;
    String s = printPathAux(N, u, N[v].predecessor, str);
    return s == null ? null : s + " -> " + v;
  }

  /**
   * Returns the path string for the start and end vertices using the DFS tree
   * results.
   *
   * @param nodes       the DFS tree results
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static String printPath(Node[] nodes, int startVertex, int endVertex) {
    String path = printPathAux(nodes, startVertex, endVertex, "");
    return path != null ? path : "No path exists from " + startVertex + " to " + endVertex;
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
    String path = printPathAux(results, startVertex, endVertex, "");
    return path != null ? path : "No path exists from " + startVertex + " to " + endVertex;
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
    if (u == v)
      Q.enqueue(u);
    else if (N[v].predecessor == -1)
      Q.enqueue(-1);
    else {
      arrayPathAux(N, u, N[v].predecessor, Q);

      if (Q.peek() != -1)
        Q.enqueue(v);
    }
  }

  /**
   * Creates an array of the vertices for the path of the specified start and end
   * vertices. If no path exists, it will return an array with a single {@code -1}
   * element.
   *
   * @param nodes       the {@code Vertex} subclass containing the data from an
   *                    algorithm to build a path
   * @param startVertex the start vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for a path, or a single {@code -1} element if
   *         no path exists
   */
  public static final int[] arrayPath(Node[] nodes, int startVertex, int endVertex) {
    Queue<Integer> Q = new Queue<>(nodes.length);
    int[] arr = new int[nodes.length];
    int i = 0;

    arrayPathAux(nodes, startVertex, endVertex, Q);

    if (Q.isEmpty())
      return copyOf(arr, 0);

    while (!Q.isEmpty())
      arr[i++] = Q.dequeue();
    return copyOf(arr, i);
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
    return arrayPath(results, startVertex, endVertex);
  }

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

}
