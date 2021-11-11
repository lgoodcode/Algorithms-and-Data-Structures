package data_structures.graphs.singleSourceShortestPaths;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;
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
    Node[] VTS = initSource(G, s);
    LinkedList<TopologicalSort.Node> L = TopologicalSort.run(G, s);
    Iterable<TopologicalSort.Node> N = L.values();
    Graph.Edge[] edges;
    int i, u, v, w;

    for (TopologicalSort.Node node : N) {
      u = node.getVertex();
      edges = G.getEdges(u);

      for (i = 0; i < edges.length; i++) {
        v = edges[i].getVertices()[1];
        w = edges[i].getWeight();

        relax(VTS, u, v, w);
      }
    }

    return VTS;
  }

}
