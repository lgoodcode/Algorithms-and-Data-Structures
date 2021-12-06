package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;

/**
 * <h3>Articulation Points {@code O(V + E)}</h3>
 *
 * <p>
 * <i><b>Articulation Points</b></i>: A vertex whose removal diconnects
 * {@code G}. More so, if removed, and all the edges associated with it results
 * in the increase of the number of <i>Connected Components</i>. They represent
 * vulnerabilities in a network because if that single point is removed then the
 * graph is no longer connected and there is no reachable path between all
 * points.
 * </p>
 *
 * <p>
 * <i><b>Back Edge</b></i>: An edge that connects a vertex to a vertex that is
 * discovered before it's parent; CLRS BOOKS - Edges {@code (u, v)} connecting a
 * vertex {@code u} to an ancestor {@code v} in a DFS tree. Self-loops, which
 * may occur in in directed graphs, are considered to be back edges.
 * </p>
 *
 * <p>
 * The algorithm uses DFS to first initialize all vertices with the following
 * attributes:
 * <ul>
 * <li><b>visited</b>: determines if the vertex has been visited or not</li>
 * <li><b>disc</b>: the discovery time</li>
 * <li><b>low</b>: the time of the earliest discovered vertex to which {@code v}
 * or any vertices in the subtree rooted at {@code v} is having a back
 * edge.</li>
 * <li><b>parent</b>: the parent vertex
 * <li><b>AP</b>: boolean value if the vertex is an articulation point or
 * not</li> </ul
 * </p>
 *
 * <p>
 * It starts at the first vertex in {@code V} and works like DFS normally does,
 * tracking the discovery times but does so by adding the time with the
 * <i>low</i> attribute, since it is just discovered, the earliest vertex it is
 * connected to is itself.
 * </p>
 *
 * <p>
 * After the DFS call for going into an adjacent vertex {@code v}, it sets
 * {@code low} to be the time of the earliest discovered vertex that can be
 * reached from any vertex in subtree rooted at {@code v}.
 * </p>
 *
 * <p>
 * Vertex {@code u} is an <i>Articulation Point</i> if either it is a root and
 * has more than one child, or if it has a parent and the adjacent vertex
 * {@code v} that was visited in the DFS call has a {@code low} (the time of the
 * earliest discovered vertex in the subtree of {@code v}) that is equal to or
 * greater than the discovery time of the vertex {@code u}. If the latter case,
 * that means {@code v} has a child {@code s} such that there is no back edge
 * from {@code s} or any descendant of {@code s} to a proper ancestor of
 * {@code v}.
 * </p>
 *
 * <p>
 * If the adjacent vertex {@code v} is already visited but isn't the parent of
 * {@code u} and is connected to {@code u}, then update the {@code low} (the
 * time of the earliest discovered vertex in its subtree) of {@code u} to be
 * minimum of its current {@code low} and the discovery time of {@code v}:
 * </p>
 *
 * <p>
 * <i>v.low = min{ v.d, w.d: (u, w) is a back edge for some descendant u of v
 * }</i>
 * </p>
 */
public final class ArticulationPoints {
  /**
   * The node used to hold the attributes of the vertices for the algorithms.
   */
  private static class Node {
    /**
     * Whether the vertex has been visited through the DFS call yet.
     */
    boolean visited;

    /**
     * The time the vertex was discovered (how many vertices it took to reach this
     * one).
     */
    int disc;

    /**
     * The time of the earliest discovered vertex to where any vertices under the
     * subtree of this vertex has a back edge.
     */
    int low;

    /**
     * The parent vertex to reach this vertex.
     */
    int parent;

    /**
     * Whether this vertex has been marked as an Articulation Point or not to
     * prevent duplicates being added in the queue to return an array of distinct
     * vertices.
     */
    boolean AP;

    Node() {
      low = Integer.MAX_VALUE;
      parent = Graph.NIL;
    }
  }
  
  // Prevent this algorithm from being instantiated
  public ArticulationPoints() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Finds the Articulation Points in the given graph.
   *
   * @param graph the graph to find the articulation points of
   * @return an {@code Object} array of the integer AP vertices
   */
  public static Object[] compute(Graph graph) {
    Node[] V = new Node[graph.getRows()];
    LinkedList<Integer> AP = new LinkedList<>();
    int[] time = {0};

    for (int u : graph.getVertices())
      V[u] = new Node();

    for (int u : graph.getVertices())
      if (!V[u].visited)
        AP_DFS(graph, V, AP, time, u);
    return AP.toArray();
  }

  /**
   * Counts the number of children under the vertex {@code u}. Will set the
   * discovery and low time of the vertex to the current time plus one. Will
   * iterate through each adjacent vertex and if not visited, visit it. If already
   * visited but isn't the parent, it will set the low of the vertex to be the
   * minimum of the current low and the discovery time of the adjacent vertex.
   *
   * @param G    the graph
   * @param V    the array of nodes holding the attributes
   * @param AP   the linkedlist to add the AP vertices
   * @param time the current time for vertices visited
   * @param u    the current vertex
   */
  private static void AP_DFS(Graph G, Node[] V, LinkedList<Integer> AP, int[] time, int u) {
    // Counts the number of children of vertex u
    int child = 0;
    V[u].visited = true;
    // Vertex u is now discovered, its earliest discovered vertex it is connected
    // to is itself; discoverty time is equal to u.low
    V[u].disc = V[u].low = time[0]++;

    for (int v : G.getAdjacentVertices(u)) {
      if (!V[v].visited) {
        // Increment number of children for vertex u
        child++;
        // Set parent of v to u
        V[v].parent = u;
        // Go into v
        AP_DFS(G, V, AP, time, v);
        // When DFS returns, v.low will have the discovery time of the earliest
        // discovered vertex that can be reached from any vertex in the subtree
        // rooted at v. So, set u.low to the minimum of itself and v.low
        V[u].low = Math.min(V[u].low, V[v].low);
        // u is an AP if: u is the root (no parent) with more than one child
        // OR if u is not the root and vertex v's earliest discovered vertex
        // is greater than or equal to the discovery time of vertex u
        if ((V[u].parent == Graph.NIL && child > 1)
          || (V[u].parent != Graph.NIL && V[v].low >= V[u].disc)
        ) {
          // If u hasn't already been visited and marked as an AP, then mark
          // it and add to queue to prevent duplicates
          if (!V[u].AP) {
            V[u].AP = true;
            AP.insertLast(u);
          }
        }
      }
      // If vertex v is connected to u but already visited, update u.low to the
      // minimum of u.low and v.disc.
      else if (V[u].parent != v) {
        V[u].low = Math.min(V[u].low, V[v].disc);
      }
    }
  }
}
