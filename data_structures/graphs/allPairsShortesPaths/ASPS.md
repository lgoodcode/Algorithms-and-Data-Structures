# All-Pairs Shortest Paths (ASPSs)
 

The All-Pairs Shortest-Paths problem invloves finding the shortest path from u to v for every pair
of vertices u,v of V. We can solve the problem by running a single-source shortest-paths (SSSPs)
algorithm |V| times, once for each vertex as the source. If all weights are nonnegative, we can 
use Dijkstra's algorithm.
 

If a graph as negative-weight edges, we cannot use Dijkstra's algorithm. Instead we must run the
slower Bellman-Ford algorithm once from each vertex. The resulting running time is O(V^2 E),
which on a dense graph (|E| is close to |V|^2) is O(V^4). 


There are algorithms that can perform better. These algorithms also use adjancency-matrix
representation, except for Johnson's algorithm for sparse graphs.
 

For representation, the input is an n x n matrix W representing the edge weights of an n-vertex
directed graph G. W = (w ij), where 
 
          { 0                                   if i = j
  w ij =  { the weight of directed edge (i, j)  if i != j and (i, j) is a member of E
          { Infinity                            if i != j and (i, j) is not a member of E

We allow negative-weight edges but assume for the time being that the graph contains 
no negative-weight cycles.
 

The output will be in tabular form of all-pairs shortest-paths algorithms, presented in an
n x n matrix D = (d ij), where d ij contains the weight of a shortest path from vertex i 
to vertex j. d ij = S(i, j).
 
 
To solve the ASPSs, we need to compute the shortest-path weight AND a predecessor matrix
P = (p ij), where p ij is NIL if either i = j or there is no path from i to j, and 
otherwise p ij is the predecessor of j on some shortest path from i to j. Just as
the predecessor subgraph G.
