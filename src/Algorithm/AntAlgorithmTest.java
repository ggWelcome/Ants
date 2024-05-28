package Algorithm;

import Graph.*;
import Staff.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AntAlgorithmTest {

    private Graph graph;
    private AntAlgorithm antAlgorithm;

    @BeforeEach
    void setUp() {
        graph = new Graph();

        Vertex v1 = new Vertex(0, 1, 'A');
        Vertex v2 = new Vertex(3, 4, 'B');
        Vertex v3 = new Vertex(1, 8, 'C');

   

        // Use vertex indices to create edges
        Edge e12 = new Edge(2, 0, 1);  // Edge between v1 (index 0) and v2 (index 1)
        Edge e13 = new Edge(3, 0, 2);  // Edge between v1 (index 0) and v3 (index 2)
        Edge e23 = new Edge(1, 1, 2);  // Edge between v2 (index 1) and v3 (index 2)


        graph.numberOfVertices = 3;
        graph.vertices = new ArrayList<>(List.of(v1, v2, v3));
        graph.edges = new ArrayList<>(List.of(e12, e13, e23));

        // Initialize adjacencyMatrix based on the edges
        graph.createMatrixFromEdgesAndVertices(); // Use this method to create the matrix

        Pair<Integer, Integer> path = new Pair<>(0, 2); // Path from node A (index 0) to C (index 2)
        antAlgorithm = new AntAlgorithm(graph, 0.8, 0.1, 2, 5, path);
    }

    


    @Test
    void testFinished() {
        assertFalse(antAlgorithm.finished()); // Initially, the algorithm should not be finished
        for (int i = 0; i < antAlgorithm.numberOfAnts; i++) {
            antAlgorithm.step();
        }
        assertTrue(antAlgorithm.finished()); // After all ants have completed, it should be finished
    }

    @Test
    void testGetCount() {
        assertEquals(0, antAlgorithm.getCount());  // Initially, the count should be zero
        antAlgorithm.step();
        assertEquals(1, antAlgorithm.getCount());  // After one step, the count should be one
    }

    @Test
    void testStep_RandomAnts() {
        // Test the first few steps with random ants
        for (int i = 0; i < antAlgorithm.numberOfRandomAnts; i++) {
            List<Integer> path = antAlgorithm.step();
            assertValidPath(path);  // Ensure the path is valid (not null, has at least two nodes)
        }
    }

    @Test
    void testStep_PheromoneAnts() {
        // Run random ant steps first
        for (int i = 0; i < antAlgorithm.numberOfRandomAnts; i++) {
            antAlgorithm.step();
        }

        // Test steps with pheromone-influenced ants
        while (!antAlgorithm.finished()) {
            List<Integer> path = antAlgorithm.step();
            assertValidPath(path); // Ensure the path is valid
        }
    }

    @Test
    void testAutoAlgorithm() {
        List<Integer> bestPath = antAlgorithm.autoAlgorithm();
        assertValidPath(bestPath); // Ensure the path is valid
    }

    @Test
    void testFindPath() {
        // Set up a specific pheromone configuration to make the test deterministic
        for (Edge e : graph.edges) {
            e.pheromone = 0.01; // Initial pheromone value
        }

        // Set pheromone values for specific edges to guide the ant towards the optimal path
        graph.edges.get(0).pheromone = 1.0; // Edge A-B
        graph.edges.get(2).pheromone = 0.8; // Edge B-C

        // Expected path: A -> B -> C (indices: 0, 1, 2)
        List<Integer> expectedPath = new ArrayList<>(Arrays.asList(0, 1, 2));

        List<Integer> actualPath = antAlgorithm.findPath();

        // Remove the last element (distance) before comparing the paths
        actualPath.remove(actualPath.size() - 1);

        assertIterableEquals(expectedPath, actualPath); // Compare the paths
    }


    // Helper method to validate a path
    private void assertValidPath(List<Integer> path) {
        assertNotNull(path);
        assertTrue(path.size() >= 2); // At least start and end node
        assertEquals(antAlgorithm.startIndex, path.get(0)); // Path starts at startIndex
        assertEquals(antAlgorithm.finishIndex, path.get(path.size() - 2)); // Path ends at finishIndex (last element is the distance)
    }
}
