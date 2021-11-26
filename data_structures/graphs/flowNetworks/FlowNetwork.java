package data_structures.graphs.flowNetworks;

import static java.util.Arrays.copyOf;

import java.util.NoSuchElementException;

/**
 * <b>Flow Network:</b>
 *
 * <ul>
 * <li>{@code G = (V, E)} is a directed graph in which each edge {@code (u, v)}
 * of {@code E} has a nonnegative <i>Capacity</i> {@code c(u, v) >= 0}.</li>
 *
 * <li>Requires that if {@code E} contains an edge {@code (u, v)}, then there is
 * no edge {@code (v, u)} in the reverse direction.</li>
 *
 * <li>Two vertices: <i>Source</i> {@code s} and <i>Sink</i> {@code t}</li>
 *
 * <li>We assume for convenience that each vertex {@code v} of {@code V} lies on
 * some path from the source to the sink. That is, the flow network contains a
 * path {@code s ~ v ~ t}.</li>
 *
 * <li>The graph is connected, and since each vertex other than {@code s} has at
 * least one entering edge: {@code |E| >= |V| - 1}.</li>
 * </ul>
 *
 * <b>Flow:</b>
 *
 * <ul>
 * <li>A flow in {@code G} is a real-valued function {@code f : V x V -> R} that
 * satisfies the following two properties:
 * <ul>
 * <li><b>Capacity constraint</b>: For all {@code u}, {@code v} of {@code V}, we
 * require {@code 0 <= f(u, v) <= c(u, v)}.</li>
 *
 * <li><b>Flow conservation:</b> For all {@code u} of {@code V - [s, t]}, we
 * require:
 * <ul>
 * <li><i>Summation of all v of V - f(v, u) = Summation of all v of V - f(u,
 * v).</i></li>
 *
 * <li>When {@code (u, v)} isn't a member of {@code E}, there can be no flow
 * from {@code u} to {@code v}, and {@code f(u, v) = 0}</li>
 * </ul>
 * </li>
 * </ul>
 * </ul>
 *
 * <p>
 * <b>Antiparallel Edges:</b> If edge (v1, v2) and (v2, v1)
 * </p>
 *
 * <b>Networks with multiple sources and sinks:</b>
 *
 * <p>
 * The problem can be converted to an ordinary flow network with only a single
 * source with a single sink. Adding a <i>Supersource</i> {@code s} and a
 * directed edge {@code (s, si)} with capacity {@code c(s, si) = Infinity} for
 * each multiple source. We also add a <i>Supersink</i> {@code t} and add a
 * directed edge {@code (ti, t)} with capacity {@code c(ti, t) = Infinity} for
 * each multiple sink.
 * </p>
 *
 * <hr/>
 *
 * <h3>Residual Networks</h3>
 *
 * <p>
 * An edge of the flow network can admit an amount of additional flow equal to
 * the edge's capacity minus the flow on that edge. If that value is positive,
 * we place that edge into the residual network, {@code Gf} with a
 * </p>
 *
 * <h4><i>Residual Capacity</i> of {@code cf(u, v) = c(u, v) - f(u, v)}</h4>
 *
 * <p>
 * The only edges of {@code G} that are in {@code Gf} are those that can admit
 * more flow; those edges {@code (u, v)} whose flow equals their capacity have
 * {@code cf(u, v) = 0}, and they are not in {@code Gf}.
 * </p>
 *
 * <p>
 * The residual network {@code Gf} may also contain edges that are not in the
 * flow network {@code G}. As an algorithm manipulates the flow with the goal of
 * increasing the total flow, it might need to decrease the flow on a particular
 * edge. In order to represent a possible decrease of a positive flow
 * {@code f(u, v)} on an edge {@code G}, we place an edge {@code (v, u)} into
 * {@code Gf} with residual capacity {@code cf(u, v) = f(u, v)} - an edge that
 * can admit flow in the opposite direction to {@code (u, v)}, at most canceling
 * out the flow on {@code (u, v)}.
 *
 * <p>
 * Sending flow back along an edge is equivalent to decreasing the flow on the
 * edge, which is a necessary operation in many algorithms.
 * </p>
 *
 * { c(u, v) - f(u, v) if (u, v) is a member of E Residual Capacity cf(u, v) = {
 * f(u, v) if (v, u) is a member of E { 0 otherwise
 *
 * <p>
 * <b>Residual Capacity</b>: the maximum amount we can increase the flow on each
 * edge in augmenting path {@code p} which is the minimum edge capacity value
 * along the path {@code s} to {@code t} of path {@code p}.
 * </p>
 *
 * <p>
 * Given a flow network {@code G = (V, E)} and a flow {@code f}, the <i>Residual
 * Network</i> of {@code G} induced by {@code f} is {@code Gf = (V, Ef)}.
 * </p>
 *
 * <i>Ef = {(u, v) member of V x V : cf(u, v) > 0}</i>
 *
 * <p>
 * The edges in {@code Ef} are either edges in E or their reversals such that
 * {@code |Ef| <= 2|E|}
 * </p>
 *
 * <p>
 * A flow in a residual network provides a roadmap for adding flow to the
 * original flow network. If {@code f} is a flow in {@code G} and {@code f'} is
 * a flow in the corresponding residual network {@code Gf}, we define
 * {@code f|f'}, the <i>Augmentation</i> of flow {@code f} by {@code f'}, to be
 * a function from {@code V x V to R} defined by:
 *
 * (f|f')(u, v) = { f(u, v) + f'(u, v) - f'(v, u) if (u, v) is a member of E { 0
 * otherwise
 *
 * <p>
 * We increase the flow on {@code (u, v)} by {@code f'(u, v)} but decrease it by
 * {@code f'(v, u)} because pushing flow on the reverse edge in the residual
 * network signifies decreasing the flow in the original network. Pushing flow
 * on the reverse edge in the residual network is known as <i>Cancellation</i>.
 * </p>
 *
 * <h4>Augmenting Paths:</h4>
 *
 * <p>
 * Given a flow network {@code G} and a flow {@code f}, an <i>Augmenting
 * Path</i> {@code p} is a simple path from {@code s} to {@code t} in the
 * residual network {@code Gf}. We may increase the flow on an edge
 * {@code (u, v)} of an augmenting path by up to {@code cf(u, v)} without
 * violating the capacity constraint on whichever {@code (u, v)} and
 * {@code (v, u)} is in the original flow network {@code G}.
 * </p>
 */
public final class FlowNetwork {
  /**
   * The length and width of the flow network.
   */
  private final int rows;

  /**
   * The flow network matrix.
   */
  private Edge[][] G;

  /**
   * The number of vertices contained in the flow network.
   */
  private int vertices;

  /**
   * The number of edges contained in the flow network.
   */
  private int edges;

  /**
   * Used to hold attributes of an algorithm for each vertex. Is meant to be
   * extended for algorithm-specific implementations.
   */
  public static class Vertex {
    private int vertex;
    protected int distance;
    protected int predecessor;

    protected Vertex(int vertex) {
      this.vertex = vertex;
      distance = Integer.MAX_VALUE;
      predecessor = -1;
    }

    public int getVertex() {
      return vertex;
    }
  }

  /**
   * The static class to easily retrieve the edges from the flow network and to be
   * able to access the vertices as well as the capcity and weight of the edge.
   */
  public static final class Edge {
    private int u;
    private int v;
    private int c;
    private int f;

    private Edge(int u, int v) {
      this.u = u;
      this.v = v;
      this.c = 0;
      this.f = 0;
    }

    private Edge(int u, int v, int capacity) {
      checkCapacity(capacity);

      this.u = u;
      this.v = v;
      this.c = capacity;
      this.f = 0;
    }

    private Edge(int u, int v, int capacity, int flow) {
      checkCapacity(capacity);
      checkFlow(flow);

      this.u = u;
      this.v = v;
      this.c = capacity;
      this.f = flow;
    }

    /**
     * Returns a two element {@code int} array containing the {@code u} and
     * {@code v} vertices.
     *
     * @return the edge vertices array
     */
    public int[] getVertices() {
      int[] V = { u, v };
      return V;
    }

    /**
     * Retrieves the capacity of the edge.
     *
     * @return the edge capacity
     */
    public int getCapacity() {
      return c;
    }

    /**
     * Retrieves the flow of the edge.
     *
     * @return the edge flow
     */
    public int getFlow() {
      return f;
    }

    /**
     * Sets the capacity of the edge.
     *
     * @param capacity the new edge capacity
     *
     * @throws IllegalArgumentException if the capacity is less than {@code 0}
     */
    public void setCapacity(int capacity) {
      checkCapacity(capacity);
      this.c = capacity;
    }

    /**
     * Sets the flow of the edge.
     *
     * @param flow the new edge flow
     *
     * @throws IllegalArgumentException if the flow is less than {@code 0}
     */
    public void setFlow(int flow) {
      checkFlow(flow);
      this.f = flow;
    }

    /**
     * Adds the flow to the current flow of the edge.
     *
     * @param flow the flow to add
     *
     * @throws IllegalArgumentException if the flow is less than {@code 0}
     */
    public void addFlow(int flow) {
      checkFlow(flow);
      this.f += flow;
    }

    /**
     * Subtracts the flow to the current flow of the edge. Used for flow
     * preservation when manipulating the flow in a direction. By having flow added
     * to the opposite direction, it keeps a balance of the total flow in the
     * network.
     *
     * @param flow the flow to subtract
     *
     * @throws IllegalArgumentException if the flow is less than {@code 0}
     */
    public void subtractFlow(int flow) {
      checkFlow(flow);
      this.f -= flow;
    }
  }

  /**
   * Constructs an empty, flow network matrix, with the specified number of rows
   * for an n x n network.
   *
   * @param rows the number of rows and columns in the network
   *
   * @throws IllegalArgumentException if the specified number of rows is less than
   *                                  {@code 1}
   */
  public FlowNetwork(int rows) {
    if (rows < 0)
      throw new IllegalArgumentException("Number of rows must be greater than 0.");
    this.rows = rows;
    vertices = 0;
    edges = 0;
    G = new Edge[rows][];
  }

  /**
   * Constructs a deep copy of the specified {@link FlowNetwork}.
   *
   * @param network the flow network to copy
   *
   * @throws NullPointerException if the network is {@code null}
   */
  public FlowNetwork(FlowNetwork network) {
    if (network == null)
      throw new NullPointerException("FlowNetwork cannot be null.");

    rows = network.rows;
    vertices = network.vertices;
    edges = network.edges;

    Edge[][] N = network.getAdjacencyMatrix();
    G = new Edge[rows][];
    int i, j;

    for (i = 0; i < rows; i++) {
      if (N[i] != null) {
        G[i] = new Edge[rows];

        for (j = 0; j < rows; j++) {
          if (N[i][j] != null) 
            G[i][j] = new Edge(i, j, N[i][j].c, N[i][j].f);
        }
      }
    }
  }

  /**
   * Constructs a deep copy and extends the length of the specified
   * {@link FlowNetwork}.
   *
   * @param network the flow network to copy
   * @param newRows    the new flow network length
   *
   * @throws NullPointerException     if the flow network is {@code null}
   * @throws IllegalArgumentException if the specified new length is less than the
   *                                  original flow network
   */
  public FlowNetwork(FlowNetwork network, int newRows) {
    if (network == null)
      throw new NullPointerException("FlowNetwork cannot be null.");
    if (newRows < network.rows)
      throw new IllegalArgumentException("New network length can't be less than the original.");

    this.rows = newRows;
    vertices = network.vertices;
    edges = network.edges;

    Edge[][] N = network.getAdjacencyMatrix();
    G = new Edge[newRows][];
    int i, j, currRows = network.rows;

    for (i = 0; i < currRows; i++) {
      if (N[i] != null) {
        G[i] = new Edge[newRows];

        for (j = 0; j < currRows; j++) {
          if (N[i][j] != null) 
            G[i][j] = new Edge(i, j, N[i][j].c, N[i][j].f);
        }
      }
    }
  }

  /**
   * Returns the transpose of the current flow network, which is all the edges
   * reversed.
   *
   * @return the transpose of the flow network
   */
  public FlowNetwork transpose() {
    FlowNetwork G = new FlowNetwork(rows);

    for (int u : getVertices()) {
      for (Edge edge : getEdges(u))
        G.addEdge(edge.v, u, edge.c, edge.f);
    }

    return G;
  }

  /**
   * Checks that the specified vertex index is valid.
   *
   * @param vertex the vertex index
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  flow network length
   */
  public void checkVertex(int vertex) {
    if (vertex < 0)
      throw new IllegalArgumentException("Vertex cannot be negative.");
    if (vertex >= rows)
      throw new IllegalArgumentException("Vertex cannot be greater than flow network length.");
  }

  /**
   * Checks that the specified vertex index is valid for the specified length of
   * the flow network.
   *
   * @param rows   the length of the flow network
   * @param vertex the vertex index
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  flow network length
   */
  public static void checkVertex(int rows, int vertex) {
    if (vertex < 0)
      throw new IllegalArgumentException("Vertex cannot be negative.");
    if (vertex >= rows)
      throw new IllegalArgumentException("Vertex cannot be greater than flow network length.");
  }

  /**
   * Checks the specified capacity that it satisfies the <i>capacity
   * constraint</i> property of flow networks, where {@code 0 <= f(u, v) <= c(u, v)}.
   *
   * @param capacity the capacity to check
   *
   * @throws IllegalArgumentException if the capacity is less than {@code 0}
   */
  private static void checkCapacity(int capacity){
    if (capacity < 0)
      throw new IllegalArgumentException("Capacity Constraint: capacity cannot be less than 0. ");
  }

  /**
   * Checks the specified flow that it satisifes the flow network properties where
   * {@code 0 <= f(u, v) <= c(u, v)}.
   *
   * @param flow the flow to check
   *
   * @throws IllegalArgumentException if the capacity is less than {@code 0}
   */
  private static void checkFlow(int flow) {
    if (flow < 0)
      throw new IllegalArgumentException("Flow cannot be less than 0.");
  }

  /**
   * Returns the number of rows in the flow network matrix.
   *
   * @return the number of rows in the flow network
   */
  public int getRows() {
    return rows;
  }

  /**
   * Returns a copy of the matrix adjacency representation.
   *
   * @return the adjacency matrix
   */
  public Edge[][] getAdjacencyMatrix() {
    return G;
  }

  /**
   * Returns the number of vertices in the flow network.
   *
   * @return the number of vertices
   */
  public int getNumVertices() {
    return vertices;
  }

  /**
   * Returns the number of edges in the flow network.
   *
   * @return the number of edges
   */
  public int getNumEdges() {
    return edges;
  }

  /**
   * Returns an array with a length of the number of vertices and each element is
   * the vertex.
   *
   * @return the array of vertices indices
   */
  public int[] getVertices() {
    int[] V = new int[rows];
    int i, j = 0;

    for (i = 0; i < V.length; i++)
      if (G[i] != null)
        V[j++] = i;
    return copyOf(V, j);
  }

  /**
   * Returns an array of the adjacent vertices of the specified vertex.
   * 
   * @param u the vertex whose adjacent vertices is needed
   * @return the adjacent vertices
   * 
   * @throws IllegalArgumentException if the vertex doesn't exist in the flow
   *                                  network
   */
  public int[] getAdjacentVertices(int u) {
    if (!hasVertex(u))
      throw new IllegalArgumentException("Vertex " + u + " does not exist in flow network.");

    int[] V = new int[rows];
    int i, j, len;

    for (i = 0, j = 0, len = rows; i < len; i++)
      if (G[u][i] != null)
        V[j++] = i;
    return copyOf(V, j);
  }

  /**
   * Returns an array of {@link FlowNetwork.Edge} of all the edges in the flow
   * network.
   *
   * @return array of {@code Edges}
   */
  public Edge[] getEdges() {
    if (edges == 0)
      return new Edge[0];

    int j, k = 0, len = rows;
    Edge[] E = new Edge[edges * 2];

    // Iterate through each vertex
    for (int i : getVertices()) {
      // Iterate through each edge of the vertex
      for (j = 0; j < len; j++) {
        if (G[i][j] != null)
          E[k++] = G[i][j];
      }
    }

    return E;
  }

  /**
   * Returns the edges for the specified vertex.
   *
   * @param u the vertex whose edges is being retrieved
   * @return the {@code Edge} array
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  flow network length or doesn't exist in the
   *                                  flow network
   */
  public Edge[] getEdges(int u) {
    if (!hasVertex(u))
      throw new IllegalArgumentException("Vertex " + u + " does not exist in flow network.");

    Edge[] E = new Edge[vertices];
    int i = 0, v, len = rows;

    // Iterate through each possible adjacent vertex v
    for (v = 0; v < len; v++) {
      if (G[u][v] != null)
        E[i++] = G[u][v];
    }

    return copyOf(E, i);
  }

  /**
   * Determines whether a given vertex exists in the flow network in or not.
   *
   * @param v the vertex index to check
   * @return whether the vertex is in the flow network or not
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the flow network length
   */
  public boolean hasVertex(int v) {
    checkVertex(v);
    return G[v] != null;
  }

  /**
   * Determines whether a given edge exists in the flow network in or not.
   *
   * @param u the u vertex index of the edge
   * @param v the v vertex index of the edge
   * @return whether the edge exists or not
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length
   */
  public boolean hasEdge(int u, int v) {
    checkVertex(u);
    checkVertex(v);
    return G[u] != null && G[u][v] != null;
  }

  /**
   * Returns an {@link FlowNetwork.Edge} and the weight if the flow network is
   * weighted.
   *
   * @param u the u vertex of the edge
   * @param v the v vertex of the edge
   * @return the {@code Edge}
   *
   * @throws NoSuchElementException if the edge does not exist in the flow network
   */
  public Edge getEdge(int u, int v) {
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    return G[u][v];
  }

  /**
   * Adds a specified vertex index to the flow network.
   *
   * @param v the vertex index to add
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the flow network length
   */
  public void addVertex(int v) {
    if (!hasVertex(v)) {
      G[v] = new Edge[rows];
      vertices++;
    }
  }

  /**
   * The default constructor for an edge in the flow network. Adds an edge to the
   * flow network with the given x and y vertices and the edge capacity. If the
   * specified vertices don't exist in the flow network, they will be added.
   *
   * <p>
   * The edge has an initial flow of {@code 0} so that the residual capacity in a
   * residual network can be determined.
   * </p>
   *
   * <p>
   * A reverse of the edge will be added to the flow network with a capacity and
   * flow of {@code 0} to allow reverse flow to be added when manipulating the
   * flow for the residual network.
   * </p>
   *
   * @param u        the u vertex index
   * @param v        the v vertex index
   * @param capacity the edge capacity
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length, or the edge already
   *                                  exists in the flow network, or the capacity
   *                                  is less than {@code 0}
   */
  public void addEdge(int u, int v, int capacity) {
    if (!hasVertex(u))
      addVertex(u);
    if (!hasVertex(v))
      addVertex(v);
    if (hasEdge(u, v))
      throw new IllegalArgumentException("Edge already exists in flow network.");

    checkCapacity(capacity);

    G[u][v] = new Edge(u, v, capacity);
    G[v][u] = new Edge(u, v);
    edges++;
  }

  /**
   * Adds an edge to the flow network with the given x and y vertices as well as
   * the edge capacity and flow. If the specified vertices don't exist in the flow
   * network, they will be added.
   *
   * <p>
   * A reverse of the edge will be added to the flow network with a capacity and
   * flow of {@code 0} to allow reverse flow to be added when manipulating the
   * flow for the residual network.
   * </p>
   *
   * @param u        the u vertex index
   * @param v        the v vertex index
   * @param capacity the edge capacity
   * @param flow     the edge flow
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length, or the edge already
   *                                  exists in the flow network, or the capacity
   *                                  or flow is less than {@code 0}
   */
  public void addEdge(int u, int v, int capacity, int flow) {
    if (!hasVertex(u))
      addVertex(u);
    if (!hasVertex(v))
      addVertex(v);
    if (hasEdge(u, v))
      throw new IllegalArgumentException("Edge already exists in flow network.");

    checkCapacity(capacity);
    checkFlow(flow);

    G[u][v] = new Edge(u, v, capacity, flow);
    G[v][u] = new Edge(u, v);
    edges++;
  }

  /**
   * Update an existing edge capacity and flow. If the edge doesn't exist, will
   * create it.
   *
   * @param u        the u vertex index
   * @param v        the v vertex index
   * @param capacity the new edge capacity
   * @param flow     the new edge flow
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length, or capacity or flow
   *                                  is less than {@code 0}
   */
  public void setEdge(int u, int v, int capacity, int flow) {
    checkCapacity(capacity);
    checkFlow(flow);

    if (!hasEdge(u, v))
      addEdge(u, v, capacity, flow);
    else {
      G[u][v].c = capacity;
      G[u][v].f = flow;
    }
  }

  /**
   * Retrieves the capacity for an edge.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   * @return the edge capacity
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length
   */
  public int getEdgeCapacity(int u, int v) {
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    return getEdge(u, v).getCapacity();
  }

  /**
   * Retrieves the flow for an edge.
   *
   * @param u the u vertex index
   * @param v the v vertex index
   * @return the edge flow
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length
   */
  public int getEdgeFlow(int u, int v) {
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    return getEdge(u, v).getFlow();
  }

  /**
   * Update an existing edge capacity. If the edge doesn't exist, it will throw an
   * exception.
   *
   * @param u        the u vertex index
   * @param v        the v vertex index
   * @param capacity the new edge capacity
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length, or capacity is less
   *                                  than {@code 0}
   * @throws NoSuchElementException   if the edge doesn't exist in the flow
   *                                  network
   */
  public void setEdgeCapacity(int u, int v, int capacity) {
    checkCapacity(capacity);

    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    G[u][v].c = capacity;
  }

  /**
   * Update an existing edge flow. If the edge doesn't exist, it will throw an
   * exception.
   *
   * @param u    the u vertex index
   * @param v    the v vertex index
   * @param flow the new edge flow
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length, or flow is less
   *                                  than {@code 0}
   * @throws NoSuchElementException   if the edge doesn't exist in the flow
   *                                  network
   */
  public void setEdgeFlow(int u, int v, int flow) {
    checkFlow(flow);

    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    G[u][v].f = flow;
  }

  /**
   * Removes a vertex from the flow network along with it's edges.
   *
   * @param v the vertex index to remove
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  the flow network length
   * @throws NoSuchElementException   if the vertex doesn't exist in the flow
   *                                  network
   */
  public void removeVertex(int v) {
    if (!hasVertex(v))
      throw new NoSuchElementException("Vertex " + v + " does not exist.");

    for (int i = 0, len = rows; i < len; i++) {
      if (G[v][i] != null) {
        G[v][i] = null;
        edges--;
      }
    }

    G[v] = null;
    vertices--;
  }

  /**
   * Removes an edge from the flow network. The reversed edge is also removed.
   *
   * @param u the edge u vertex
   * @param v the edge v vertex
   *
   * @throws IllegalArgumentException if either vertex is negative or greater than
   *                                  the flow network length
   * @throws NoSuchElementException   if the edge doesn't exist in the flow
   *                                  network
   */
  public void removeEdge(int u, int v) {
    if (!hasEdge(u, v))
      throw new NoSuchElementException("Edge (" + u + ", " + v + ") does not exist.");
    G[u][v] = null;
    G[v][u] = null;
    edges--;
  }

}
