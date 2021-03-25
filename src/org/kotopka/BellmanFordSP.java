package org.kotopka;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class BellmanFordSP {

    private final int size;
    private final double[] distTo;
    private final DirectedEdge[] edgeTo;
    private final boolean[] onQueue;
    private final Queue<Integer> queue;
    private int cost;
    private Iterable<DirectedEdge> cycle;

    public BellmanFordSP(Digraph G, int source) {
        this.size = G.V();
        this.distTo = new double[size];
        this.edgeTo = new DirectedEdge[size];
        this.onQueue = new boolean[size];
        this.queue = new Queue<>();

        Arrays.fill(distTo, Double.POSITIVE_INFINITY);

        distTo[source] = 0.0;
        queue.enqueue(source);
        onQueue[source] = true;

        while (!queue.isEmpty() && !hasNegativeCycle()) {
            int v = queue.dequeue();
            onQueue[v] = false;
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

                if (!onQueue[w]) {
                    queue.enqueue(w);
                    onQueue[w] = true;
                }
            }
            if (++cost % G.V() == 0) {
                findNegativeCycle();
                if (hasNegativeCycle()) return;
            }
        }
    }

    private void findNegativeCycle() {
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(edgeTo.length);

        for (int v = 0; v < size; v++) {
            if (edgeTo[v] != null) {
                ewd.addEdge(edgeTo[v]);
            }
        }

        EdgeWeightedDC dc = new EdgeWeightedDC(ewd);
        cycle = dc.cycle();
    }

    private void validateVertex(int vertex) {
        if (vertex < 0 || vertex >= size) throw new IllegalArgumentException("Invalid vertex " + vertex);
    }

    public double distTo(int v) {
        validateVertex(v);

        return distTo[v];
    }

    public boolean hasPathTo(int v) {
        validateVertex(v);

        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    public Iterable<DirectedEdge> pathTo(int v) {
        validateVertex(v);
        if (hasNegativeCycle()) throw new UnsupportedOperationException("Negative cycle exists");

        Stack<DirectedEdge> path = new Stack<>();

        for (int x = v; edgeTo[x] != null; x = edgeTo[x].from()) {
            path.push(edgeTo[x]);
        }

        return path;
    }

    public boolean hasNegativeCycle() {
        return cycle != null;
    }

    public Iterable<DirectedEdge> negativeCycle() {
        if (!hasNegativeCycle()) throw new NoSuchElementException("No negative cycle found");

        return cycle;
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: missing commandline argument.");
            System.exit(-1);
        }

        EdgeWeightedDigraph ewd = GraphLoader.load(args[0]);

        BellmanFordSP sp = new BellmanFordSP(ewd, 6);
        int destination = 3;

        if (!sp.hasNegativeCycle() && sp.hasPathTo(destination)) {
            for (DirectedEdge e : sp.pathTo(destination)) {
                System.out.println(e);
            }
        } else {
            System.out.println("No path to " + destination);
        }

        if (sp.hasNegativeCycle()) {
            System.out.println("Negative cycle:");
            for (DirectedEdge e : sp.negativeCycle()) {
                System.out.println(e);
            }
        }
    }
}
