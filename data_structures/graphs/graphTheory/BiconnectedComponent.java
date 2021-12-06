package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;
import data_structures.stacks.Stack;

/**
 * <i><b>Biconnected Component - </b></i>A maximal set of edges such that any two
 * edges in the set lie on a common simple cycle. Simply put; a connected
 * component that consists of at least two edges where even with the removal of
 * any vertex, all vertices are still reachable from any vertex.
 *
 * <p>
 * A graph is Biconnected if:
 * <ul>
 * <li>It is <i><b>connected</b></i>; it is possible to reach every vertex from
 * every other vertex, by a simple path.</li>
 * <li>Even after removing any vertex the graph remains connected</li>
 * </ul>
 * </p>
 */
public final class BiconnectedComponent extends GraphTheory {
  // Prevent this class from being instantiated
  public BiconnectedComponent() {
    super();
  }

  /**
   * Finds the Biconnected Componenets in the given graph.
   *
   * @param graph the graph to find the biconencted components of
   * @return an {@code Object} array of the integer AP vertices
   */
  public static Object[] compute(Graph graph) {
    Node[] V = new Node[graph.getRows()];
    Stack<Graph.Edge> edges = new Stack<>(graph.getNumEdges());
    LinkedList<Object[]> BCC = new LinkedList<>();
    int[] time = {0};

    for (int u : graph.getVertices())
      V[u] = new Node();

    for (int u : graph.getVertices()) {
      if (!V[u].visited) {
        BCC_DFS(graph, V, BCC, edges, time, u);

        // Check if there is a leftover BCC in the stack
        if (!edges.isEmpty()) {
          LinkedList<Integer[]> group =new LinkedList<>();

          while (!edges.isEmpty())
            group.insert(edges.pop().toArray());
          BCC.insert(group.toArray());
        }
      }
    }

    return BCC.toArray();
  }

  /**
   * As each unique edge is visited, it is added to the Stack which will be used
   * to form the Biconnected Components (BCC). A stack structure is necessary
   * because the BCC is formed from the most recently visited edges, which needs
   * FILO. Once an Articulation Point is found, it pops edges from the stack and
   * add them to a LinkedList to hold the current BCC until we pop an edge that is
   * the currently visited edge of {@code (u, v)}. If the BCC group consists of at
   * least {@code 2} edges, then it satisfies the requirement and is then added
   * the the LinkedList of all the BCC's found.
   *
   * @param G     the graph
   * @param V     the array of nodes holding the attributes
   * @param BCC   the LinkedList holding the groups of BCC's
   * @param edges the Stack holding the visited edges
   * @param time  the current time for vertices visited
   * @param u     the current vertex
   */
  private static void BCC_DFS(Graph G, Node[] V, LinkedList<Object[]> BCC,
Stack<Graph.Edge> edges, int[] time, int u) {
    int child = 0;
    V[u].visited = true;
    V[u].disc = V[u].low = ++time[0];

    for (int v : G.getAdjacentVertices(u)) {
      Graph.Edge edge = G.getEdge(u, v);

      if (!V[v].visited) {
        child++;

        V[v].parent = u;

        edges.push(edge);

        BCC_DFS(G, V, BCC, edges, time, v);

        V[u].low = Math.min(V[u].low, V[v].low);

        // Once an AP is found, start putting together a BCC from the edges visited
        if ((V[u].parent == Graph.NIL && child > 1)
          || (V[u].parent != Graph.NIL && V[v].low >= V[u].disc)
        ) {
          LinkedList<Integer[]> group = new LinkedList<>();
          Graph.Edge e;
          int x, y;

          // Pop the edges from the stack to access the most recently visited
          // edges first. Continue until we reach the current visited edge (u, v)
          do {
            e = edges.pop();
            x = e.getVertices()[0];
            y = e.getVertices()[1];
            group.insert(e.toArray());
          } while (u != x || v != y);

          // If there is at least two edges, then it is a BCC and add it
          if (group.size() > 1)
            BCC.insert(group.toArray());
        }
      }
      // If vertex v is connected to u but already visited AND v was discovered
      // before the earliest discovered vertex in the subtree of u, update u.low
      // to the minimum of u.low and v.disc.
      else if (V[u].parent != v && V[v].disc < V[u].low) {
        V[u].low = Math.min(V[u].low, V[v].disc);
        edges.push(edge);
      }
    }
  }
}
