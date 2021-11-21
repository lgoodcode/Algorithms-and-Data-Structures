package data_structures.graphs.allPairsShortesPaths;

import static java.util.Arrays.fill;

import data_structures.graphs.Graph;
import data_structures.graphs.singleSourceShortestPaths.Dijkstra;
import data_structures.graphs.singleSourceShortestPaths.SSSP;
import data_structures.graphs.singleSourceShortestPaths.ShorterPathFaster;

/**
 * Johnson(G, w)
 * 1   compute G' where G'.V = G.V U {s},
 *         G'.E = G.E U {(s, v) : v is a member of G.V}, and
 *         w(s, v) = 0 for all v of G.V
 * 2   if Bellman-Ford(G', w, s) == False
 * 3       print "the input graph contains a negative-weight cycle"
 * 4   else for each vertex v of G'.V
 * 5           set h(v) to the value of S(s, v) computed by the Bellman-Ford algorithm
 * 6       for each edge (u, v) of G'.E
 * 7           w'(u, v) = w(u, v) + h(u) - h(v)
 * 8       let D = d uv be a new n x n matrix
 * 9       for each vertex u of G.V
 * 10          run Dijkstra(G, w', u) to compute S'(u, v) for all v of G.V
 * 11          for each vertex v of G.V
 * 12              d uv = S'(u, v) + h(v) - h(u)
 * 13      return D
 */

/**
 * Johnson's algorithm finds shortest paths between all pairs in
 * {@code O(V^2 lg V + VE)} time. For sparse graphs, it is asymptotically faster
 * than either repeated squaring of matrices of the Floyd-Warshall algorithm.
 * The algorithm either returns a matrix of shortest-path weight for all pairs
 * of vertices or reports that the input graph contains a negative-weight cycle.
 *
 * <p>
 * Johnson's algorithm uses as subroutines both Dijkstra's algorithm and the
 * Bellman-Ford algorithm (or the improved Shorter-Path Faster algorithm).
 * </p>
 *
 * <p>
 * It uses the technique of "Reweighting", which works as follows:
 * </p>
 *
 * <p>
 * If all edge weights {@code w} in graph {@code G} are nonnegative, we can find
 * shortest paths between all pairs of vertices by running Dijkstra's algorithm
 * once from each vertex (Dijkstra's running time depends on the implementation
 * of the min-priority queue).
 * </p>
 *
 * <p>
 * If {@code G} has negative-weight edges but no negative-weight cycles, we
 * simply compute a new set of nonnegative edge weights that allows us to use
 * the same method. The set of edges must satisfy two important properties:
 * </p>
 *
 * <ol>
 * <li>For all pairs of vertices {@code u}, {@code v} of {@code V}, a path
 * {@code p} is a shortest path from {@code u} to {@code v} using weight
 * function {@code w} if and only if {@code p} is also a shortest path from
 * {@code u} to {@code v} using weight function {@code w'}.</li>
 * <li>For all edges {@code (u, v)}, the new weight {@code w'(u, v)} is
 * nonnegative.</li>
 * </ol>
 *
 * <p>
 * The algorithm uses adjacency-list representation and returns the usual
 * {@code |V| x |V| matrix D = d ij}, where {@code d ij = S(i, j)}, or reports
 * the input graph contains a negative-weight cycle.
 * </p>
 *
 * <h3>Preserving shortest paths by reweighting:</h3>
 *
 * <p>
 * Given a weighted, directed graph {@code G} with weight function
 * {@code w : E -> R}. Let {@code h : V -> R} be any function mapping vertices
 * to real numbers. For each edge {@code (u, v) of E}, define:
 * </p>
 *
 * <i>w'(u, v) = w(u, v) + h(u) - h(v)</i>
 *
 * <p>
 * Let {@code p = [v0, v1, ..., vk]} be any path from vertex {@code v0} to
 * vertex {@code vk. p} is a shortest path from {@code v0} to {@code vk} with
 * weight function w if and only if it is a shortest path with weight function
 * w'. That is, weight of path p - w(p) = S(vo, vk) if and only if w'(p) =
 * S'(v0, vk). G has a negative-weight cycle using weight function w if and only
 * if G has a negative-weight cycle using weight function w'.
 * </p>
 *
 * <p>
 * Therefore, any path {@code p} from {@code v0} to {@code vk} has
 * {@code w'(p) = w(p) + h(v0) - h(vk)}. Because {@code h(v0)} and {@code h(vk)}
 * do not depend on the path, if one path from {@code v0} to {@code vk} is
 * shorter than another using weight function {@code w}, then it is also shorter
 * using {@code w'}. Thus,
 * <p>
 *
 * <i>w(p) = S(v0, vk) if and only if w'(p) = S'(v0, vk)</i>
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code O(VE lg V)}</h3>
 *
 */
public final class Johnsons extends ASPS {
  public static class Node extends Graph.Vertex {
    private Node(int vertex) {
      super(vertex);
    }
  }

  // Prevent this class from being instantiated
  public Johnsons() { 
    super(); 
  }

  /**
   * Runs the Johnson's algorithm on the directed, weighted graph to find the
   * shortest paths for all vertices. If a negative weight cycle exists in the
   * graph, it will return {@code null}. Will return the matrix table of the total
   * weight of distances between two vertices {@code i} and {@code j} for
   * {@code D[i][j]}.
   *
   * <p>
   * Using the ShorterPathFaster algorithm which has an expected running time of
   * {@code O(|E|)} against Bellman-Ford's {@code O(VE)} for detecting negative
   * weight cycles.
   * </p>
   *
   * @param graph the graph matrix to run the algorithm on
   * @return the table of weights for vertex to vertex paths or {@code null} if it
   *         contains a negative weight cycle
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static int[][] run(Graph graph) {
    checkGraph(graph);
    return _run(graph);
  }

  private static int[][] _run(Graph G) {
    int n = G.getRows();
    // compute G' where G'.V = G.V U {s}, where s is a new vertex
    Graph T = new Graph(G, n + 1);
    int[] vertices, h = new int[n], V = G.getVertices();
    int[][] D;
    SSSP.Node[] VTS;
    // Set s, the new vertex, to the number of rows in the graph
    int s = n, i, j, u, v;

    // Add the new edge from the new vertex to all vertices with 0 weight
    for (i = 0; i < V.length; i++)
      T.addEdge(s, V[i], 0);

    // Check if the graph contains a negative weight cycle
    if ((VTS = ShorterPathFaster.run(T, s)) == null)
      return null;

    // Set h(v) to the value of S(s, v) computed by the Bellman-Ford algorithm
    for (i = 0; i < n; i++)
      h[i] = VTS[i].distance;

    for (Graph.Edge edge : G.getEdges()) {
      vertices = edge.getVertices();
      u = vertices[0];
      v = vertices[1];

      // w'(u, v) = w(u, v) + h(u) - h(v)
      G.setEdge(u, v, edge.getWeight() + h[u] - h[v]);
    }

    D = new int[n][n];

    for (i = 0; i < n; i++)
      fill(D[i], Graph.NIL);

    // Run Dijkstra(G, w', u) to compute S'(u, v) for all u and v of G.V
    for (i = 0; i < V.length; i++) {
      u = V[i];
      VTS = Dijkstra.run(G, u);

      for (j = 0; j < V.length; j++) {
        v = V[j];
        D[u][v] = VTS[v].distance + h[v] - h[u];
      }
    }

    return D;
  }

  /**
   * Runs the Johnson's algorithm on the directed, weighted graph to find the
   * shortest paths for all vertices. If a negative weight cycle exists in the
   * graph, it will return {@code null}. Will return the predecessor matrix that
   * can be passed to the {@link Graph#printPath()} or {@link Graph#arrayPath()}
   * methods to compute the paths.
   *
   * @param graph the graph matrix to run the algorithm on
   * @return the predecessor matrix of the graph or {@code null} if it contains a
   *         negative weight cycle
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static SSSP.Node[][] table(Graph graph) {
    checkGraph(graph);
    return _table(graph);
  }

  private static SSSP.Node[][] _table(Graph G) {
    int n = G.getRows();
    Graph T = new Graph(G, n + 1);
    int[] vertices, h = new int[n], V = G.getVertices();
    int[][] D;
    SSSP.Node[] VTS;
    SSSP.Node[][] P = new SSSP.Node[n][];
    int s = n, i, j, u, v;

    for (i = 0; i < V.length; i++)
      T.addEdge(s, V[i], 0);

    if ((VTS = ShorterPathFaster.run(T, s)) == null)
      return null;

    for (i = 0; i < n; i++)
      h[i] = VTS[i].distance;

    for (Graph.Edge edge : G.getEdges()) {
      vertices = edge.getVertices();
      u = vertices[0];
      v = vertices[1];

      G.setEdge(u, v, edge.getWeight() + h[u] - h[v]);
    }

    D = new int[n][n];

    for (i = 0; i < n; i++)
      fill(D[i], Graph.NIL);

    for (i = 0; i < V.length; i++) {
      u = V[i];
      VTS = Dijkstra.run(G, u);
      // Sets this vertex index to the results of Dijkstra's algorithm to
      // compute path with this vertex as the source.
      P[u] = VTS;

      for (j = 0; j < V.length; j++) {
        v = V[j];
        D[u][v] = VTS[v].distance + h[v] - h[u];
      }
    }

    return P;
  }

  /**
   * Detects if the specified graph contains a negative weight cycle by using the
   * first parth of the Johnson's algorithm, which creates a new Graph {@code G'}
   * with a new vertex and adds a new edge from the new vertex to all existing
   * vertices with a weight of {@code 0}. Then, runs the faster version of the
   * BellmanFord algorith, the Shorter-Path Faster algorithm to determine if a
   * negative weight cycle exists or not.
   *
   * @param graph the directed weighted graph to check
   * @return whether the graph contains a negative weight cycle or not
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static boolean hasNegativeWeightCycle(Graph graph) {
    checkGraph(graph);
    return _hasNegativeWeightCycle(graph);
  }

  private static boolean _hasNegativeWeightCycle(Graph G) {
    int n = G.getRows();
    Graph T = new Graph(G, n + 1);
    int[] V = G.getVertices();
    int s = n, i;

    for (i = 0; i < V.length; i++)
      T.addEdge(s, V[i], 0);
    return ShorterPathFaster.run(T, s) == null;
  }

  /**
   * Runs the algorithm and returns the path string for the start and end
   * vertices. Uses the array of predecessor matrices and specifies the start
   * vertex to determine which matrix to use to build the path. If there is no
   * predecessor matrix for the specified start vertex, then the vertex doesn't
   * exist in the graph, therefore, cannot have a path.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);

    SSSP.Node[][] P = table(graph);

    if (P == null)
      return "Graph contains a negative weight cycle.";
    if (P[startVertex] == null)
      return "No path exists from " + startVertex + " to " + endVertex;
    return Graph.printPath(P[startVertex], startVertex, endVertex);
  }

  /**
   * Returns the path string for the start and end vertices using the results of
   * the Johnson's algorithm. If there is no predecessor matrix for the specified
   * start vertex, then the vertex doesn't exist in the graph, therefore, cannot
   * have a path.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   *
   * @throws NullPointerException if the specified nodes is {@code null}
   */
  public static final String printPath(Graph.Vertex[][] nodes, int startVertex, int endVertex) {
    if (nodes == null)
      throw new NullPointerException("Nodes cannot be null.");
    if (nodes[startVertex] == null)
      return "No path exists from " + startVertex + " to " + endVertex;
    return Graph.printPath(nodes[startVertex], startVertex, endVertex);
  }

  /**
   * Runs the algorithm and returns the array of path vertices for the start and
   * end vertices. Uses the array of predecessor matrices and specifies the start
   * vertex to determine which matrix to use to build the path. If there is no
   * predecessor matrix for the specified start vertex, then the vertex doesn't
   * exist in the graph, therefore, cannot have a path.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(startVertex);
    graph.checkVertex(endVertex);

    SSSP.Node[][] P = table(graph);
    int[] noPath = { -1 };

    if (P == null || P[startVertex] == null)
      return noPath;
    return Graph.arrayPath(P[startVertex], startVertex, endVertex);
  }

  /**
   * Returns the array of path vertices for the start and end vertices using the
   * results of the Johnson's algorithm. If there is no predecessor matrix for the
   * specified start vertex, then the vertex doesn't exist in the graph,
   * therefore, cannot have a path.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string path if one exists or a no path exists message string
   *
   * @throws NullPointerException if the specified nodes is {@code null}
   */
  public static final int[] arrayPath(Graph.Vertex[][] nodes, int startVertex, int endVertex) {
    if (nodes == null)
      throw new NullPointerException("Nodes cannot be null.");
    if (nodes[startVertex] == null) {
      int[] noPath = { -1 };
      return noPath;
    }
    return Graph.arrayPath(nodes[startVertex], startVertex, endVertex);
  }

}
