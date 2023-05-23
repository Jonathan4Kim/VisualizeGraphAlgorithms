import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import javax.swing.*;
import java.util.Queue;

public class BFSVisualizer extends JPanel {
    private Graph graph;
    private String[] vertexColors;
    private int[] discoveryTime;
    private int time;

    /**
     * BFSVisualizer is a class that is used to perform depth-first search (DFS) traversal of a graph,
     * visualize the traversal process, and record the discovery time and finish time of each vertex.
     *
     * @param graph The graph object that is being traversed.
     */
    public BFSVisualizer(Graph graph) {
        this.graph = graph;
        vertexColors = new String[graph.getNumVertices()];
        discoveryTime = new int[graph.getNumVertices()];
        time = 0;
        resetGraph();
    }

    /**
     * resetGraph method is used to reset the color, parent, discovery time, and finish time of all vertices
     * to their initial values, which is white, -1, 0, and 0 respectively.
     */
    public void resetGraph() {
        for (int i = 0; i < vertexColors.length; i++) {
            vertexColors[i] = "WHITE";
            discoveryTime[i] = 0;
        }
        time = 0;
    }

    /**
     * traverse method is used to start DFS traversal from the given startVertex.
     *
     * @param startVertex The vertex from where the DFS traversal is started.
     * @throws IllegalArgumentException if startVertex is not within the valid range of vertices.
     */
    public void traverse(int startVertex) {
        resetGraph();
        if (startVertex < 0 || startVertex >= graph.getNumVertices()) {
            throw new IllegalArgumentException("Invalid start vertex: " + startVertex);
        }

        boolean[] visited = new boolean[graph.getNumVertices()];
        LinkedList<Tuple<Integer, Integer>>[] adjList = graph.getAdjList();
        Queue<Integer> queue = new LinkedList<Integer>();
        HashSet<Integer> undiscovered = new HashSet<Integer>();
        for (int i = 0 ; i < graph.getNumVertices(); i++) {
            undiscovered.add(i);
        }
        queue.add(startVertex);
        visited[startVertex] = true;
        discoveryTime[startVertex] = time;
        time++;
        undiscovered.remove(startVertex);

        while (!queue.isEmpty()) {
            int frontier = queue.poll();
            vertexColors[frontier] = "BLACK";
            repaint();

            try {
                Thread.sleep(1000); // delay for visualization
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            for (Tuple<Integer, Integer> neighbor : adjList[frontier]) {
                int dest = neighbor.getFirst();
                if (!visited[dest]) {
                    discoveryTime[dest] = time;
                    time++;
                    vertexColors[dest] = "GRAY";
                    repaint();
                    try {
                        Thread.sleep(1000); // delay for visualization
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    queue.add(dest);
                    undiscovered.remove(dest);
                    visited[dest] = true;
                }
            }
            if (queue.isEmpty() && !undiscovered.isEmpty()) {
                int next = undiscovered.iterator().next();
                discoveryTime[next] = time;
                time++;
                vertexColors[next] = "GRAY";
                repaint();
                try {
                    Thread.sleep(1000); // delay for visualization
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                queue.add(next);
                undiscovered.remove(next);
            }
        }

    }

    /**
     * Draws on the JPanel using the specified Graphics object.
     * Essentially helps draw the graph.
     *
     * @param g the Graphics object to draw the arrow on
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int vertexRadius = 20;
        int edgeLength = 120;
        int numVertices = graph.getNumVertices();
        int numRows = (int) Math.ceil(Math.sqrt(numVertices)); // number of rows in grid
        int numCols = (int) Math.ceil((double) numVertices / numRows); // number of columns in grid
        int graphWidth = numCols * edgeLength + 2 * vertexRadius;
        int graphHeight = numRows * edgeLength + 2 * vertexRadius;

        // Calculate the x and y coordinates of the top-left corner of the graph
        int xOffset = (getWidth() - graphWidth) / 2;
        int yOffset = (getHeight() - graphHeight) / 2;

        for (int i = 0; i < graph.getNumVertices(); i++) {
            LinkedList<Tuple<Integer, Integer>>[] adjList = graph.getAdjList();
            for (Tuple<Integer, Integer> neighbor : adjList[i]) {
                int x1 = (i % numCols) * edgeLength + xOffset + vertexRadius;
                int y1 = (i / numCols) * edgeLength + yOffset + vertexRadius;
                int x2 = (neighbor.getFirst() % numCols) * edgeLength + xOffset + vertexRadius;
                int y2 = (neighbor.getFirst() / numCols) * edgeLength + yOffset + vertexRadius;

                // Set edge color and thickness based on weight
                int weight = 5;
                float thickness = (float) Math.log(weight);
                g2d.setStroke(new BasicStroke(thickness));
                g2d.setColor(Color.BLACK); // semi-transparent black

                // Draw the arrow
                drawArrow(g2d, x1, y1, x2, y2);
            }
        }

        drawTable(g2d, graph.getNumVertices());

        for (int i = 0; i < graph.getNumVertices(); i++) {
            if (vertexColors[i].equals("WHITE")) {
                g2d.setColor(Color.WHITE);
            } else if (vertexColors[i].equals("GRAY")) {
                g2d.setColor(Color.GRAY);
            } else if (vertexColors[i].equals("BLACK")) {
                g2d.setColor(Color.BLACK);
            }

            int x = (i % numCols) * edgeLength + xOffset;
            int y = (i / numCols) * edgeLength + yOffset;

            // Highlight the vertex if it was discovered during the traversal
            if (!vertexColors[i].equals("WHITE")) {
                g2d.fillOval(x, y, vertexRadius * 2, vertexRadius * 2);
                g2d.setColor(Color.WHITE);
                g2d.drawString(Integer.toString(i), x + vertexRadius, y + vertexRadius + 5);
            } else {
                g2d.drawOval(x, y, vertexRadius * 2, vertexRadius * 2);
                g2d.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(i), x + vertexRadius, y + vertexRadius + 5);
            }
        }
    }


    private void drawTable(Graphics g2d, int numVertices) {
        g2d.setColor(Color.WHITE);
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int rowHeight = fontMetrics.getHeight() + 10;
        int colWidth = 70;
        int tableWidth = colWidth * 4;
        int tableHeight = (numVertices + 2) * rowHeight;

        // Draw table headers
        g2d.setColor(Color.BLACK);
        g2d.drawRect(getWidth() - tableWidth - 50, 130, colWidth, rowHeight);
        g2d.drawRect(getWidth() - tableWidth - 50 + colWidth, 130, colWidth, rowHeight);

        // Draw header titles
        g2d.drawString("Vertex",
                getWidth() - tableWidth - 50 + colWidth/2 - fontMetrics.stringWidth("Vertex")/2,
                130 + rowHeight/2 + fontMetrics.getHeight()/2 - 3);
        g2d.drawString("Discovered",
                getWidth() - tableWidth - 50 + colWidth + colWidth/2 - fontMetrics.stringWidth("Discovered")/2,
                130 + rowHeight/2 + fontMetrics.getHeight()/2 - 3);

        // Draw table content
        for (int i = 0; i < numVertices; i++) {
            int x = getWidth() - tableWidth - 50;
            int y = 130+ rowHeight + i * rowHeight;

            // Draw cell outlines
            g2d.setColor(Color.BLACK);
            g2d.drawRect(x, y, colWidth, rowHeight);
            g2d.drawRect(x + colWidth, y, colWidth, rowHeight);

            // Draw cell content
            g2d.setColor(Color.BLACK);
            g2d.drawString(Integer.toString(i), x + colWidth/2 - fontMetrics.stringWidth(Integer.toString(i))/2,
                    y + rowHeight/2 + fontMetrics.getHeight()/2 - 3);
            g2d.drawString(Integer.toString(discoveryTime[i]),
                    x + colWidth + colWidth/2 - fontMetrics.stringWidth(Integer.toString(discoveryTime[i]))/2,
                    y + rowHeight/2 + fontMetrics.getHeight()/2 - 3);
        }
    }

    /**
     * Draws an arrow between two points (x1,y1) and (x2,y2) using the specified Graphics object.
     *
     * @param g the Graphics object to draw the arrow on
     * @param x1 the x-coordinate of the first point
     * @param y1 the y-coordinate of the first point
     * @param x2 the x-coordinate of the second point
     * @param y2 the y-coordinate of the second point
     */
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        double theta = Math.atan2(dy, dx);
        int x, y, rho = 20;
        Polygon arrowHead = new Polygon();

        // Calculate midpoint of the line
        int midX = (x1 + x2) / 2;
        int midY = (y1 + y2) / 2;

        // Calculate points of arrow polygon centered at midpoint
        arrowHead.addPoint(midX, midY);
        x = (int) (midX - rho * Math.cos(theta - Math.PI / 6));
        y = (int) (midY - rho * Math.sin(theta - Math.PI / 6));
        arrowHead.addPoint(x, y);
        x = (int) (midX - rho * Math.cos(theta + Math.PI / 6));
        y = (int) (midY - rho * Math.sin(theta + Math.PI / 6));
        arrowHead.addPoint(x, y);

        // Draw the arrow
        g.drawPolyline(new int[]{x1, x2}, new int[]{y1, y2}, 2);
        g.fillPolygon(arrowHead);
    }

    /**
     * Returns the preferred size of the visualization based on the number of vertices in the graph.
     *
     * @return a Dimension object representing the preferred size of the visualization
     */
    @Override
    public Dimension getPreferredSize() {
        int radius = 20;
        int width = graph.getNumVertices() * radius * 2;
        int height = radius * 2;
        return new Dimension(width, height);
    }


    public static void visualizeBFS(Graph graph) throws Exception {
        // Create instruction screen
        BFSVisualizer visualizer = new BFSVisualizer(graph);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(visualizer);

        // Add a "Reset" button to allow the user to pick a new starting vertex
//        JButton resetButton = new JButton("New Starting Vertex");
//        resetButton.addActionListener(e -> {
//            int newStartIndex = Integer.parseInt(JOptionPane.showInputDialog(null,
//                    "Enter the new start vertex (0 to " + (graph.getNumVertices() - 1) + "):"));
//            visualizer.traverse(newStartIndex);
//            visualizer.repaint();
//        });

        JPanel buttonPanel = new JPanel();
//        buttonPanel.add(resetButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(1000, 1000); // Set the size to 800x600 pixels
        frame.pack();
        frame.setVisible(true);

        // Get the dimensions of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) screenSize.getWidth();
        int height = (int) screenSize.getHeight();

        // Set the size of the frame to fit the screen
        frame.setSize(width, height);

        int startIndex = Integer.parseInt(JOptionPane.showInputDialog(null,
                "Enter the start vertex (0 to " + (graph.getNumVertices() - 1) + "):"));
        visualizer.traverse(startIndex);
        visualizer.repaint();
    }
}