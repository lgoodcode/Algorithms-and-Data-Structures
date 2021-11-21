package data_structures.graphs.flowNetworks;

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
 */
public class FlowNetworks {

}
