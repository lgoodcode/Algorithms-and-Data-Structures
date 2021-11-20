package data_structures.graphs.search;

import data_structures.graphs.Graph;
import data_structures.queues.Queue;

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
   * Vertex node of the Breadth-first Search. Used to hold the attributes of BFS.
   */
  public static class Node extends Graph.Vertex {
    /**
     * The status of the vertex, either undiscovered "WHITE" or discovered "GRAY".
     */
    private int color;

    /**
     * Constructs an empty basic BFS node.
     */
    private Node(int vertex) {
      super(vertex);
      color = WHITE;
    }
  }

  // Prevent this class from being instantiated
  public BFS() { 
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Runs the Breadth-first Search algorithm on the supplied graph matrix and
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
    graph.checkVertex(startVertex);
    return _run(graph, startVertex);
  }

  private static Node[] _run(Graph G, int s) {
    int[] V = G.getVertices();
    Node[] VTS = new Node[V.length];
    Queue<Integer> Q = new Queue<>(V.length);
    int i, u, v;

    // Initialize BFS vertex nodes
    for (i = 0; i < V.length; i++)
      VTS[i] = new Node(V[i]);

    // Initialize starting vertex as discovered (GRAY), self (0), root (-1)
    VTS[s].color = GRAY;
    VTS[s].distance = 0;

    Q.enqueue(s);

    while (!Q.isEmpty()) {
      u = Q.dequeue();
      
      for (Graph.Edge edge : G.getEdges(u)) {
        v = edge.getVertices()[1];

        if (VTS[v].color == WHITE) {
          VTS[v].color = GRAY;
          VTS[v].distance = VTS[u].distance + 1;
          VTS[v].predecessor = u;

          Q.enqueue(v);
        }
      }
    }

    return VTS;
  }

  /**
   * Runs the BFS algorithm and returns the path string for the start and end
   * vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = run(graph, startVertex);
    return Graph.printPath(results, startVertex, endVertex);
  }

  /**
   * Returns the path string for the start and end vertices using the results of
   * the BFS algorithm.
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
   * Runs the BFS algorithm and returns the array of path vertices for the start
   * and end vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for the path
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = run(graph, startVertex);
    return Graph.arrayPath(results, startVertex, endVertex);
  }

  /**
   * Returns the array of path vertices for the start and end vertices using the
   * results of the BFS algorithm.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for the path
   */
  public static int[] arrayPath(Node[] nodes, int startVertex, int endVertex) {
    return Graph.arrayPath(nodes, startVertex, endVertex);
  }

}
