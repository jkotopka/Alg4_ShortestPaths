package org.kotopka;

import java.util.Arrays;

/**
 * {@code AcyclicSP} - Finds shortest-paths-tree in acyclic edge-weighted digraphs based on traversing the
 * nodes in topological order. Note: the graph MUST be a DAG.
 */
public class AcyclicSP {

    private final int size;
    private final double[] distTo;
    private final DirectedEdge[] edgeTo;

    /**
     * {@code AcyclicSP} - Constructor. Creates a shortest-paths-tree from an acyclic digraph from {@code source}.
     * @param G The digraph
     * @param source source vertex of the SPT
     * @throws IllegalArgumentException if the graph is not a DAG
     */
    public AcyclicSP(Digraph G, int source) {
        if (G == null) throw new IllegalArgumentException("Graph cannot be null");

        size = G.V();

        validateVertex(source);

        Topological topological = new Topological(G);

        if (!topological.hasOrder()) throw new IllegalArgumentException("Graph must be a DAG");

        this.distTo = new double[size];
        this.edgeTo = new DirectedEdge[size];

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);

        distTo[source] = 0.0;
        edgeTo[source] = null;

        for (int v : topological.order()) {
            relax(G, v);
        }
    }

    private void relax(Digraph G, int v) {

        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            double pathWeight = distTo[v] + e.weight();

            if (distTo[w] > pathWeight) {
                distTo[w] = pathWeight;
                edgeTo[w] = e;
            }
        }
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= size) throw new IllegalArgumentException("Invalid vertex " + vertex);
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);

        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public double distTo(int v) {
        validateVertex(v);

        return distTo[v];
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);

        Stack<DirectedEdge> path = new Stack<>();

        for (int x = v; edgeTo[x] != null; x = edgeTo[x].from()) {
            path.push(edgeTo[x]);
        }

        return path;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: missing commandline argument.");
            System.exit(-1);
        }

        EdgeWeightedDigraph ewd = GraphLoader.load(args[0]);

        AcyclicSP sp = new AcyclicSP(ewd, 5);
        int destination = 0;

        if (sp.hasPathTo(destination)) {
            System.out.println("distTo " + destination + " " + sp.distTo(destination));
            for (DirectedEdge e : sp.pathTo(destination)) {
                System.out.println(e);
            }
        } else {
            System.out.println("No path to " + destination);
        }
    }

}
