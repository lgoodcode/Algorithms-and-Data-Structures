package data_structures.graphs.flowNetworks;

import java.util.Iterator;

import data_structures.linkedLists.LinkedList;

public class MaxFlowAlgorithm {
  /**
   * Represents no vertex
   */
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
   * 
   * Is initialized different whether it will be used to build the paths as
   * strings or arrays based on the boolean constructor.
   */
  protected static final class FlowPaths {
    /**
     * Holds the paths of strings
     */
    private LinkedList<String> paths;

    /**
     * Holds the flow for each path of the array paths
     */
    private LinkedList<Integer> flows;

    /**
     * Holds the length of each path of the array paths
     */
    private LinkedList<Integer> lengths;

    /**
     * Holds the vertices of each path of the array paths
     */
    private LinkedList<Object[]> vertices;

    /**
     * Constructs an object to hold the paths based on the type initialized.
     * 
     * @param string whether this will hold string or array paths
     */
    protected FlowPaths(boolean string) {
      if (string)
        paths = new LinkedList<>();
      else {
        flows = new LinkedList<>();
        vertices = new LinkedList<>();
        lengths = new LinkedList<>();
      }
    }

    /**
     * Adds a string path - which also contains the flow at the start -to the
     * {@code LinkedList} holding the paths.
     * 
     * @param path the string path to add
     */
    protected void addPath(String path) {
      this.paths.insert(path);
    }

    /**
     * Adds the {@code LinkedList} of the vertices for the path and the flow for the
     * array of paths.
     * 
     * @param path the linkedlist of vertices for the path
     * @param flow the flow for the path
     */
    protected void addPath(LinkedList<Integer> path, int flow) {
      Object[] arr = path.toArray();
      flows.insertLast(flow);
      vertices.insertLast(arr);
      lengths.insertLast(arr.length);
    }

    /**
     * Returns the array of string paths in the order the algorithm processed it.
     * 
     * @return the array of string paths
     */
    protected String[] getStringPaths() {
      String[] p = new String[paths.size()];
      int i = paths.size() - 1;

      for (String path : paths.iterable())
        p[i--] = path;
      return p;
    }
    
    /**
     * Returns the array of integer array paths containing the flow as the first
     * index value and the path vertices for the rest.
     * 
     * @return the array of paths
     */
    protected Integer[][] getArrayPaths() {
      Integer[][] arr = new Integer[flows.size()][];
      Iterator<Object[]> v = vertices.iterator();
      Iterator<Integer> len = lengths.iterator();
      Object[] vertices, f = flows.toArray();
      int i, j;

      for (i = 0; i < f.length; i++) {
        vertices = v.next();
        arr[i] = new Integer[len.next() + 1];
        arr[i][0] = (Integer) f[i];

        for (j = 0; j < vertices.length; j++)
          arr[i][j+1] = (Integer) vertices[j];
      }

      return arr;
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
      sb.append(u + " -> ");
      
      return cfP;
    }

    return 0;
  }

  /**
   * Recursively finds the residual capacity and builds an array of the paths.
   *
   * @param G    the flow network adjacency matrix
   * @param VTS  the vertex nodes
   * @param L    the {@link LinkedList} to add the vertex for the path
   * @param flow the current maximum flow that can be augmented
   * @param v    the current vertex along the
   * @return the residual capacity for the augmenting path {@code p} or {@code 0}
   *         if there is no possible augmenting path
   */
  protected static int arrayResidualCapacity(FlowNetwork.Edge[][] G, Node[] VTS, LinkedList<Integer> L, int flow, int v) {
    if (VTS[v].predecessor == NIL)
      return flow;    

    int u = VTS[v].predecessor;
    int cf = Math.min(flow, G[u][v].getCapacity() - G[u][v].getFlow());
    // cf(p) = min {cf(u, v) : (u, v) is in p} where cf(u, v) = c(u, v) - f(u, v)
    int cfP = arrayResidualCapacity(G, VTS, L, cf, u);

    // If there is a residual capacity, we can augment edges along path p; update edges
    if (cfP > 0) {
      G[u][v].addFlow(cfP);
      G[v][u].subtractFlow(cfP);

      // Add vertex to end of list so it is in order
      L.insertLast(u);
      
      return cfP;
    }

    return 0;
  }
}
