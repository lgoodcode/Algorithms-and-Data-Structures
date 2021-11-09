package data_structures.graphs.search;

import static java.util.Arrays.copyOf;

import data_structures.graphs.Graph;
import data_structures.queues.Queue;
import data_structures.queues.exceptions.QueueFullException;

/**
 * BFS(G, s)
 * 1   for each vertex u of G.V - {s}
 * 2       u.color = WHITE
 * 3       u.d = Infinity
 * 4       u.p = NIL
 * 5   s.color = GRAY
 * 6   s.d = 0
 * 7   s.p = NIL
 * 8   Q = 0
 * 9   Enqueue(Q, s)
 * 10  while Q != 0
 * 11      u = Dequeue(Q)
 * 12      for each v of G.Adj[u]
 * 13          if v.color == WHITE
 * 14              v.color = GRAY
 * 15              v.d = u.d + 1
 * 16              v.p = u
 * 17              Enqueue(Q, v)
 * 18      u.color = Black
 */

/**
 * Breadth-first search is one of the simplest algorithms for searching a graph
 * and is the archetype for many important graph algorithms. Prim's
 * minimum-spanning-tree algorithm and Dijkstra's single-source shortest-paths
 * algorithm use ideas similar to those in breadth-first search.
 *
 * <p>
 * Given a graph {@code G = (V, E)} and a distinguished source vertex {@code s},
 * BFS searches systematically to explore the edges of G to "discover" every
 * vertex that is reachable from {@code s}.
 * </p>
 *
 * <p>
 * It computes the distance (smallest number of edges) from {@code s} to each
 * reachable vertex. It also produces a "breadth-first tree" with root {@code s}
 * that contains all reachable vertices. Whenever the search discovers a white
 * vertex {@code v} while scanning the adjacency list of an already discovered
 * vertex u, the vertex {@code v} and the edge {@code (u, v)} are added to the
 * tree. {@code u} is a predecessor or parent of {@code v} in the BFS.
 * </p>
 *
 * <p>
 * The algorithm works on both directed and undirected graphs.
 * </p>
 *
 * <p>
 * This implementation doesn't use the black color to mark a fully discovered
 * vertex because as long it is marked gray, as discovered, it won't be
 * revisted.
 * </p>
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code O(V + E)}</h3>
 *
 * <p>
 * The operations of enqueuing and dequeing take {@code O(1)} time, so the total
 * time devoted to queue operations is {@code O(V)}.
 * </p>
 *
 * <p>
 * Because the procedure scans the adjacency list of each vertex that is
 * dequeued, it scans each adjacency list at most once. Since the sum of the
 * lengths of all the adjacency lists is {@code (-)(E)}, the total time spent in
 * scanning the adjacency lists is {@code O(E)}.
 * </p>
 *
 * <p>
 * The overhead for initialization is {@code O(V)}, and the total running time
 * of the BFS procedure is {@code O(V + E)}. Thus, BFS runs in time linear in
 * the size of the adjacency-list representation of {@code G}.
 * </p>
 */
public final class BFS {
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
   * @param nodes  the BFS results to check against
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
   * start vertex to serve as the root of the BFS tree.
   *
   * @param G the graph matrix
   * @param s the starting vertex
   * @return the {@link BFS.Node} array results
   *
   * @throws IllegalArgumentException if the start vertex is negative or greater
   *                                  than the graph length
   */
  private static Node[] _run(Graph G, int s) {
    checkVertex(G, s);

    int[] V = G.getVertices();
    Node[] VTS = new Node[V.length];
    Queue<Integer> Q = new Queue<>(V.length);
    Graph.Edge[] edges;
    int i, u, v;

    // Initialize BFS vertex nodes
    for (i = 0; i < V.length; i++)
      VTS[i] = new Node(V[i]);

    // Initialize starting vertex as discovered (GRAY), self (0), root (-1)
    VTS[s].color = GRAY;
    VTS[s].distance = 0;

    try {
      Q.enqueue(s);
    } catch (QueueFullException e) {}

    while (!Q.isEmpty()) {
      u = Q.dequeue();
      edges = G.getEdges(u);

      for (i = 0; i < edges.length; i++) {
        v = edges[i].getVertices()[1];

        if (VTS[v].color == WHITE) {
          VTS[v].color = GRAY;
          VTS[v].distance = VTS[u].distance + 1;
          VTS[v].parent = u;

          try {
            Q.enqueue(v);
          } catch (QueueFullException e) {}
        }
      }
    }

    return VTS;
  }

  /**
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the BFS tree.
   *
   * @param graph the graph matrix
   * @param startVertex the starting vertex
   * @return the {@link BFS.Node} array results
   *
   * @throws IllegalArgumentException if the start vertex is negative or greater
   *                                  than the graph length
   */
  public static Node[] run(Graph graph, int startVertex) {
    return _run(graph, startVertex);
  }

  /**
   * Runs the Breadth-First Search algorithm on the supplied graph matrix and
   * start vertex to serve as the root of the BFS tree. Then runs a up-tracing on
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
   * given {@link BFS.Node} results. Checks each parent of end vertex until either
   * the start vertex is reached, resulting in a path, or not, and no path exists
   * between the two vertices.
   *
   * @param nodes       the BFS tree results
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
   * start vertex to serve as the root of the BFS tree. Then runs a up-tracing on
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
   * given {@link BFS.Node} results. Checks each parent of end vertex until either
   * the start vertex is reached, resulting in a path, or not, and no path exists
   * between the two vertices.
   *
   * @param nodes       the BFS tree results
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
   * Vertex node of the Breadth-First Search. Used to hold the attributes of BFS.
   */
  public static final class Node {
    /**
     * The vertex index in the graph.
     */
    protected int vertex;

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
     * Constructs an empty basic BFS node.
     */
    protected Node(int vertex) {
      this.vertex = vertex;
      color = WHITE;
      distance = Integer.MIN_VALUE;
      parent = -1;
    }

    /**
     * Traces the results of the BFS tree with a given start and end vertex, to
     * return a string of the path using the {@code StringBuilder}, if one exists.
     * Otherwise, it will return a no path exists message.
     *
     * @param N  the BFS tree results
     * @param u  the start vertex
     * @param v  the end vertex
     * @param sb the {@code StringBuilder} to build the path string
     */
    private static void printPathAux(Node[] N, int u, int v, StringBuilder sb) {
      if (u == v)
        sb.append(u);
      else if (N[v].parent == -1)
        sb.append("No path exists from " + u + " to " + v);
      else {
        printPathAux(N, u, N[v].parent, sb);
        sb.append(" -> " + v);
      }
    }

    /**
     * Returns the path string for the start and end vertices using the BFS tree
     * results.
     *
     * @param nodes the BFS tree results
     * @param u     the starting vertex of the path
     * @param v     the end vertex of the path
     * @return the string path if one exists or a no path exists message string
     */
    protected static String printPath(Node[] nodes, int u, int v) {
      StringBuilder sb = new StringBuilder();
      printPathAux(nodes, u, v, sb);
      return sb.toString();
    }

    /**
     * Uses a {@link Queue} to queue the vertices of a path from the specified start
     * and end vertices to build an array of the path of vertices.
     *
     * @param N the BFS tree results
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
          Q.enqueue(v);
        }
      } catch (QueueFullException e) {}
    }

    /**
     * Creates an array of the vertices for the path of the specified start and end
     * vertices. If no path exists, it will return an array with a single {@code -1}
     * element.
     *
     * @param nodes the BFS tree results
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
        return copyOf(arr, 0);

      while (!Q.isEmpty())
        arr[i++] = Q.dequeue();
      return copyOf(arr, i);
    }

  }
}
