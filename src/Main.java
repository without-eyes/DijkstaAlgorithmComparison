import java.io.*;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        // input data
        Menu menu = new Menu();
        DijkstraAlgorithm dijkstraAlgorithm = menu.initializeAlgorithm();
        Random rand = new Random();
        int numVertices = dijkstraAlgorithm.getNumVertices();

        // Run Dijkstra's algorithm without multithreading
        int destination = rand.nextInt(numVertices) + 1;
        long startTime = System.nanoTime();
        System.out.println("\nDijkstra's algorithm without multithreading: ");
        dijkstraAlgorithm.runRegular(0, destination);
        long regularEstimatedTime = System.nanoTime() - startTime;
        System.out.println("Estimated time: " + regularEstimatedTime);

        // Run Dijkstra's algorithm with multithreading all vertices
        startTime = System.nanoTime();
        System.out.println("\nDijkstra's algorithm with multithreading all vertices: ");
        dijkstraAlgorithm.runAllVerticesMultithreaded(0, destination);
        long allVerticesMultithreadedEstimatedTime = System.nanoTime() - startTime;
        System.out.println("Estimated time: " + allVerticesMultithreadedEstimatedTime);

        // Run Dijkstra's algorithm with multithreading source's neighbours only
        startTime = System.nanoTime();
        System.out.println("\nDijkstra's algorithm with multithreading source's neighbours only: ");
        dijkstraAlgorithm.runFirstNeighboursMultithreaded(0, destination);
        long firstNeighboursMultithreadedEstimatedTime = System.nanoTime() - startTime;
        System.out.println("Estimated time: " + firstNeighboursMultithreadedEstimatedTime);

        // Print results
        System.out.println("\nRegular estimated time:                                 " + regularEstimatedTime);
        System.out.println("Multithreading all vertices estimated time:             " + allVerticesMultithreadedEstimatedTime);
        System.out.println("Multithreading source's neighbours only estimated time: " + firstNeighboursMultithreadedEstimatedTime);
    }
}