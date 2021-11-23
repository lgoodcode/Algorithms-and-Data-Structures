package data_structures.graphs.flowNetworks;

import data_structures.linkedLists.LinkedList;

public class MaxFlowAlgorithm {
  // Represents no vertex predecessor
  protected static final int NIL = -1;

  // Prevent the algorithms from being instantiated
  public MaxFlowAlgorithm() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  /**
   * BFS/DFS node
   */
  protected static class Node {
    private int vertex;
    protected boolean visited;
    protected int predecessor;

    protected Node(int vertex) {
      this.vertex = vertex;
      visited = false;
      predecessor = -1;
    }

    public int getVertex() {
      return vertex;
    }

    public boolean visited() {
      return visited;
    }
  }

  /**
   * Holds the calculated the maximum flow and the paths that derived it. Uses a
   * {@link LinkedList} to hold the unknown number of paths that the algorithm
   * will find.
   */
  protected static final class FlowPaths {
    private int flow;
    private LinkedList<String> paths;

    protected FlowPaths() {
      flow = 0;
      paths = new LinkedList<>();
    }

    protected void addFlow(int flow) {
      this.flow += flow;
    }

    protected void addPath(String path) {
      this.paths.insert(path);
    }

    public int getFlow() {
      return flow;
    }

    public Object[] getPaths() {
      return paths.toArray();
    }
  }

  /**
   * Recursively finds the residual capacity: the maximum amount we can increase
   * the flow along the path, which is the minimum capacity capacity along the
   * path {@code p}:
   *
   * <p>
   * <i>cf(p) = min {cf(u, v) : (u, v) is in p}</i>
   * </p>
   *
   * <p>
   * If there is a positive residual capacity, then we can augment the edges along
   * the path {@code p} and update the flow.
   * </p>
   *
   * <p>
   * This is more efficient because it recursively backtracks along the path from
   * the sink to the source, finding the residual capacity and updating the flow
   * of the edges at the same time.
   * </p>
   *
   * @param G    the flow network adjacency matrix
   * @param VTS  the vertex nodes
   * @param flow the current maximum flow that can be augmented
   * @param v    the current vertex along the
   * @return the residual capacity for the augmenting path {@code p} or {@code 0}
   *         if there is no possible augmenting path
   */
  protected static int residualCapacity(FlowNetwork.Edge[][] G, Node[] VTS, int flow, int v) {
    if (VTS[v].predecessor == NIL)
      return flow;

    int u = VTS[v].predecessor;
    int cf = Math.min(flow, G[u][v].getCapacity() - G[u][v].getFlow());
    // cf(p) = min {cf(u, v) : (u, v) is in p} where cf(u, v) = c(u, v) - f(u, v)
    int cfP = residualCapacity(G, VTS, cf, u);

    // If there is a residual capacity, we can augment edges along path p; update edges
    if (cfP > 0) {
      G[u][v].addFlow(cfP);
      G[v][u].subtractFlow(cfP);
      return cfP;
    }

    return 0;
  }

  /**
   * Recursively finds the residual capacity and builds a string path.
   *
   * @param G    the flow network adjacency matrix
   * @param VTS  the vertex nodes
   * @param sb   the {@code StringBuilder} to add the vertex for the path
   * @param flow the current maximum flow that can be augmented
   * @param v    the current vertex along the
   * @return the residual capacity for the augmenting path {@code p} or {@code 0}
   *         if there is no possible augmenting path
   */
  protected static int printResidualCapacity(FlowNetwork.Edge[][] G, Node[] VTS, StringBuilder sb, int flow, int v) {
    if (VTS[v].predecessor == NIL)
      return flow;    

    int u = VTS[v].predecessor;
    int cf = Math.min(flow, G[u][v].getCapacity() - G[u][v].getFlow());
    // cf(p) = min {cf(u, v) : (u, v) is in p} where cf(u, v) = c(u, v) - f(u, v)
    int cfP = printResidualCapacity(G, VTS, sb, cf, u);

    // If there is a residual capacity, we can augment edges along path p; update edges
    if (cfP > 0) {
      G[u][v].addFlow(cfP);
      G[v][u].subtractFlow(cfP);

      // Add vertex to the path
      sb.append(u + " >- ");
      
      return cfP;
    }

    return 0;
  }
}
