import javax.swing.*;
public class MainMenu {
    public static void main(String[] args) throws Exception {
        // Create instruction screen
        JOptionPane.showMessageDialog(null,
                "Welcome! This is the Graph Algorithm Visualizer, " +
                        "where you'll be able to input the number of vertices,\n"
                        + "edge probability, and choose an algorithm to " +
                        "see a visual walkthrough on" +
                        "a randomly generated graph!\n"
                        + "Once you close this screen, you'll be able to " +
                        "input those values.\n"
                        + "Try to keep the number of vertices small and edge density low " +
                        "to visually see the walkthrough better!\n"
                        + "Have fun!", "Instructions", JOptionPane.INFORMATION_MESSAGE);

        // Get user input for number of vertices, edge probability, and start vertex
        int numVertices = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Enter the number of vertices (value must be greater than 0, max of 20):"));
        double edgeProbability = Double.parseDouble(JOptionPane.showInputDialog(null,
                "Enter the edge probability (from 0 to 1):"));

        int algoSelection = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Select the algorithm you would like to visualize (DFS: 1, BFS: 2, Dijkstra's: 3, " +
                         "Bellman Ford: 4, Floyd-Warshall: 5):"));

        if (algoSelection == 1) {
            Graph graph = GraphGenerator.generateRandomGraph(numVertices, edgeProbability, -10, 10);
            DFSVisualizer dfsVisualizer = new DFSVisualizer(graph);
            dfsVisualizer.visualizeDFS(graph);
        } else if (algoSelection == 2) {
            Graph graph = GraphGenerator.generateRandomGraph(numVertices, edgeProbability, -10, 10);
            BFSVisualizer bfsVisualizer = new BFSVisualizer(graph);
            bfsVisualizer.visualizeBFS(graph);
        } else if (algoSelection == 3) {
            Graph graph = GraphGenerator.generateRandomGraph(numVertices, edgeProbability, 1, 10);
            DijkstrasVisualizer dijkstrasVisualizer = new DijkstrasVisualizer(graph);
            dijkstrasVisualizer.visualizeDijkstras(graph);
        } else if (algoSelection == 4) {
            Graph graph = GraphGenerator.generateRandomGraph(numVertices, edgeProbability, -5, 10);
            BFVisualizer bfVisualizer = new BFVisualizer(graph);
            bfVisualizer.visualizeBF(graph);
        } else if (algoSelection == 5) {
            Graph graph = GraphGenerator.generateRandomGraph(numVertices, edgeProbability, -5, 10);
            FWVisualizer fwVisualizer = new FWVisualizer(graph);
            fwVisualizer.visualizeFW(graph);
        } else {
            JOptionPane.showMessageDialog(null,
                    "Invalid Selection.",
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
