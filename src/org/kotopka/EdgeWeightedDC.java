package org.kotopka;

public class EdgeWeightedDC {

    private final boolean[] marked;
    private final DirectedEdge[] edgeTo;
    private final Stack<DirectedEdge> cycle;
    private final boolean[] onStack;

    public EdgeWeightedDC(Digraph G) {
        this.marked = new boolean[G.V()];
        this.edgeTo = new DirectedEdge[G.V()];
        this.cycle = new Stack<>();
        this.onStack = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {

        onStack[v] = true;
        marked[v]  = true;

        for (DirectedEdge e : G.adj(v)) {

            int w = e.to();

            if (!marked[w]) {
                edgeTo[w] = e;

                dfs(G, w);
            } else if (onStack[w]) {
                // TODO: ALL cycles from a given vertex, right now it won't find ALL cycles from a given vertex
                // cycle detected
                DirectedEdge x = e;
                for (; x.from() != w; x = edgeTo[x.from()]) {
                    cycle.push(x);
                }
                cycle.push(x);
            }
        }

        onStack[v] = false;
    }

    public boolean hasCycle() { return cycle != null; }

    public Iterable<DirectedEdge> cycle() { return cycle; }

    public static void main(String[] args) {
        EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(13);

        ewd.addEdge(new DirectedEdge(0, 1 ));
        ewd.addEdge(new DirectedEdge(0, 5 ));
        ewd.addEdge(new DirectedEdge(2, 0 ));
        ewd.addEdge(new DirectedEdge(2, 3 ));
        ewd.addEdge(new DirectedEdge(3, 2 ));
        ewd.addEdge(new DirectedEdge(3, 5 ));
        ewd.addEdge(new DirectedEdge(4, 2 ));
        ewd.addEdge(new DirectedEdge(4, 3 ));
        ewd.addEdge(new DirectedEdge(5, 4 ));
        ewd.addEdge(new DirectedEdge(6, 0 ));
        ewd.addEdge(new DirectedEdge(6, 4 ));
        ewd.addEdge(new DirectedEdge(6, 8 ));
        ewd.addEdge(new DirectedEdge(6, 9 ));
        ewd.addEdge(new DirectedEdge(7, 6 ));
        ewd.addEdge(new DirectedEdge(7, 9 ));
        ewd.addEdge(new DirectedEdge(8, 6 ));
        ewd.addEdge(new DirectedEdge(9, 10));
        ewd.addEdge(new DirectedEdge(9, 11));
        ewd.addEdge(new DirectedEdge(10, 12));
        ewd.addEdge(new DirectedEdge(11, 12));
        ewd.addEdge(new DirectedEdge(12, 9));

        EdgeWeightedDC dc = new EdgeWeightedDC(ewd);

        System.out.println("has cycle? " + dc.hasCycle());

        if (dc.hasCycle()) {
            for (DirectedEdge i : dc.cycle()) {
                System.out.print(i + " ");
            }
        }
    }
}
