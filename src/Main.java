import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        // input data
        Scanner myObj = new Scanner(System.in);
        System.out.print("Do you want to generate random data(1) or read from file(else): ");
        int choice = myObj.nextInt();

        DijkstraAlgorithm dijkstraAlgorithm;
        int numVertices;
        Random rand = new Random();
        if (choice == 1) {
            System.out.print("Enter number of vertices: ");
            numVertices = myObj.nextInt();
            dijkstraAlgorithm = new DijkstraAlgorithm(numVertices);

            System.out.print("Do you want to save into file(1 for yes, else for no): ");
            choice = myObj.nextInt();

            if (choice == 1) {
                BufferedWriter writer = new BufferedWriter(new FileWriter("inputData.txt"));
                writer.write(String.valueOf(numVertices));
                writer.newLine();

                for (int i = 0; i < numVertices; i++) {
                    for (int j = i + 1; j < numVertices; j++) {
                        if (rand.nextInt(2) == 1) {
                            int weight = rand.nextInt(10) + 1;
                            dijkstraAlgorithm.addEdge(i, j, weight);
                            writer.write(i + "\t" + j + "\t" + weight);
                            writer.newLine();
                        }
                    }
                }

                writer.close();
            } else {
                for (int i = 0; i < numVertices; i++) {
                    for (int j = i + 1; j < numVertices; j++) {
                        if (rand.nextInt(2) == 1) {
                            int weight = rand.nextInt(10) + 1;
                            dijkstraAlgorithm.addEdge(i, j, weight);
                        }
                    }
                }
            }
        } else {
            BufferedReader reader = new BufferedReader(new FileReader("inputData.txt"));
            String line = reader.readLine();
            numVertices = Integer.parseInt(line.trim());
            dijkstraAlgorithm = new DijkstraAlgorithm(numVertices);

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                int vertex1 = Integer.parseInt(parts[0].trim());
                int vertex2 = Integer.parseInt(parts[1].trim());
                int weight = Integer.parseInt(parts[2].trim());
                dijkstraAlgorithm.addEdge(vertex1, vertex2, weight);
            }

            reader.close();
        }

        // run dijkstra algorithm without multithreading
        int destination = rand.nextInt(numVertices) + 1;
        long startTime = System.nanoTime();
        System.out.println("\nDijkstra's algorithm without multithreading: ");
        dijkstraAlgorithm.runRegular(0, destination);
        long regularEstimatedTime = System.nanoTime() - startTime;
        System.out.println("Estimated time: " + regularEstimatedTime);

        // run dijkstra algorithm with multithreading
        startTime = System.nanoTime();
        System.out.println("\nDijkstra's algorithm with multithreading: ");
        dijkstraAlgorithm.runMulthithreaded(0, destination);
        long multithreadedEstimatedTime = System.nanoTime() - startTime;
        System.out.println("Estimated time: " + multithreadedEstimatedTime);

        // print results
        System.out.println("\nMultithreaded Dijkstra's algorithm is " + (float)multithreadedEstimatedTime / regularEstimatedTime * 100 + "% faster than regular.");
    }
}