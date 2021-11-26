package data_structures.graphs.singleSourceShortestPaths;

import data_structures.graphs.Graph;
import data_structures.graphs.TopologicalSort;

/**
 * DAG-Shortest-Paths(G, w, s)
 * 1   topologically sort the vertices of G
 * 2   Initialize-Single-Source(G, s)
 * 3   for each vertex u, taken in topologically sorted order
 * 4       for each vertex v of G.Adj[u]
 * 5           Relax(u, v, w)
 */

/**
 * By relaxing the edges of a weighted <i><b>DAG - Directed Acyclic
 * Graph</b></i> according to a topological sort of its vertices, we can compute
 * shortest paths from a single source in {@code (-)(V + E)} time. Shortst paths
 * are always well defined in a DAG, since even if there are negative-weight
 * edges, no negative-weight cycles can exist.
 *
 * The algorithm starts by topologically sorting the DAG to impose a linear
 * ordering on the vertices. If the DAG contains a path from vertex {@code u} to
 * vertex {@code v}, then {@code u} precedes {@code v} in the topological sort.
 * We make just one pass over the vertices in the topologically sorted order. As
 * we process each vertex, we relax each edge that leaves the vertex.
 *
 * <hr/>
 * <h3>Aggregate Analysis {@code (-)(V + E)}</h3>
 *
 * The running time of the DAG-Shortest-Paths algorithm is {@code (-)(V + E)}.
 * The call to Initialize_Single_Source takes {@code (-)(V)} time. The for loop
 * of lines 3-5 makes on iteration per vertex and the nested for loop on line 4
 * relaxes each edge exactly once.
 */
public final class DAGShortestPath extends SSSP {
  // Prevent this class from being instantiated
  public DAGShortestPath() { 
    super();
  }

  /**
   * Topologically sorts all the vertices of the DAG (Directed Acylic Graph) and
   * then relaxes them in sorted order to derive the shortest paths for the
   * specified start vertex.
   *
   * @param graph       the graph matrix
   * @param startVertex the starting vertex
   * @return the {@code Node[]} results of the algorithm
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start vertex
   *                                  is invalid
   */
  public static Node[] run(Graph graph, int startVertex) {
    checkGraph(graph);
    graph.checkVertex(startVertex);
    return _run(graph, startVertex);
  }

  private static Node[] _run(Graph G, int s) {
    Object[] V = TopologicalSort.run(G, s);
    Node[] VTS = initSource(G, s);
    int v, w;

    for (Object u : V) {
      for (Graph.Edge edge : G.getEdges((int) u)) {
        v = edge.getVertices()[1];
        w = edge.getWeight();

        relax(VTS, (int) u, v, w);
      }
    }

    return VTS;
  }

  /**
   * Runs the algorithm and returns the path string for the start and end
   * vertices.
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
    graph.checkVertex(endVertex);
    Node[] results = run(graph, startVertex);
    return Graph.printPath(results, startVertex, endVertex);
  }

  /**
   * Runs the algorithm and returns the array of path vertices for the start and
   * end vertices.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array of vertices for the path
   * 
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(endVertex);
    Node[] results = run(graph, startVertex);
    return Graph.arrayPath(results, startVertex, endVertex);
  }

}
