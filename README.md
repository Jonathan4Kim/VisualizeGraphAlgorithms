# VisualizeGraphAlgorithms
A Java project that I used to help me study various graph traversal algorithms.  Includes DFS, BFS, Bellman Ford's, and more!
Description: One of the best ways of understanding graph algorithms after learning about them is through a visual walkthrough. Here, a visually implementation a variety of graph traversal algorithms were created that allow for user interactivity as well.  the project showcased DFS, BFS, and Dijkstra’s using java to help users have a visual and therefore better understanding of how these graph algorithms work.


* Java Files
   * MainMenu.java: The Java Swing GUI used to select and visualize the graph algorithms. Graphs are generated based on the user inputs and algorithm requirements (i.e. yes or no negative edge weights)
   * BFVisualizer.java: Executes the Bellman Ford algorithm based on the source vertex from the user and provides a visualization of how the algorithm relaxes edges while showing an updated distances table.
   * FWVisualizer.java: Executes the Floyd Warshall algorithm for all pairs shortest paths and provides a visualization of how the algorithm uses dynamic programming while showing an updated distances matrix.
   * Tuple.java: Type generic tuple class used to represent edges in our adjacency lists. The first entry we use for the neighbor and the second entry we use for the edge weight. 
   * Graph.java:  The Graph class.  Utilized by Graph Generator and is an adjacency list representation of a directed graph.
   * GraphGenerator.java: using the graph class and user inputted edge probability and number of vertices, created a graph of appropriate size and randomly generated directed edges in adjacency lists for each vertex.
   * DFSVisualizer.java:  Does the DFS Traversal of the Graph and also handles the GUI using Java Swing.
   * BFSVisualizer.java: The BFS class. This class implements one of the Graph algorithms that we are interested in visualizing. It takes a graph and a source node as inputs and outputs the parent array, AKA the BFS tree.
   * DijkstraVisualizer.java: This file implements Dijkstra's algorithm, as learned in class, for finding single source shortest paths in a graph. It takes a Graph object and source as input and returns the single shortest path tree, similar to BFS.  This allows Dijkstra’s to be visualized using the MainMenu GUI.  To avoid negative edge weight cycles, some parameters were hard-coded into the file in the case of negative edge weights.
