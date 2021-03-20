package org.kotopka;

import java.util.Arrays;

public class SP {

    private final EdgeWeightedDigraph G;
    private final int s;
    private final double[] distTo;
    private final DirectedEdge[] edgeTo;

    public SP(EdgeWeightedDigraph G, int s) {
        int V = G.V();
        this.G = G;
        this.s = s;
        this.distTo = new double[V];
        this.edgeTo = new DirectedEdge[V];

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);

        distTo[s] = 0.0;
        edgeTo[s] = null;

        Queue<Integer> edgeQueue = new Queue<>();
        edgeQueue.enqueue(s);

        while (!edgeQueue.isEmpty()) {
            findPaths(edgeQueue, edgeQueue.dequeue());
        }
    }

    private void findPaths(Queue<Integer> edgeQueue, int v) {
        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();

            // enqueue unseen vertices
            if (Double.compare(distTo[w], Double.POSITIVE_INFINITY) == 0) {
                edgeQueue.enqueue(w);
            }

            // relax the current edge
            relax(e);
        }
    }

    private void relax(DirectedEdge e) {
        int v = e.from();
        int w = e.to();
        double pathWeight = distTo[v] + e.weight();

        if (distTo[w] > pathWeight) {

            // edge is eligible
            distTo[w] = pathWeight;
            edgeTo[w] = e;
        }
        // else, edge is ineligible
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
        SP sp = new SP(ewd, 8);
        int destination = 1
                ;

        if (sp.hasPathTo(destination)) {
            for (DirectedEdge e : sp.pathTo(destination)) {
                System.out.println(e);
            }
        } else {
            System.out.println("No path to " + destination);
        }
    }

}
