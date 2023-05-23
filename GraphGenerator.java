import java.util.Random;

public class GraphGenerator {
    /**
     * Generates a random graph with a specified number of vertices and edge probability.
     *
     * @param numVertices      the number of vertices in the graph
     * @param edgeProbability  the probability that two vertices are connected by an edge
     * @return                 a randomly generated graph
     * @throws NumberFormatException if numVertices is less than or equal to 0
     */
    public static Graph generateRandomGraph(int numVertices, double edgeProbability, int minWeight, int maxWeight) throws NumberFormatException {

        //Will throw exception after first 2 inputs
        if (numVertices <= 0) {
            throw new NumberFormatException("Invalid Value! Try Again.");
        }

        //initialize graph and random instance
        Graph graph = new Graph(numVertices);
        Random random = new Random();

        // Iterate over all possible edges and add them with probability edgeProbability
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i != j && random.nextDouble() <= edgeProbability) {
                    graph.addEdge(i, j, random.nextInt(minWeight, maxWeight));
                }
            }
        }

        return graph;
    }
}