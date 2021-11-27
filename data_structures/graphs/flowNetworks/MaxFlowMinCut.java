package data_structures.graphs.flowNetworks;

import java.util.Arrays;

public class MaxFlowMinCut extends MaxFlowAlgorithm {
  public MaxFlowMinCut() {
    super();
  }

  /**
   * Prints all edges that are from a reachable vertex to non-reachable vertex in
   * the original graph. Uses the Edmonds-Karp algorithm to get the maximum flow
   * which has a time complexity of {@code O(V E^2)}. This method is preferred
   * over the Push-Relabel algorithm if it has less vertices.
   * 
   * @param network the flow network to find the minimum cut
   * @param source
   * @param sink
   * @return
   */
  public static String[] minCutsEdmondsKarp(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return new String[0];

    int[][] Gf = EdmondKarp.residualGraph(network, source, sink);
    return getMinCuts(network, Gf, source, sink);
  }

  /**
   * Prints all edges that are from a reachable vertex to non-reachable vertex in
   * the original graph. Uses Goldberg's generic Push-Relabel algorithm to get the
   * maximum flow which has a time complexity of {@code O(V^3)}. This method is
   * preferred over the Edmond-Karp algorithm if it has more vertices.
   * 
   * @param network the flow network to find the minimum cut
   * @param source
   * @param sink
   * @return
   */
  public static String[] minCutsPushRelabel(FlowNetwork network, int source, int sink) {
    network.checkVertex(source);
    network.checkVertex(sink);

    if (source == sink)
      return new String[0];

    int[][] Gf = Goldberg.residualGraph(network, source, sink);
    return getMinCuts(network, Gf, source, sink);
  }

  /**
   * Compares the residual network, the network after running the max flow
   * algorithn with the original network. For each edge {@code (u, v)} of the
   * residual network graph and the original graph, their residual capacity is
   * compared. If {@code cf(u, v) == 0} for the edge in the residual network and
   * the edge in the original graph {@code cf(u, v) > 0}, that edge is part of the
   * min-cut.
   * 
   * @param network the flow network to find the min-cut of
   * @param Gf      the residual network derived from the max flow algorithm
   * @param s       the source vertex
   * @param t       the sink vertex
   * @return the array of edges in the min-cut
   */
  private static String[] getMinCuts(FlowNetwork network, int[][] Gf, int s, int t) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    String[] cuts = new String[Gf.length];
    int i = 0;

    for (int u : network.getVertices()) {
      for (int v : network.getAdjacentVertices(u)) {
        if (Gf[u][v] == 0 && G[u][v].getCapacity() > G[u][v].getFlow())
          cuts[i++] = "(" + u + ", " + v + ")";
      }
    }

    return Arrays.copyOf(cuts, i);
  }

}
