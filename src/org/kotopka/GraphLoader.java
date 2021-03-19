package org.kotopka;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GraphLoader {

    private static EdgeWeightedDigraph ewd = null;

    public static EdgeWeightedDigraph load(String filename) {
        try (Scanner reader = new Scanner(new File(filename))) {
            int vertexCount = reader.nextInt();
            reader.nextInt(); // burn this int, not needed...

            ewd = new EdgeWeightedDigraph(vertexCount);

            while (reader.hasNext()) {
                ewd.addEdge(new DirectedEdge(reader.nextInt(), reader.nextInt(), reader.nextDouble()));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return ewd;
    }

}
