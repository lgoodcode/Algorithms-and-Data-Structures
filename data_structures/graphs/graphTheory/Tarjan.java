package data_structures.graphs.graphTheory;

import data_structures.graphs.Graph;
import data_structures.linkedLists.LinkedList;
import data_structures.stacks.Stack;

/**
 * <h3>Tarjan's SCC {@code O(|V| + |E|)}</h3>
 *
 * <p>
 * The algorithm takes a directed graph as input, and produces a partition of
 * the graph's vertices into the graph's strongly connected components. Each
 * vertex of the graph appears in exactly one of the strongly connected
 * components. Any vertex that is not on a directed cycle forms a strongly
 * connected component all by itself: for example, a vertex whose in-degree or
 * out-degree is {@code 0}, or any vertex of an acyclic graph.
 * </p>
 *
 * <p>
 * The basic idea of this algorithm is a single DFS from an arbitrary start node
 * which constitutes subsequent DFS searches on any nodes that have not yet been
 * visited. As usual with depth-first search, the search visits every node of
 * the graph exactly once, declining to revisit any node that has already been
 * visited. Thus, the collection of search trees is a spanning forest of the
 * graph. The strongly connected components will be recovered as certain
 * subtrees of this forest. The roots of these subtrees are called the
 * <i>roots</i> of the strongly connected components. Any node of a strongly
 * connected component might serve as a root, if it happens to be the first node
 * of a component that is discovered by search.
 * </p>
 *
 *
 * Stack Invariant:
 *
 * Nodes are placed on a stack in the order in which they are visited. When the
 * depth-first search recursively visits a node v and its descendants, those
 * nodes are not all necessarily popped from the stack when this recursive call
 * returns. The crucial invariant property is that a node remains on the stack
 * after it has been visited if and only if there exists a path in the input
 * graph from it to some node earlier on the stack. In other words it means that
 * in the DFS a node would be only removed from the stack after all its
 * connected paths have been traversed. When the DFS will backtrack it would
 * remove the nodes on a single path and return back to the root in order to
 * start a new path.
 *
 * At the end of the call that visits v and its descendants, we know whether v
 * itself has a path to any node earlier on the stack. If so, the call returns,
 * leaving v on the stack to preserve the invariant. If not, then v must be the
 * root of its strongly connected component, which consists of v together with
 * any nodes later on the stack than v (such nodes all have paths back to v but
 * not to any earlier node, because if they had paths to earlier nodes then v
 * would also have paths to earlier nodes which is false). The connected
 * component rooted at v is then popped from the stack and returned, again
 * preserving the invariant.
 *
 *
 * Bookkeeping:
 *
 * Each node v is assigned a unique integer v.index, which numbers the nodes
 * consecutively in the order in which they are discovered. It also maintains a
 * value v.lowlink that represents the smallest index of any node known to be
 * reachable from v through v's DFS subtree, including v itself. Therefore v
 * must be left on the stack if v.lowlink < v.index, whereas v must be removed
 * as the root of a strongly connected component if v.lowlink == v.index. The
 * value v.lowlink is computed during the depth-first search from v, as this
 * finds the nodes that are reachable from v.
 */
public final class Tarjan {
  private static class Node {
    int index;
    int lowLink;
    boolean onStack;

    Node() {
      index = -1;
    }
  }

  // Prevent this algorithm from being instantiated
  public Tarjan() {
    throw new NoClassDefFoundError("Cannot instantiate this class.");
  }

  public static Object[] compute(Graph graph) {
    Node[] VTS = new Node[graph.getRows()];
    Stack<Integer> S = new Stack<>(graph.getRows());
    LinkedList<Object[]> SCC = new LinkedList<>();
    int[] V = graph.getVertices(), index = {0};

    for (int u : V)
      VTS[u] = new Node();

    for (int u : V) {
      if (VTS[u].index == -1)
        T_DFS(graph, VTS, SCC, S, index, u);
    }

    return SCC.toArray();
  }

  private static void T_DFS(Graph G, Node[] VTS, LinkedList<Object[]> SCC,
    Stack<Integer> S, int[] index, int u)
  {
    VTS[u].index = VTS[u].lowLink = index[0]++;
    S.push(u);
    VTS[u].onStack = true;

    // Consider successors of v
    for (int v : G.getAdjacentVertices(u)) {
      if (VTS[v].index == -1) {
        T_DFS(G, VTS, SCC, S, index, v);

        VTS[u].lowLink = Math.min(VTS[u].lowLink, VTS[v].lowLink);
      }
      else if (VTS[v].onStack) {
        // Successor v is in stack S, hence, in the current SCC. If v is not in
        // stack, then (u, v) is an edge pointing to an SCC already found and
        // must be ignored. Set the earliest discovered verted time to be the
        // minimum of u.lowLink or the discovery time of v.
        VTS[u].lowLink = Math.min(VTS[u].lowLink, VTS[v].index);
      }
    }

    // If u is a root node, generate the SCC
    if (VTS[u].lowLink == VTS[u].index) {
      LinkedList<Integer> L = new LinkedList<>();
      int v;

      do {
        v = S.pop();
        L.insert(v);
        VTS[v].onStack = false;
      } while (v != u);

      SCC.insert(L.toArray());
    }
  }
}
