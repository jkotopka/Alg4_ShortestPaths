package org.kotopka;

/**
 * {@code DirectedCycleOrder} - Detects and returns cycles. Also returns preorder, postorder and reverse postorder traversals.
 */
public class DirectedCycleOrder {

    private final boolean[] marked;
    private final int[] edgeTo;
    private final Stack<Integer> cycle;
    private final boolean[] onStack;
    private boolean hasCycle;

    private final Queue<Integer> preOrder;
    private final Queue<Integer> postOrder;
    private final Stack<Integer> reversePostOrder;

    public DirectedCycleOrder(Digraph G) {
        this.marked = new boolean[G.V()];
        this.edgeTo = new int[G.V()];
        this.cycle = new Stack<>();
        this.onStack = new boolean[G.V()];

        this.preOrder = new Queue<>();
        this.postOrder = new Queue<>();
        this.reversePostOrder = new Stack<>();

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
        preOrder.enqueue(v);

        onStack[v] = true;
        marked[v]  = true;

        for (DirectedEdge e : G.adj(v)) {

            int w = e.to();

            if (!marked[w]) {
                edgeTo[w] = v;

                dfs(G, w);
            } else if (onStack[w]) {
                // TODO: find ALL cycles from a given vertex, right now it won't find ALL cycles from a given vertex
                // cycle detected
                hasCycle = true;
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }

                cycle.push(w);
                cycle.push(v);
            }
        }

        onStack[v] = false;

        postOrder.enqueue(v);
        reversePostOrder.push(v);
    }

    public boolean hasCycle() { return hasCycle; }

    public Iterable<Integer> cycle() { return cycle; }

    public Iterable<Integer> preOrder() { return preOrder; }

    public Iterable<Integer> postOrder() { return postOrder; }

    public Iterable<Integer> reversePostOrder() { return reversePostOrder; }

    // the ol' testerino
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: missing commandline argument.");
            System.exit(-1);
        }

        EdgeWeightedDigraph ewd = GraphLoader.load(args[0]);

        DirectedCycleOrder dc = new DirectedCycleOrder(ewd);

        System.out.println("has cycle? " + dc.hasCycle());
        if (dc.hasCycle()) {
            for (int i : dc.cycle()) {
                System.out.print(i + " ");
            }
        }
        System.out.println();

        System.out.println("PreOrder");
        for (int i : dc.preOrder()) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("PostOrder");
        for (int i : dc.postOrder()) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("ReversePostOrder");
        for (int i : dc.reversePostOrder()) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
