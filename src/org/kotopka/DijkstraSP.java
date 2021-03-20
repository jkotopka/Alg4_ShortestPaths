package org.kotopka;

import java.util.Arrays;

/**
 * {@code DijkstraSP} - Calculate a shortest-paths tree using Dijkstra's algorithm
 */
public class DijkstraSP {

    private final double[] distTo;
    private final DirectedEdge[] edgeTo;
    private final IndexMinPQ<Double> edgePQ;    // v is index, weight is key

    public DijkstraSP(EdgeWeightedDigraph G, int s) {
        int V = G.V();
        this.distTo = new double[V];
        this.edgeTo = new DirectedEdge[V];
        this.edgePQ = new IndexMinPQ<>(V);

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);

        distTo[s] = 0.0;
        edgeTo[s] = null;

        edgePQ.insert(0, 0.0);

        while (!edgePQ.isEmpty()) {
            relax(G, edgePQ.delMin());
        }

    }

    private void relax(EdgeWeightedDigraph G, int v) {

        // for all edges incident to vertex v
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();
            double pathWeight = distTo[v] + e.weight();

            if (distTo[w] > pathWeight) {
                distTo[w] = pathWeight;
                edgeTo[w] = e;

                if (edgePQ.contains(w)) {
                    edgePQ.changeKey(w, e.weight());
                } else {
                    edgePQ.insert(w, e.weight());
                }
            }
        }
    }

    public double distTo(int v) { return distTo[v]; }

    public Iterable<DirectedEdge> pathTo(int v) {
        if (! hasPathTo(v)) return null;

        Stack<DirectedEdge> path = new Stack<>();

        for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
            path.push(e);
        }

        return path;
    }

    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    // TODO: test client p 645
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: missing commandline argument.");
            System.exit(-1);
        }

        EdgeWeightedDigraph ewd = GraphLoader.load(args[0]);
        DijkstraSP sp = new DijkstraSP(ewd, 0);
        int destination = 6;

        if (sp.hasPathTo(destination)) {
            for (DirectedEdge e : sp.pathTo(destination)) {
                System.out.println(e);
            }
        } else {
            System.out.println("No path to " + destination);
        }
    }

}
