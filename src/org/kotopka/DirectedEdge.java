package org.kotopka;

/**
 * {@code DirectedEdge} - Used to represent a directed edge in a weighted digraph
 */
public class DirectedEdge {

    private final int v;
    private final int w;
    private final double weight;

    /**
     * {@code DirectedEdge} data type constructor
     * @param v the origin vertex of this edge
     * @param w the destination vertex of this edge
     * @param weight the weight of this edge
     */
    public DirectedEdge(int v, int w, double weight) {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /**
     * {@code DirectedEdge} Constructor for "unweighted" digraph edge, sets {@code weight} to {@code 0.0}
     * @param v the origin vertex of this edge
     * @param w the destination vertex of this edge
     */
    public DirectedEdge(int v, int w) {
        this(v, w, 0.0);
    }

    /**
     * {@code from()} - returns the origin vertex of this edge
     * @return origin vertex
     */
    public int from() { return v; }

    /**
     * {@code to()} - returns the destination vertex of this edge
     * @return destination vertex
     */
    public int to() {  return w; }

    /**
     * {@code weight()} - returns the weight of this edge
     * @return weight of this edge
     */
    public double weight() { return weight; }

    @Override
    public String toString() {
        return "(" + v + "->" + w + ") " + weight;
    }
}
