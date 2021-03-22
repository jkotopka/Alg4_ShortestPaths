package org.kotopka;

public interface Digraph {
    void addEdge(DirectedEdge e);

    Iterable<DirectedEdge> adj(int v);

    int V();

    int E();

    Iterable<DirectedEdge> edges();
}
