import java.io.*;
import java.util.Random;
import java.util.Scanner;

class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final Random rand = new Random();

    public DijkstraAlgorithm initializeAlgorithm() throws IOException {
        while (true) {
            System.out.print("Choose input method: \n1 - Generate random data\n2 - Read from file\nYour choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                return generateRandomData();
            } else if (choice == 2) {
                return readDataFromFile();
            } else {
                System.out.println("Invalid choice. Please enter 1 or 2.");
            }
        }
    }

    private DijkstraAlgorithm generateRandomData() throws IOException {
        System.out.print("Enter number of vertices: ");
        int numVertices = scanner.nextInt();
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(numVertices);

        System.out.print("Do you want to save into file? (1 - Yes, 2 - No): ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            saveToFile(dijkstraAlgorithm, numVertices);
        } else if (choice == 2) {
            generateEdges(dijkstraAlgorithm, numVertices);
        }
        return dijkstraAlgorithm;
    }

    private void saveToFile(DijkstraAlgorithm dijkstraAlgorithm, int numVertices) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inputData.txt"))) {
            writer.write(String.valueOf(numVertices));
            writer.newLine();

            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (rand.nextInt(2) == 1) {
                        int weight = rand.nextInt(numVertices + 10) + 1;
                        dijkstraAlgorithm.addEdge(i, j, weight);
                        writer.write(i + "\t" + j + "\t" + weight);
                        writer.newLine();
                    }
                }
            }
        }
    }

    private void generateEdges(DijkstraAlgorithm dijkstraAlgorithm, int numVertices) {
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (rand.nextInt(2) == 1) {
                    int weight = rand.nextInt(numVertices + 10) + 1;
                    dijkstraAlgorithm.addEdge(i, j, weight);
                }
            }
        }
    }

    private DijkstraAlgorithm readDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader("inputData.txt"))) {
            int numVertices = Integer.parseInt(reader.readLine().trim());
            DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(numVertices);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                int vertex1 = Integer.parseInt(parts[0].trim());
                int vertex2 = Integer.parseInt(parts[1].trim());
                int weight = Integer.parseInt(parts[2].trim());
                dijkstraAlgorithm.addEdge(vertex1, vertex2, weight);
            }
            return dijkstraAlgorithm;
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found. Please try again.");
            return readDataFromFile();
        } catch (Exception e) {
            System.out.println("Error reading file. Please ensure it's correctly formatted.");
            return readDataFromFile();
        }
    }
}