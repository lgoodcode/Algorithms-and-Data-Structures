package data_structures.graphs;

import static java.util.Arrays.copyOf;

import java.util.NoSuchElementException;

public class FlowNetwork {
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
     * Sets the flow of the edge/
     * 
     * @param flow the new edge flow
     * 
     * @throws IllegalArgumentException if the flow is less than {@code 0}
     */
    public void setFlow(int flow) {
      checkFlow(flow);
      this.f = flow;
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
   * Constructs a shallow copy of the specified {@link FlowNetwork}.
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

    G = new Edge[rows][];

    for (int u : network.getVertices())
      G[u] = copyOf(network.G[u], rows);
  }

  /**
   * Constructs a copy and extends the length of the specified
   * {@link FlowNetwork}.
   *
   * @param network the flow network to copy
   * @param rows    the new flow network length
   *
   * @throws NullPointerException     if the flow network is {@code null}
   * @throws IllegalArgumentException if the specified new length is less than the
   *                                  original flow network
   */
  public FlowNetwork(FlowNetwork network, int rows) {
    if (network == null)
      throw new NullPointerException("FlowNetwork cannot be null.");
    if (rows < network.rows)
      throw new IllegalArgumentException("New network length can't be less than the original.");

    this.rows = rows;
    vertices = network.vertices;
    edges = network.edges;

    G = new Edge[rows][];

    for (int u : network.getVertices())
      G[u] = copyOf(network.G[u], rows);
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
  public static void checkCapacity(int capacity){
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
  public static void checkFlow(int flow) {
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
   * Returns the matrix adjacency representation.
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
   * Returns an array of {@link FlowNetwork.Edge} of all the edges in the flow
   * network.
   *
   * @return array of {@code Edges}
   */
  public Edge[] getEdges() {
    if (edges == 0)
      return new Edge[0];

    int j, k = 0, len = rows;
    Edge[] E = new Edge[edges];

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
   * @param vertex the vertex whose edges is being retrieved
   * @return the {@code Edge} array
   *
   * @throws IllegalArgumentException if the vertex is negative or greater than
   *                                  flow network length or doesn't exist in the
   *                                  flow network
   */
  public Edge[] getEdges(int vertex) {
    if (!hasVertex(vertex))
      throw new IllegalArgumentException("Vertex " + vertex + " does not exist in flow network.");

    Edge[] E = new Edge[vertices];
    int i = 0, u = vertex, v, len = rows;

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
   * Adds an edge to the flow network with the given x and y vertices as well as
   * the edge capacity and flow. If the specified vertices don't exist in the flow
   * network, they will be added.
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
    G[u][v].c = flow;
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
   * Removes an edge from the flow network.
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
    edges--;
  }

}
