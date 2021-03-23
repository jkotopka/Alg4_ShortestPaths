package org.kotopka;

/**
 * {@code Topological} - Detects if a digraph has a topological sort (i.e. is a DAG).
 * If so the method {@code order()} returns an {@code Iterable<Integer>} object of the vertices arranged in topological order.
 */
public class Topological {

    private final boolean[] marked;
    private final Stack<Integer> reversePostOrder;
    private boolean isDAG;

    public Topological(Digraph G) {
        this.marked = new boolean[G.V()];
        this.reversePostOrder = new Stack<>();
        this.isDAG = true;

        for (int v = 0; v < G.V(); v++) {
            if (isDAG && !marked[v]) dfs(G, v, v);
        }
    }

    private void dfs(Digraph G, int s, int v) {
        marked[v] = true;

        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();

            if (s == w) {
                isDAG = false;
                return;
            }

            if (!marked[w]) dfs(G, s, w);
        }

        reversePostOrder.push(v);
    }

    public boolean hasOrder() { return isDAG; }

    public Iterable<Integer> order() { return reversePostOrder; }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: missing commandline argument.");
            System.exit(-1);
        }

        EdgeWeightedDigraph ewd = GraphLoader.load(args[0]);
        Topological t = new Topological(ewd);

        if (t.hasOrder()) {
            for (int i : t.order()) {
                System.out.print(i + " ");
            }
        } else {
            System.out.println("Graph is not topological");
        }
    }
}
