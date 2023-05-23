import java.util.LinkedList;
import java.util.Random;

public class Graph {
    private int numVertices;
    private LinkedList<Tuple<Integer, Integer>>[] adjList;
    private String[] vertexColors;

    /**
     * Constructs a new Graph with the specified number of vertices.
     * @param numVertices the number of vertices in the graph
     */
    public Graph(int numVertices) {
        this.numVertices = numVertices;
        adjList = new LinkedList[numVertices];
        vertexColors = new String[numVertices];

        // Initialize adjacency list and vertex colors
        for (int i = 0; i < numVertices; i++) {
            adjList[i] = new LinkedList<>();
            vertexColors[i] = "WHITE";
        }
    }

    /**
     * Adds an edge from source vertex to destination vertex in the graph's adjacency list.
     * @param src the source vertex
     * @param dest the destination vertex
     */
    public void addEdge(int src, int dest, int weight) {
        adjList[src].add(new Tuple<>(dest, weight));
    }

    /**
     * Returns the adjacency list of the graph.
     * @return the adjacency list
     */
    public LinkedList<Tuple<Integer, Integer>>[] getAdjList() {
        return adjList;
    }

    /**
     * Returns the array of vertex colors.
     * @return the vertex colors
     */
    public String[] getVertexColors() {
        return vertexColors;
    }

    /**
     * Returns the number of vertices in the graph.
     * @return the number of vertices
     */
    public int getNumVertices() {
        return numVertices;
    }
}
