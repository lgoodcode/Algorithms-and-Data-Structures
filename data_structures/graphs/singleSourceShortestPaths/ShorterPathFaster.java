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
 *
 * <p>
 * This is faster because of the fact that the BellmanFord repeats the process
 * on all vertices for {@code |V| - 1} times. Whereas, the SPF only repeats the
 * search for edges to relax, on vertices of edges that already have been
 * relaxed to see if it can be relaxed again, by placing them in a queue.
 * </p>
 */
public class ShorterPathFaster extends SSSP {
  // Prevent this class from being instantiated
  public ShorterPathFaster() { 
    super();
  }

  /**
   * Runs the SPF algorithm on the directed weighted graph with the given source
   * vertex. Throws if the supplied graph isn't weighted and directed.
   * 
   * <p>
   * This is modified to make a check before enqueing the vertex of the edge that
   * was relaxed that the queue isn't full and doesn't already exist in the queue.
   * It doesn't add the vertex if it is already in the queue because it has to be
   * checked if it can relax or not before adding into the queue to attempt to
   * relax it again.
   * </p>
   * 
   * <p>
   * Another modification in the algorithm that includes a counter {@code n} that
   * increments on each dequeue of a vertex. Once the counter reaches over the
   * number of vertices squared, it is deemed reasonable to determine that there
   * are negative weighted edges in the graph resulting in a negative weight
   * cycle.
   * </p>
   *
   * @param graph        the weighted directed graph to run the algorithm on
   * @param sourceVertex the single source vertex from which all paths originate
   *                     from
   * @return the {@code Node[]} results of the algorithm or {@code null} if there
   *         is a negative weight cycle
   *
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed or the source vertex
   *                                  is invalid
   */
  public static Node[] run(Graph graph, int sourceVertex) {
    checkGraph(graph);
    graph.checkVertex(sourceVertex);
    return _run(graph, sourceVertex);
  }

  private static Node[] _run(Graph G, int s) {
    int rows = G.getRows(), loop = rows * rows;
    Queue<Integer> Q = new Queue<>(rows);
    Node[] VTS = initSource(G, s);
    int n = 0, u, v, w;

    Q.enqueue(s);

    while(!Q.isEmpty()) {
      u = Q.dequeue();

      for (Graph.Edge edge : G.getEdges(u)) {
        v = edge.getVertices()[1];
        w = edge.getWeight();

        // Relax and queue vertex v to check if we can relax it again
        if (VTS[v].distance > VTS[u].distance + w) {
          VTS[v].distance = VTS[u].distance + w;
          VTS[v].predecessor = u;

          // Checks for infinite loop
          if (++n > loop)
            return null;
          // Enqueue v if it doesn't already exist in the queue
          if (!Q.has(v))
            Q.enqueue(v);
        }
      }
    }

    for (Graph.Edge edge : G.getEdges()) {
      u = edge.getVertices()[0];
      v = edge.getVertices()[1];
      w = edge.getWeight();

      // If either vertex is unreachable (distance = Infinity), continue
      // to check next vertex to prevent addition overflow
      if (VTS[v].distance == Graph.NIL || VTS[u].distance == Graph.NIL)
        continue;
      if (VTS[v].distance > VTS[u].distance + w)
        return null;
    }

    return VTS;
  }

  /**
   * Runs the algorithm and returns the path string for the start and end
   * vertices. Will return a message indicating a cycle in the graph if one
   * exists. Otherwise, will return the path string.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the string for a cycle detected, if there is one, or the path if one
   *         exists or a no path exists message string
   * 
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static String printPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(endVertex);
    Node[] results = run(graph, startVertex);
    
    if (results == null)
      return "Graph contains a negative weight cycle.";
    return Graph.printPath(results, startVertex, endVertex);
  }

  /**
   * Runs the algorithm and returns the array of path vertices for the start and
   * end vertices. Will return the no path array, which is an integer array
   * containing only {@code -1} if a cycle is detected. Otherwise, will return the
   * path array as normal or the same as the cycle array if no path exists.
   *
   * @param graph       the graph to run the algorithm on
   * @param startVertex the starting vertex of the path
   * @param endVertex   the end vertex of the path
   * @return the array path containing only {@code -1} if a cycle is detected or
   *         there is no path, or the array of vertices for the path, if one
   *         exists.
   * 
   * @throws IllegalArgumentException if the specified {@code Graph} is not
   *                                  weighted and directed, or the start or end
   *                                  vertices are invalid
   */
  public static int[] arrayPath(Graph graph, int startVertex, int endVertex) {
    graph.checkVertex(endVertex);
    Node[] results = run(graph, startVertex);
    int[] cycle = { -1 };

    if (results == null)
      return cycle;
    return Graph.arrayPath(results, startVertex, endVertex);
  }

}
