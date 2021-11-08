# Minimum Spanning Trees (MST)

Assuming we have a connected, undirected graph G = (V, E), with a weight 
function w : E -> R Real Numbers), we can use greedy algorithms to find 
a minimum spanning tree for G.

The first greedy strategy follows the genereic method, which grows the MST 
one edge at a time. The genereic method manages a set of edges A, maintaining 
the following loop invariant:

  Prior to each iteration, A is a subset of some minimum spanning tree.

  At each step, we determine an edge (u, v) that we can add to A without 
  violating the invariant, in the sense that A U {(u,v)} (U is union) is 
  also a subset of a MST.

We call such an edge a "safe edge" for A, since we can add it safely to A 
while maintaining the invariant.

Generic-MST(G, w)
 1   A = 0 (empty set)
 2   while A does not form a spanning tree
 3       find an edge (u,v) that is safe for A
 4       A = A U {(u,v)}
 5   return A

## Definitions:
A "Cut" (S, V-S) of an undirected graph is a partition of V.

An edge (u, v) of E "Crosses" the cut (S, V-S) if one of its endpoints is 
in S and the other is in V-S.

A cut "Respects" a set A of edges if no edge in A crosses the cut.

An edge is a "Light Edge" crossing a cut if its weight is the minimum of any 
edge crossing the cut.

- Note: There can be more than one light edge crossing a cut in the case of ties.
         We say that an edge is a "Light Edge" satisfying a given property if its 
         weight is the minimum of any edge satisfying the property.

### Theorem 23.1 - Rule for recognizing "Safe Edges"

Let G = (V, E) be a connected, undirected graph with a real-valued wieght function w 
defined on E.

Let A be a subset of E that is included in some MST for G, let (S, V-S) be any cut of
G that respects A, and let (u,v) be a light edge crossing (S, V-S).
