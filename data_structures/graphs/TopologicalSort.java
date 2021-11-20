package data_structures.graphs;

import data_structures.linkedLists.LinkedList;

/**
 * Depth-first search can be used to perform a topological sort of a directed
 * acylic graph or <i><b>DAG</b></i>. A topological sort of a DAG is a linear
 * ordering of all its vertices such that if graph {@code G} contains an edge
 * {@code (u, v)}, then {@code u} appears before {@code v} in the ordering. (If
 * a graph contains a cycle then no linear ordering is possible.)
 *
 * <p>
 * Many applications use directed acylic graphs to indicate precedences among
 * events. An example given in the book is clothing; socks before shoes, shirt
 * before tie, undershorts before pants before a belt. A directed edge
 * {@code (u, v)} means {@code u} precedes {@code v}.
 * </p>
 *
 * <p>
 * Topological sort algorithm for a dag is simply:
 * </p>
 *
 * <ol>
 * <li>call {@code DFS(G)} to compute finishing times {@code v.f} for each
 * vertex {@code v}</li>
 * <li>as each vertex is finished, insert it onto the front of a linked
 * list</li>
 * <li>return the linked list of vertices</li>
 * </ol>
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code (-)(V + E)}</h3>
 *
 * <p>
 * Topological sort runs in time {@code (-)(V + E)}, since DFS takes
 * {@code (-)(V + E)} time and it takes {@code O(1)} time to insert each of the
 * {@code |V|} vertices onto the front of the linked list.
 * </p>
 */
public final class TopologicalSort {
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

  // Prevent this class from being instantiated
  public TopologicalSort() { 
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Runs the Depth-first Search algorithm on the supplied graph matrix and start
   * vertex to serve as the root of the DFS tree and inserts all the vertices, in
   * order, into a {@code LinkedList}.
   *
   * @param graph       the graph matrix
   * @param startVertex the starting vertex
   * @return the {@code LinkedList} of vertices in order
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start vertex
   *                                  is invalid
   */
  public static LinkedList<Node> run(Graph graph, int startVertex) {
    if (!graph.directed && !graph.weighted)
      throw new IllegalArgumentException("The algorithm can only run on a directed weighted graph.");
    graph.checkVertex(startVertex);
    return _run(graph, startVertex);
  }

  private static LinkedList<Node> _run(Graph G, int s) {
    int u, V = G.rows;
    int[] time = { 0 };
    Node[] VTS = new Node[V];
    LinkedList<Node> L = new LinkedList<>();

    for (u = 0; u < V; u++)
      VTS[u] = new Node(u);

    for (u = 0; u < V; u++) {
      if (VTS[u].color == WHITE)
        visit(G, VTS, L, u, time);
    }

    return L;
  }

  private static void visit(Graph G, Node[] VTS, LinkedList<Node> L, int u, int[] time) {
    // If vertex doesn't exist in the graph, don't check for edges
    if (!G.hasVertex(u))
      return;

    VTS[u].distance = ++time[0];
    VTS[u].color = GRAY;

    Graph.Edge[] edges = G.getEdges(u);
    int i, v;

    for (i = 0; i < edges.length; i++) {
      v = edges[i].getVertices()[1];

      if (VTS[v].color == WHITE) {
        VTS[v].predecessor = u;
        visit(G, VTS, L, v, time);
      }
    }

    VTS[u].finish = ++time[0];
    L.insert(VTS[u]);
  }

}
