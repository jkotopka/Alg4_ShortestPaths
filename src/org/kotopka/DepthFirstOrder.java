package org.kotopka;

public class DepthFirstOrder {

    private final boolean[] marked;
    private final Queue<Integer> preOrder;
    private final Queue<Integer> postOrder;
    private final Stack<Integer> reversePostOrder;

    public DepthFirstOrder(Digraph G) {
        this.preOrder = new Queue<>();
        this.postOrder = new Queue<>();
        this.reversePostOrder = new Stack<>();
        this.marked = new boolean[G.V()];

        for (int v = 0; v < G.V(); v++) {
            if (!marked[v]) dfs(G, v);
        }
    }

    private void dfs(Digraph G, int v) {
        preOrder.enqueue(v);

        marked[v] = true;

        for (DirectedEdge e : G.adj(v)) {
            int w = e.to();

            if (!marked[w]) dfs(G, w);
        }

        postOrder.enqueue(v);
        reversePostOrder.push(v);
    }

    public Iterable<Integer> preOrder() { return preOrder; }

    public Iterable<Integer> postOrder() { return postOrder; }

    public Iterable<Integer> reversePostOrder() { return reversePostOrder; }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Error: missing commandline argument.");
            System.exit(-1);
        }

        EdgeWeightedDigraph ewd = GraphLoader.load(args[0]);

        DepthFirstOrder dfo = new DepthFirstOrder(ewd);


        System.out.println("preorder:");
        for (int i : dfo.preOrder()) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("postorder:");
        for (int i : dfo.postOrder()) {
            System.out.print(i + " ");
        }
        System.out.println();

        System.out.println("reverse postorder:");
        for (int i : dfo.reversePostOrder()) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
