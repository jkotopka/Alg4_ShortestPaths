package org.kotopka;

/**
 * {@code EdgeWeightedDigraph} - Data type representing an edge-weighted digraph.
 * Adapted from <a href="https://algs4.cs.princeton.edu/home/">Algorithms 4th ed.</a> by Robert Sedgewick and Kevin Wayne
 */
public class EdgeWeightedDigraph {

    private final int V;
    private final Bag<DirectedEdge>[] adj;
    private int E;

    /**
     * {@code EdgeWeightedDigraph} Constructor. Constructs an edge-weighted digraph of V vertices.
     * @param V number of vertices in this digraph
     */
    @SuppressWarnings("unchecked")
    public EdgeWeightedDigraph(int V) {
        if (V <= 0) throw new IllegalArgumentException("Graph must have positive number of vertices");

        this.V = V;
        this.adj = (Bag<DirectedEdge>[]) new Bag[V];

        for (int i = 0; i < V; i++) {
            adj[i] = new Bag<>();
        }
    }

    /**
     * {@code addEdge()} - Adds an edge to this edge-weighted digraph
     * @param e the edge to be added
     */
    public void addEdge(DirectedEdge e) {
        if (e == null) throw new IllegalArgumentException("DirectedEdge argument is null");

        adj[e.from()].add(e);
        this.E++;
    }

    /**
     * adj() - Returns an object of type Iterable of all edges originating from vertex v.
     * @param v The origin vertex incident to the desired edges
     * @return An object of type Iterable. Order should be considered arbitrary.
     */
    public Iterable<DirectedEdge> adj(int v) {
        if (V <= 0) throw new IllegalArgumentException("Invalid vertex");

        return adj[v];
    }

    /**
     * {@code V()} - The number of vertices in this digraph.
     * @return the number of vertices
     */
    public int V() { return V; }

    /**
     * {@code E()} - The number of edges in this digraph
     * @return the number of edges
     */
    public int E() { return E; }

    /**
     * edges() - Returns an object of type Iterable of all edges in the edge-weighted digraph.
     * @return An object of type Iterable containing all of the edges in this digraph. Order should be considered arbitrary.
     */
    public Iterable<DirectedEdge> edges() {
        Bag<DirectedEdge> q = new Bag<>();

        for (Bag<DirectedEdge> b : adj) {
            for (DirectedEdge e : b) {
                if (e != null) q.add(e);
            }
        }

        return q;
    }

    /**
     * <code>toString()</code> - String representation of this graph
     * @return String
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("[");

        for (DirectedEdge e : edges()) {
            sb.append("(").append(e).append("), ");
        }

        if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
        }

        sb.append("]");

        return sb.toString();
    }

}
