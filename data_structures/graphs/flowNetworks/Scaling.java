package data_structures.graphs.flowNetworks;

import data_structures.linkedLists.LinkedList;
import data_structures.queues.Queue;

/**
 * Max Flow by Scaling(G, s, t)
 * 1   C = max( (u, v) of E c(u, v))   // Max capacity of all edges
 * 2   initialize flow f to 0
 * 3   K = 2^floor(lg C)
 * 4   while K >= 1
 * 5      while there is an augmenting path p of capacity at least K
 * 6          augment flow f along p
 * 7      K = K / 2
 * 8   return f
 */

/**
 * <h3>Max Flow by Scaling Algorithm {@code O(E^2 log2 C)}</h3>
 *
 * <p>
 * The algorithm is a modification of the Max Flow Scaling Method to compute
 * maximum flow in {@code G}.
 * </p>
 *
 * <p>
 * <b>C</b> is the maximum capacity in the flow network.
 * </p>
 *
 * <p>
 * The BFS algorithm is modified from the normal Edmonds-Karp BFS to find edges
 * with positive residual capacity but also keeps track of the sum of all edges
 * visited for the path to determine, if there is an augmenting path where the
 * sum of those capacities is at least {@code K}.
 * </p>
 */
public final class Scaling extends MaxFlowAlgorithm {
  // Prevent this class from being instantiated
  public Scaling() {
    super();
  }

  /**
   * Runs the Max Flow Scaling algorithm to find the maximum flow in the specified
   * flow network from the specified source to the sink.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the maximum flow from the source to the sink or {@code 0} if the
   *         source is the sink
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static int maxFlow(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return 0;
    return computeMaxFlow(new FlowNetwork(network), source, sink);
  }

  /**
   * Runs the Max Flow Scaling algorithm to find the paths for the maximum flow in
   * the specified flow network from the specified source to the sink. Returns an
   * array of strings for each path and their respective.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the strings of paths for the maximum flow from source to sink or an
   *         array of {@code 0} length if no paths exist for a maximum flow
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static String[] maxFlowPaths(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return new String[0];
    return computeMaxFlowPaths(new FlowNetwork(network), source, sink);
  }

  /**
   * Runs the Max Flow Scaling algorithm to find the paths for the maximum flow in
   * the specified flow network from the specified source to the sink. Returns an
   * array of the paths and their flow as the first index followed by the vertices
   * of the path.
   *
   * @param network the flow network
   * @param source  the starting vertex
   * @param sink    the destination vertex
   * @return the arrays of paths for the maximum flow from source to sink or an
   *         array of {@code 0} length if no paths exist for a maximum flow
   *
   * @throws IllegalArgumentException if the source or sink vertices are invalid
   */
  public static Integer[][] maxFlowArray(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return new Integer[0][];
    return computeMaxFlowArray(new FlowNetwork(network), source, sink);
  }

  private static int computeMaxFlow(FlowNetwork network, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    Node[] VTS = new Node[network.getRows()];
    int C = 0, K, maxFlow = 0;

     // Initialize nodes
     for (int i = 0; i < G.length; i++)
     VTS[i] = new Node(i);

    // Find the C, the maximum capacity of the flow network
    for (FlowNetwork.Edge edge : network.getEdges())
      C = Math.max(C, edge.getCapacity());

    // Compute K = 2^floor(lg C) = 2^floor(log10(C) * log2(2))
    K = 1 << (int) Math.floor(Math.log(C) * 1.443);

    while (K >= 1) {
      while (scaling_BFS(network, VTS, K, s, t))
        maxFlow += residualCapacity(G, VTS, Integer.MAX_VALUE, t);
      K /= 2;
    }

    return maxFlow;
  }

 /**
  * Modfied BFS to check if there is a path from source {@code s} to sink
  * {@code t} that has a positive residual capacity to be able to augment a path
  * and the total sum of the residual capacity of all the edges along the path is
  * equal to or greater than {@code K}.
  *
  * @param G   the flow network
  * @param VTS the nodes
  * @param K   the value the sum of the edges flow must be at least
  * @param s   the source
  * @param t   the sink
  * @returns whether a path exists from the source to the sink that can be
  *          augmented
  */
  private static boolean scaling_BFS(FlowNetwork G, Node[] VTS, int K, int s, int t) {
    Queue<Integer> Q = new Queue<>(G.getRows());
    int c, f, u, v, C = 0;

    // Reset nodes to unvisited
    for (Node node : VTS) {
      node.visited = false;
      node.predecessor = NIL;
    }

    VTS[s].visited = true;
    Q.enqueue(s);

    while (!Q.isEmpty()) {
      u = Q.dequeue();

      for (FlowNetwork.Edge edge : G.getEdges(u)) {
        c = edge.getCapacity();
        f = edge.getFlow();
        v = edge.getVertices()[1];

        if (f < c && !VTS[v].visited()) {
          C += c - f;
          VTS[v].visited = true;
          VTS[v].predecessor = u;
          Q.enqueue(v);
        }
      }
    }

    // Find if there was a path from the source to the sink (reached sink from source)
    // AND the sum of all edges flow is at least K
    return VTS[t].visited() && C >= K;
  }

  private static String[] computeMaxFlowPaths(FlowNetwork network, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    Node[] VTS = new Node[network.getRows()];
    FlowPaths P = new FlowPaths(true);
    StringBuilder sb = new StringBuilder();
    int C = 0, K, flow = 0;

    // Initialize nodes
    for (int i = 0; i < G.length; i++)
      VTS[i] = new Node(i);

    // Find the C, the maximum capacity of the flow network
    for (FlowNetwork.Edge edge : network.getEdges())
      C = Math.max(C, edge.getCapacity());

    // Compute K = 2^floor(lg C) = 2^floor(log10(C) * log2(2))
    K = 1 << (int) Math.floor(Math.log(C) * 1.443);

    while (K >= 1) {
      while (scaling_BFS(network, VTS, K, s, t)) {
        flow = printResidualCapacity(G, VTS, sb, Integer.MAX_VALUE, t);
        // Add the string path with the flow
        P.addPath(flow + ": " + sb.toString() + t);
        // Reset the stringbuilder for the next path
        sb = new StringBuilder();
      }

      K /= 2;
    }

    return P.getStringPaths();
  }

  private static Integer[][] computeMaxFlowArray(FlowNetwork network, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    Node[] VTS = new Node[network.getRows()];
    FlowPaths P = new FlowPaths(false);
    LinkedList<Integer> L = new LinkedList<>();
    int C = 0, K, flow = 0;

    // Initialize nodes
    for (int i = 0; i < G.length; i++)
      VTS[i] = new Node(i);

    // Find the C, the maximum capacity of the flow network
    for (FlowNetwork.Edge edge : network.getEdges())
      C = Math.max(C, edge.getCapacity());

    // Compute K = 2^floor(lg C) = 2^floor(log10(C) * log2(2))
    K = 1 << (int) Math.floor(Math.log(C) * 1.443);

    while (K >= 1) {
      while (scaling_BFS(network, VTS, K, s, t)) {
        flow = arrayResidualCapacity(G, VTS, L, Integer.MAX_VALUE, t);
        // Add the sink to the end of the path of vertices
        L.insertLast(t);
        // Add the flow for this path
        P.addPath(L, flow);
        // Reset the linkedlist for the next path
        L = new LinkedList<>();
      }

      K /= 2;
    }

    return P.getArrayPaths();
  }

}
