import javax.swing.*;
        import java.awt.*;
        import java.util.LinkedList;
public class FWVisualizer extends JPanel {
    private Graph graph;
    private String[] vertexColors;
    int[][] distances;

    /**
     * FWVisualizer is a class that is used to perform depth-first search (DFS) traversal of a graph,
     * visualize the traversal process, and record the discovery time and finish time of each vertex.
     *
     * @param graph The graph object that is being traversed.
     */
    public FWVisualizer(Graph graph) {
        this.graph = graph;
        vertexColors = new String[graph.getNumVertices()];
        distances = new int[graph.getNumVertices()][graph.getNumVertices()];
        resetGraph();
    }

    /**
     * resetGraph method is used to reset the color, parent, discovery time, and finish time of all vertices
     * to their initial values, which is white, -1, 0, and 0 respectively.
     */
    public void resetGraph() {
        for (int i = 0; i < vertexColors.length; i++) {
            vertexColors[i] = "WHITE";
        }
    }

    /**
     * traverse method is used to start DFS traversal from the given startVertex.
     *
     * @throws IllegalArgumentException if startVertex is not within the valid range of vertices.
     */
    public void traverse() {
        resetGraph();
        int numVertices = graph.getNumVertices();
        for (int i = 0; i < numVertices; i++) {
            for (int j = 0; j < numVertices; j++) {
                if (i == j) {
                    distances[i][j] = 0;
                } else {
                    distances[i][j] = Integer.MAX_VALUE;
                }
            }
        }
        LinkedList<Tuple<Integer, Integer>>[] adjList = graph.getAdjList();
        for (int i = 0; i < numVertices; i++) {
            LinkedList<Tuple<Integer, Integer>> neighbors = adjList[i];
            for (Tuple<Integer, Integer> neighbor : neighbors) {
                int destination = neighbor.getFirst();
                String oldColor1 = vertexColors[i];
                String oldColor2 = vertexColors[destination];
                vertexColors[i] = "BLACK";
                vertexColors[destination] = "BLACK";
                repaint();
                try {
                    Thread.sleep(500); // delay for visualization
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int weight = neighbor.getSecond();
                distances[i][destination] = weight;
                vertexColors[i] = "GRAY";
                vertexColors[destination] = "GRAY";
                repaint();
                try {
                    Thread.sleep(500); // delay for visualization
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                vertexColors[i] = oldColor1;
                vertexColors[destination] = oldColor2;
            }
        }
        for (int k = 0; k < numVertices; k++) {
            for (int i = 0; i < numVertices; i++) {
                for (int j = 0; j < numVertices; j++) {
                    String oldColor1 = vertexColors[i];
                    String oldColor2 = vertexColors[j];
                    vertexColors[i] = "BLACK";
                    vertexColors[j] = "BLACK";
                    repaint();
                    try {
                        Thread.sleep(500); // delay for visualization
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (distances[i][k] != Integer.MAX_VALUE && distances[k][j] != Integer.MAX_VALUE &&
                            distances[i][k] + distances[k][j] < distances[i][j]) {
                        distances[i][j] = distances[i][k] + distances[k][j];
                        vertexColors[i] = "GRAY";
                        vertexColors[j] = "GRAY";
                        repaint();
                        try {
                            Thread.sleep(500); // delay for visualization
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (distances[i][j] < -1000) {
                            JOptionPane.showMessageDialog(null,
                                    "Negative weight cycle detected. " +
                                            "Please try adjusting your parameters to make this less probable.",
                                    "Result", JOptionPane.INFORMATION_MESSAGE);
                            return;
                        }
                    }
                    vertexColors[i] = oldColor1;
                    vertexColors[j] = oldColor2;
                }
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
        int graphHeight = numCols * edgeLength + 2 * vertexRadius;

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
                drawArrow(g2d, x1, y1, x2, y2, neighbor.getSecond());
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
        int colWidth = rowHeight;
        int tableWidth = rowHeight * colWidth;
        int tableHeight = (numVertices + 2) * rowHeight;

        // Draw table content
        for (int i = 0; i <= numVertices; i++) {
            int y = 130+ rowHeight + i * rowHeight;
            for (int j = 0; j <= numVertices; j++) {
                int x = getWidth() - tableWidth + 125 + j * rowHeight;
                // Draw cell outlines
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x, y, rowHeight, rowHeight);

                // Draw cell content
                if (i > 0 && j > 0 && distances[i-1][j-1] < Integer.MAX_VALUE) {
                    g2d.drawString(Integer.toString(distances[i-1][j-1]),
                            x + colWidth/2 - fontMetrics.stringWidth(Integer.toString(distances[i-1][j-1]))/2,
                            y + rowHeight/2 + fontMetrics.getHeight()/2 - 3);
                }
            }
            if (i > 0) {
                g2d.setColor(Color.BLACK);
                g2d.drawString(Integer.toString(i-1),
                        getWidth() - tableWidth + 125 + colWidth/2 - fontMetrics.stringWidth(Integer.toString(i-1))/2,
                        y + rowHeight/2 + fontMetrics.getHeight()/2 - 3);
                g2d.drawString(Integer.toString(i-1),
                        getWidth() - tableWidth + 125 + colWidth/2 - fontMetrics.stringWidth(Integer.toString(i-1))/2 + i * rowHeight,
                        130+ rowHeight + rowHeight/2 + fontMetrics.getHeight()/2 - 3);
            }
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
    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2, int weight) {
        g.setColor(Color.BLACK);
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
        g.setColor(Color.RED);
        g.drawString(Integer.toString(weight), midX, midY);
        g.setColor(Color.BLACK);
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
    public static void visualizeFW(Graph graph) throws Exception {
        // Create instruction screen
        FWVisualizer visualizer = new FWVisualizer(graph);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(visualizer);

        JPanel buttonPanel = new JPanel();
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

        JOptionPane.showInputDialog(null,
                "Press enter to begin");
        visualizer.traverse();
        visualizer.repaint();
    }
}