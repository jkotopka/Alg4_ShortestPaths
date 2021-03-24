package org.kotopka;

import java.util.Scanner;

/**
 * {@code CPM} - Critical Path Method for parallel precedence-constrained job scheduling. <br>
 *
 * Adapted from <a href="https://algs4.cs.princeton.edu/home/">Algorithms 4th. ed.</a> p. 665
 */
public class CPM {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String in = scan.nextLine();
        int n = Integer.parseInt(in);   // vertices 0 to n - 1 are the jobs

        EdgeWeightedDigraph G = new EdgeWeightedDigraph(2 * n + 2);

        int s = 2 * n;  // s is the source, arbitrarily AFTER the jobs
        int t = s + 1;  // t is the sink

        for (int i = 0; i < n; i++) {
            String input = scan.nextLine();
            String[] a = input.split("\\s+");

            double duration = Double.parseDouble(a[0]);

            G.addEdge(new DirectedEdge(i, i + n, duration));    // the job itself
            G.addEdge(new DirectedEdge(s, i, 0.0));          // zero-weight edge from source to job start
            G.addEdge(new DirectedEdge(i + n, t, 0.0));   // zero-weight edge from job end to sink

            for (int j = 1; j < a.length; j++) {
                int successor = Integer.parseInt(a[j]);

                G.addEdge(new DirectedEdge(i + n, successor, 0.0));    // zero-weight edge from each job end to
                                                                                // the job it must precede
            }

            if (input.isEmpty()) break;
        }

        AcyclicLongestPaths lp = new AcyclicLongestPaths(G, s);

        for (int i = 0; i < n; i++) {
            System.out.printf("%4d: %5.1f\n", i, lp.distTo(i));
        }
        System.out.printf("Finish time: %5.1f\n", lp.distTo(t));
    }
}
