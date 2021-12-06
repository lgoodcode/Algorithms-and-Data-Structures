package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;

/**
 * <i><b>Bridge - </b></i>An edge whose removal disconnects {@code G}. Similar
 * to an <i>Articulation Point</i>, when removed, increases the number of
 * <i>Connected Components</i> and removes a path between the components that
 * each vertex of the edge was contained in.
 *
 * <p>
 * To check if an edge is a bridge or not, just like for articulation points,
 * the concept of a <i>Back Edge</i> can be used to check the existence of the
 * alternate path.
 * </p>
 *
 * <p>
 * For any edge {@code (u, v)}, where {@code u} has an earlier discovery time
 * than {@code v}, if the earliest discovered vertex that can be visited from
 * any vertex in the subtree rooted at {@code v} has a discovery time strictly
 * greater than that of {@code u}, then {@code (u, v)} is a bridge.
 *
 * <p>
 * Unlike articulation points, root is not a special case here, so it doesn't
 * need to count the children under a subtree.
 * </p>
 */
public final class Bridge {
  private static class Node {
    int disc;
    int low;
    int parent;
    boolean visited;

    Node() {}
  }

  // Prevent this algorithm from being instantiated
  public Bridge() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Finds the Bridges in the given graph.
   *
   * @param graph the graph to find the bridges of
   * @return an {@code Object} array of the edge vertices
   */
  public static Object[] compute(Graph graph) {
    Node[] V = new Node[graph.getRows()];
    LinkedList<Integer[]> B = new LinkedList<>();
    int[] time = {0};

    for (int u : graph.getVertices())
      V[u] = new Node();

    for (int u : graph.getVertices())
      if (!V[u].visited)
        B_DFS(graph, V, B, time, u);
    return B.toArray();
  }

  /**
   * Operates the same as the Articulation Points algorithm, with a couple
   * differences. Since the root isn't a special case, it doesn't count the number
   * of children in a subtree and only checks if the earliest discovered vertex in
   * the subtree of {@code v} is strictly greater than the discovery time of
   * {@code u}, then {@code (u, v)} is a bridge.
   *
   * @param G    the graph
   * @param V    the array of nodes holding the attributes
   * @param B    the linkedlist to add the bridge edges
   * @param time the current time for vertices visited
   * @param u    the current vertex
   */
  private static void B_DFS(Graph G, Node[] V, LinkedList<Integer[]> B, int[] time, int u) {
    V[u].visited = true;
    V[u].disc = V[u].low = ++time[0];

    for (Graph.Edge edge : G.getEdges(u)) {
      int v = edge.getVertices()[1];

      if (!V[v].visited) {
        V[v].parent = u;

        B_DFS(G, V, B, time, v);
        // When DFS returns, v.low will have the discovery time of the earliest
        // discovered vertex that can be reached from any vertex in the subtree
        // rooted at v. So, set it u.low to the minimum of itself and v.low
        V[u].low = Math.min(V[u].low, V[v].low);
        /**
         * Only difference between APs and Bridges: if the earliest discovered vertex
         * that can be visited from any vertex in the subtree rooted at vertex v has a
         * discovery time strictly greater than the discovery time of vertex u - then
         * edge (u, v) is a bridge.
         */
        if (V[v].low > V[u].disc)
          B.insert(edge.toArray());
      }
      // If vertex v is connected to u but already visited, update u.low to the
      // minimum of u.low and v.disc.
      else if (V[u].parent != v) {
        V[u].low = Math.min(V[u].low, V[v].disc);
      }
    }
  }
}
