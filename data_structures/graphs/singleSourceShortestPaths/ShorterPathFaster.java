package data_structures.graphs.singleSourceShortestPaths;

import data_structures.graphs.Graph;
import data_structures.queues.Queue;

/**
 * Shorter Path Faster Algorithm {@code O(|V||E|)} - Expected running time is
 * {@code O(|E|)}
 *
 * <p>
 * A faster implementation of the Bellman-Ford algorithm where every vertex is
 * used to relax its adjacent vertices but in SPF algorithm, a queue of vertices
 * is maintained and a vertex is added to the queue only if that vertex is
 * relaxed. This process repeats until no more vertex can be relaxed.
 * </p>
 */
public class ShorterPathFaster extends SSSP {
  /**
   * Runs the SPF algorithm on the directed weighted graph with the given source
   * vertex. Throws if the supplied graph isn't weighted and directed.
   * 
   * @param graph        the weighted directed graph to run the algorithm on
   * @param sourceVertex the single source vertex from which all paths originate
   *                     from
   * @return the {@code Node[]} results of the algorithm or {@code null} if there
   *         is a cycle
   * 
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed
   */
  public static Node[] run(Graph graph, int sourceVertex) {
    return _run(graph, sourceVertex);
  }

  public static Node[] _run(Graph G, int s) {
    int[] V = G.getVertices();
    Queue<Integer> Q = new Queue<>(V.length * 2);
    Node[] VTS = initSource(G, s);
    Graph.Edge[] edges;
    int i, j, u, v, w;

    Q.enqueue(s);

    while(!Q.isEmpty()) {
      u = Q.dequeue();
      edges = G.getEdges(u);

      for (i = 0; i < edges.length; i++) {
        v = edges[i].getVertices()[1];
        w = edges[i].getWeight();

        // Relax and queue vertex v to check if we can relax it again
        if (VTS[v].distance > VTS[u].distance + w) {
          VTS[v].distance = VTS[u].distance + w;
          VTS[v].predecessor = u;

          if (!Q.has(v))
            Q.enqueue(v);
        }
      }    
    }

    for (i = 0; i < V.length; i++) {
      u = V[i];
      edges = G.getEdges(u);

      for (j = 0; j < edges.length; j++) {
        v = edges[j].getVertices()[1];
        w = edges[j].getWeight();

        if (VTS[v].distance > VTS[u].distance + w)
          return null;
      }
    }
    
    return VTS;
  }
}
