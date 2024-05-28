package Graph;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {
    private Graph graph;

    @BeforeEach
    public void setUp() {
        graph = new Graph();  // Set default parameters for evaporation, alpha, and beta
    }

    @Test
    public void testIsNull_EmptyGraph() {
        assertTrue(graph.isNull());
    }

    @Test
    public void testIsNull_GraphWithEdges() {
        graph.generateGraph(5, 50, 1, 10);
        assertFalse(graph.isNull());
    }

    @Test
    public void testIsCreated_EmptyGraph() {
        assertFalse(graph.isCreated());
    }

    @Test
    public void testIsCreated_GraphWithVertices() {
        graph.generateGraph(5, 50, 1, 10);
        assertTrue(graph.isCreated());
    }

    @Test
    public void testDeleteGraph() {
        graph.generateGraph(5, 50, 1, 10);
        graph.deleteGraph();
        assertTrue(graph.isNull()); // After deletion, the graph should be null
        assertFalse(graph.isCreated());
    }

    @Test
    public void testGenerateGraph() {
        graph.generateGraph(5, 50, 1, 10);
        assertEquals(5, graph.numberOfVertices);  // Check number of vertices
        assertTrue(graph.edges.size() > 0);    // Ensure some edges were created
    }

    @Test
    public void testCreateGraphFromFile_ValidInput() {
        String validInput = "0 10 0 0 20\n" +
                "10 0 10 10 0\n" +
                "0 10 0 10 0\n" +
                "0 10 10 0 10\n" +
                "20 0 0 10 0\n"; // Example adjacency matrix
        Scanner scanner = new Scanner(validInput);
        assertTrue(graph.createGraphFromFile(scanner));
    }

    @Test
    public void testCreateGraphFromFile_InvalidInput_NotSquareMatrix() {
        String invalidInput = "0 1 2\n3 4 5 6";
        Scanner scanner = new Scanner(invalidInput);
        assertFalse(graph.createGraphFromFile(scanner));
    }

    @Test
    public void testCreateGraphFromFile_InvalidInput_NonZeroDiagonal() {
        String invalidInput = "1 1 2\n3 4 5\n6 7 8";
        Scanner scanner = new Scanner(invalidInput);
        assertFalse(graph.createGraphFromFile(scanner));
    }

    @Test
    public void testCreateGraphFromFile_InvalidInput_NotSymmetric() {
        String invalidInput = "0 1 2\n3 0 5\n6 8 0";
        Scanner scanner = new Scanner(invalidInput);
        assertFalse(graph.createGraphFromFile(scanner));
    }
    @Test
    public void testRemovePheromone(){
        graph.generateGraph(5, 50, 1, 10);
        graph.removePheromone();
        for(Edge e : graph.edges){
            assertEquals(0, e.pheromone, 0.001);
        }
    }

    @Test
    public void testCreateMatrixFromEdgesAndVertices() {
        // Create sample vertices and edges
        Vertex v1 = new Vertex(10, 10, 'A');
        Vertex v2 = new Vertex(20, 20, 'B');
        Vertex v3 = new Vertex(30, 30, 'C');

        Edge e12 = new Edge(5, 0, 1); // Edge between v1 and v2 with weight 5
        Edge e13 = new Edge(8, 0, 2); // Edge between v1 and v3 with weight 8
        Edge e23 = new Edge(3, 1, 2); // Edge between v2 and v3 with weight 3

        // Add vertices and edges to the graph
        graph.numberOfVertices = 3;
        graph.vertices = new ArrayList<>(List.of(v1, v2, v3));
        graph.edges = new ArrayList<>(List.of(e12, e13, e23));

        // Call the method under test
        graph.createMatrixFromEdgesAndVertices();

        // Assertions
        assertEquals(3, graph.numberOfVertices);  // Check number of vertices
        assertEquals(3, graph.edges.size());    // Check number of edges

        // Verify adjacency matrix
        int[][] expectedAdjacencyMatrix = {
                {0, 5, 8},
                {5, 0, 3},
                {8, 3, 0}
        };
        assertArrayEquals(expectedAdjacencyMatrix, graph.adjacencyMatrix);

        // Verify links matrix (optional)
        boolean[][] expectedLinksMatrix = {
                {false, true, true},
                {true, false, true},
                {true, true, false}
        };
        assertArrayEquals(expectedLinksMatrix, graph.linksMatrix);
    }

}
