// package data_structures.graphs.flowNetworks;

// /**
//  * Relabel-To-Front(G, s, t)
//  * 1   Initialize-Preflow(G, s)
//  * 2   L = G.V - {s, t}, in any order
//  * 3   for each vertex u of G.V - {s, t}
//  * 4       u.current = u.N.head
//  * 5   u = L.head
//  * 6   while u != NIL
//  * 7       old-height = u.h
//  * 8       Discharge(u)
//  * 9       if (u.h > old-height)
//  * 10          move u to the front of list L
//  * 11      u = u.next
//  */

// /**
//  * <h3>Relabel to Front {@code O(V^3)}</h3>
//  *
//  * <p>
//  * This algorithm maintains a list of the vertices in the network. It scans
//  * the list, repeatedly selecting an overflowing vertex {@code u} and then <i>Discharging</i>
//  * it; performs push and relabel operations until {@code u} is no overflowing, or have
//  * positive excess. Whenever a vertex is relabeled it is placed at the front of
//  * the list and the algorithm starts from the beginning again.
//  * </p>
//  *
//  * <p>
//  * The algorithm relies on <i>Admissible Edges</i> where cf(u, v) > 0 and h(u) ==
//  * h(v) + 1, otherwise the edge is "Inadmissible". The "Admissible Network" is
//  * Gfh = (V, Efh), where Efh is the set of admissible edges.
//  * </p>
//  * 
//  * <p>
//  * The vertices u themselves contain a "Neighbor List" that consists of all the
//  * vertices adjacent to u, v. When discharging a vertex u, it will iterate
//  * through it's neighbor list of vertices as long as u is overflowing. If it
//  * doesn't have an admissible edge to send flow through, it will continually
//  * relabel itself until a Push operation applies. This continues until u is no
//  * longer overflowing. If a vertex is relabeled during the Discharge operation,
//  * the vertex in the main list of vertices is placed to the front and the
//  * process restarts until it does a full iteration of the list and no vertices
//  * are overflowing.
//  * </p>
//  */
// public class RelabelToFront {
//   private static int maxFlow(Graph G, int s, int t) {

//   }
// }
