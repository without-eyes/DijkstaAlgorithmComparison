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

        // Run Dijkstra's algorithm with multithreading
        startTime = System.nanoTime();
        System.out.println("\nDijkstra's algorithm with multithreading: ");
        dijkstraAlgorithm.runAllVerticesMultithreaded(0, destination);
        long multithreadedEstimatedTime = System.nanoTime() - startTime;
        System.out.println("Estimated time: " + multithreadedEstimatedTime);

        // Print results
        float speedup = 100 * (regularEstimatedTime - multithreadedEstimatedTime) / (float)regularEstimatedTime;

        if (speedup >= 0) {
            System.out.println("\nMultithreaded Dijkstra's algorithm is " + speedup + "% faster than regular.");
        } else {
            System.out.println("\nMultithreaded Dijkstra's algorithm is " + (-speedup) + "% slower than regular.");
        }

    }
}