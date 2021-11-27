package data_structures.graphs.search;

import data_structures.graphs.Graph;
import data_structures.stacks.Stack;

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
  private static final boolean WHITE = false;

  /**
   * Color constant used to flag a vertex as "discovered"
   */
  private static final boolean GRAY = true;

  /**
   * Vertex node of the Depth-first Search. Used to hold the attributes of DFS.
   */
  public static final class Node extends Graph.Vertex {
    /**
     * The status of the vertex, either undiscovered "WHITE" or discovered "GRAY".
     */
    protected boolean color;

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

    public boolean visited() {
      return color == GRAY;
    }
  }

  // Prevent this class from being instantiated
  public DFS_Stack() { 
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Runs the Depth-first Search algorithm on the supplied graph matrix and start
   * vertex to serve as the root of the DFS tree.
   * 
   * <p>
   * This implementation uses a {@code Stack} to hold the vertices for a given
   * vertice depth path to elminate recursion.
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
    Node[] VTS = new Node[G.getRows()];
    Stack<Integer> S = new Stack<>(G.getRows());
    int time = 0;

    for (int u : G.getVertices())
      VTS[u] = new Node(u);

    S.push(s);

    while (!S.isEmpty()) {
      int u = S.pop();
      VTS[u].distance = ++time;
      VTS[u].color = GRAY;

      for (int v : G.getAdjacentVertices(u)) {
        if (!VTS[v].visited()) {
          VTS[v].predecessor = u;
          S.push(v);
        }
      }

      VTS[u].finish = ++time;
    }

    return VTS;
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
    Node[] results = run(graph, startVertex);
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
   * @return the array of vertices for the path
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    Node[] results = run(graph, startVertex);
    return Graph.arrayPath(results, startVertex, endVertex);
  }

  /**
   * Returns the array of path vertices for the start and end vertices using the
   * results of the DFS algorithm.
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
