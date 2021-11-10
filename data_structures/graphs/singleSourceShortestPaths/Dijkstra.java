package data_structures.graphs.singleSourceShortestPaths;

import data_structures.graphs.Graph;

import data_structures.heaps.FibonacciHeap;

/**
 * Dijkstra(G, w, s)
 * 1   Initialize-Single-Source(G, s)
 * 2   S = 0 (empty set)
 * 3   Q = G.V
 * 4   while Q != 0
 * 5       u = Extract-Min(Q)
 * 6       S = S U {u}
 * 7       for each vertex v of G.Adj[u]
 * 8           Relax(u, v, w)
 */

/**
 * Dijkstra's algorithm solves the single-source shortest-paths problem on a
 * weighted (DAG) directed Acylic Graph, for the case in which all edge weights
 * are nonnegative.
 *
 * <p>
 * For this, we assume that {@code w(u, v) >= 0} for each edge
 * {@code (u, v) of E}. A good implementation of Dijkstra's algorithm has a
 * lower running time than the Bellman-Ford algorithm.
 * </p>
 *
 * <p>
 * Dijkstra's algorithm maintains a set {@code S} of vertices whose final
 * shortest-path weights from the source {@code s} have already been determined.
 * The algorithm repeatedly selects the vertex {@code u of V - S} with the
 * minimum shortest-path estimate, adds {@code u} to {@code S}, and relaxes all
 * edges leaving {@code u}. We use a min-priority queue {@code Q} of vertices,
 * keyed by their {@code d} (distance) values, similar to Kruskal's
 * implementation.
 * </p>
 *
 * <p>
 * This implementation uses the {@link FibonacciHeap} for the MinPriorityQueue
 * over a Binary MinHeap for improved performance. It also omits the set
 * {@code S}, which is unecessary in this case because the {@code Node[]} holds
 * all the information to produce the path, which also has all the vertices in
 * the graph.
 * </p>
 *
 * <p>
 * It also iterates for {@code |V| - 1} times, removing all but one vertex and
 * will still give the same solution.
 * </p>
 *
 * <hr/>
 * <h3>Aggregate Analysis</h3>
 * <h4>FibHeap - {@code  O(V lg V + E)} BinMinHeap - {@code O(V + E lg V)}</h4>
 *
 * <p>
 * The running time of the algorithm with the FibonacciHeap operations is
 * improved over the BinaryMinHeap implementation. The insert and extractMin
 * operations are called at most once each which is {@code |V|} and the for loop
 * runs once for every edge which is {@code |E|}. with the Fibonacci heap, the
 * Extract-Min operations are {@code O(lg V)}.
 * </p>
 *
 * <p>
 * The running time of the algorithm depends on the implementation of the
 * min-priority queue and the operations called: insert, extract min, and the
 * sorting. The insert, search, and extract min operations are called at most
 * once each which is {@code |V|} and the for loop runs once for every edge
 * which is {@code |E|}. It runs in {@code O((V + E) lg V)} which is
 * {@code O(E lg V)} if all vertices are reachable from the source.
 * </p>
 */
public final class Dijkstra extends SSSP {
  /**
   * Runs the Dijkstra SSSP algorithm on the specified graph for the source vertex
   * of all the paths. Details on the specifics of this implementation is noted in
   * the documentation of the class.
   *
   * @param graph        the weighted directed graph to run the algorithm on
   * @param sourceVertex the single source vertex from which all paths originate
   *                     from
   * @return the {@code Node[]} results of the algorithm
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the source vertex
   *                                  is invalid
   */
  public static Node[] run(Graph graph, int sourceVertex) {
    checkGraph(graph);
    graph.checkVertex(sourceVertex);
    return _run(graph, sourceVertex);
  }

  private static Node[] _run(Graph G, int s) {
    Node[] VTS = initSource(G, s);
    FibonacciHeap<Node> Q = new FibonacciHeap<>((x, y) -> x.distance < y.distance);
    Graph.Edge[] edges;
    int i, u, v, w;

    // Initialize the PriorityQueue with all the vertex nodes
    for (i = 0; i < VTS.length; i++)
      Q.insert(VTS[i]);

    while (Q.size() > 1) {
      u = Q.extractMin().getVertex();
      edges = G.getEdges(u);

      for (i = 0; i < edges.length; i++) {
        v = edges[i].getVertices()[1];
        w = edges[i].getWeight();

        relax(VTS, u, v, w);
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
    Node[] results = _run(graph, startVertex);
    return Graph.printPath(results, startVertex, endVertex);
  }

  /**
   * Runs the BFS algorithm and returns the array of path vertices for the start
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

}
