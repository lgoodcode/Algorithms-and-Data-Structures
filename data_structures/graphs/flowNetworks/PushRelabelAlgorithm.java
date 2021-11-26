package data_structures.graphs.flowNetworks;

/**
 * The Push-Relabel algorithm works differently than the Ford-Fulkerson method;
 * it works in a more localized manner by working on one vertex at a time,
 * looking only at the vertex's neighbors in the residual network rather than
 * examining the entire residual network to find an augmenting path.
 *
 * <p>
 * Unlike the Ford-Fulkerson method, push-relabel algorithms <b>DO NOT</b>
 * maintain the <i><b>Flow-Conservation</b></i> property throughout their
 * execution. They do, however, maintain a <i><b>Preflow</b></i>, which is a
 * function {@code f: V x V -> R} that satisfies the <i><b>Capacity
 * Constraint</b></i>.
 * </p>
 *
 * <p>
 * For all vertices {@code u} of <i>V - {s}</i>, flow into a vertex may exceed
 * the flow out, known as the <i><b>Excess Flow</b></i> into vertex {@code u}.
 * The excess at a vertex is the amount by which the flow in exceeds the flow
 * out. We call the vertex {@code u} <i><b>Overflowing<b/></i> if
 * {@code e(u) > 0}.
 * </p>
 *
 * <p>
 * The flow can only go <i>downhill</i>, from a vertex {@code u} with height
 * greater than {@code v} by one; {@code h(u) == h(v) + 1}. The excess flow that
 * can't be pushed down anymore from a vertex is pushed into an imaginary
 * resovoir where, at the end, it will return back to the source so that the
 * source preflow is the negative value of the flow at the sink, for flow
 * conservation.
 * </p>
 *
 * <p>
 * The Push operation only applies to an overflowing vertex {@code u} to another
 * vertex {@code v} where their edge {@code (u, v)} has a positive residual
 * capacity {@code c(u, v) - f(u, v) > 0} and vertex {@code u} has a height
 * greater than {@code v} by one: {@code h(u) == h(v) + 1}. The height
 * difference is necessary because the flow cannot "skip" or jump around. It
 * pushes the smaller of the two; the excess flow of the vertex {@code e(u)} or
 * the residual capacity of the edge {@code (u, v)}. This is to preserve the
 * <i><b>capacity constraint<b/></i>; we cannot send more flow than it can hold.
 * If excess flow remains, the edge is <i><b>Saturated</b></i>, where the
 * residual capacity is {@code 0}, that excess goes to the <i>resovoir</i> where
 * it will go back to the source.
 * </p>
 * 
 * <p>
 * The Relabel operation applies when a vertex is overflowing, {@code e(u) > 0}
 * and there is no adjacent vertex with height {@code h(v)} less than
 * {@code h(u)} by one, meaning we cannot push the flow anywhere so we need to
 * relabel the height of {@code u} by increasing it by {@code 1} so the flow can
 * go "downhill". It will find the smallest height of the vertices adjacent to
 * {@code u} that are in the residual network, meaning {@code (u, v)} has a
 * positive residual capacity, so it can receive flow to preserve the capacity
 * constraint. Once the smallest height of its neighbors is found we add that
 * value by {@code 1} and set it as the new height of {@code u}. After
 * relabelling, we are able to push flow to rid the excess flow from {@code u}.
 * </p>
 * 
 * <p>
 * The residual network initializes by setting the <i><b>Preflow</b></i>, where
 * the excess flow of the source is the negative value of all the capacities of
 * it's adjacent neighbors, where the flow starts from. We then continuously
 * look for overflowing vertices to push until all {@code v} of <i>V - {s,
 * t}</i> have no excess flow.
 * </p>
 */
public class PushRelabelAlgorithm {
  // Prevent the algorithms from being instantiated
  PushRelabelAlgorithm() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * Push-Relabel node containing the height label and excess flow.
   */
  protected static class Node {
    int vertex;
    int height;
    int excess;

    Node(int vertex) {
      this.vertex = vertex;
      height = 0;
      excess = 0;
    }
  }

  /**
   * Initialize-Preflow(G, s)
   * 1   for each vertex v of G.V
   * 2       v.h = 0
   * 3       v.e = 0
   * 4   for each edge (u, v) of G.E
   * 5       (u, v).f = 0
   * 6   s.h = |G.V|
   * 7   for each vertex v of adjacent of s
   * 8       f(s, v) = c(s, v)
   * 9       v.e = c(s, v)
   * 10      s.e = s.e - c(s, v)
   */

  /**
   * The operation creates an initial preflow {@code f} defined by:
   *
   * <p>
   * {@code f(u, v): u == s ? c(u, v) : 0}
   * </p>
   * 
   * <p>
   * We fill only each edge leaving the source {@code s} to the capacity. All
   * other edges have no flow. Then, for each vertex {@code v} adjacent to the
   * source, will have {@code v.excess = c(s, v)}, and we initialize
   * {@code s.excess} to the negative of the sum of the capacities of the edges
   * leaving the source that we filled the flow of. This is to satisfy the flow
   * conservation property.
   * </p>
   * 
   * <p>
   * The height of the source vertex is set to the total number of vertices in the
   * flow network, minus {@code 2} because the source and sink vertices aren't
   * used in the relabel operations. This ensures that it will be able to perform
   * push/relabel operations.
   * </p>
   * 
   * @param network the flow network
   * @param s       the source
   * @return the vertex nodes with the initialized preflow for the source
   *         {@code s}
   */
  protected static Node[] initializePreflow(FlowNetwork network, Node[] VTS, int s) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    int c, v;

    VTS[s].height = network.getNumVertices() - 2;

    for (FlowNetwork.Edge edge : network.getEdges(s)) {
      v = edge.getVertices()[1];
      c = G[s][v].getCapacity();

      G[s][v].setFlow(c);
      G[v][s].subtractFlow(c);
      VTS[v].excess = c;
      VTS[s].excess -= c;
    }

    return VTS;
  }

  /**
   * Push(u, v)
   * 1   excessflow(u, v) = min(u.e, cf(u, v))
   * 2   if (u, v) is a member of E
   * 3       (u, v).f = (u, v).f + excessflow(u, v)
   * 4   else (v, u).f = (v, u).f - excessflow(u, v)
   * 5   u.e = u.e - excessflow(u, v)
   * 6   v.e = v.e + excessflow(u, v)
   */

  /**
   * Pushes the flow from an overflowing vertex to an adjacent vertex, that is,
   * where {@code h(u) = h(v) + 1}.
   * 
   * <p>
   * <i>Overflowing vertex</i>: {@code cf(u, v) > 0 or e(u) > 0}
   * </p>
   * 
   * @param network the flow network
   * @param VTS     the nodes for the algorithm
   * @param u       the overflowing vertex to push flow from
   * @param v       the adjacent vertex to receive flow
   */
  protected static void push(FlowNetwork network, Node[] VTS, int u, int v) {
    FlowNetwork.Edge[][] G = network.getAdjacencyMatrix();
    int c = G[u][v].getCapacity();
    int f = G[u][v].getFlow();
    int flow = Math.min(VTS[u].excess, c - f);
    G[u][v].addFlow(flow);
    G[v][u].subtractFlow(flow);
    VTS[u].excess -= flow;
    VTS[v].excess += flow;  
  }

  /**
   * Relabel(u)
   * 1   u.h = 1 + min{v.h : (u, v) of Ef}
   */

  /**
   * The operation applies if {@code u} is overflowing and if {@code u.h <= v.h}
   * for all edges {@code (u, v)} of {@code Ef}. Finds the smallest height label
   * of all adjacent vertices of {@code u} that has a positive residual capacity
   * and sets it that height, plus one.
   * 
   * <p>
   * In other words, we relabel an overflowing vertex if, for every vertex
   * {@code v} there is residual capacity from {@code u} to {@code v}, with the
   * smallest height, plus one, so that it can push flow <i>"downhill"</i>.
   * </p>
   * 
   * <p>
   * By definition, netiher the source {@code s} nor the sink {@code t} can be
   * overflowing, and so {@code s} and {@code t} are ineligible for relabeling.
   * </p>
   */
  protected static void relabel(FlowNetwork network, Node[] VTS, int u) {
    int v, min = Integer.MAX_VALUE;

    for (FlowNetwork.Edge edge : network.getEdges(u)) {
      v = edge.getVertices()[1];

      if (VTS[v].height < min && edge.getCapacity() > edge.getFlow())
        min = VTS[v].height;
    }

    VTS[u].height = min + 1;
  }

  /**
   * Finds any overflowing vertex, that is, has either a positive residual
   * capacity or excess flow, and returns it. The source {@code s} and sink
   * {@code t} vertices are ineligible for relabeling or to be used as an
   * overflowing vertex.
   * 
   * @param network the flow network
   * @param VTS     the nodes for the algorithm
   * @param s       the source vertex
   * @param t       the sink vertex
   * @retrun the first overflowing vertex found or {@code -1} if none found
   */
  protected static int overflowingVertex(FlowNetwork network, Node[] VTS, int s, int t) {
    for (int u : network.getVertices())
      if (u != s && u != t && VTS[u].excess > 0)
        return u;
    return -1;
  }

}
