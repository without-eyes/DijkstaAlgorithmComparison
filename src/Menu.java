import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final Random rand = new Random();

    public DijkstraAlgorithm initializeAlgorithm() throws IOException {
        while (true) {
            System.out.print("Choose input method:\n1 - Generate random data\n2 - Read from file\nYour choice: ");
            int choice = scanner.nextInt();

            if (choice == 1) {
                return generateRandomData();
            } else if (choice == 2) {
                return FileHandler.readFromFile();
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
            FileHandler.saveToFile(dijkstraAlgorithm, numVertices);
        } else if (choice == 2) {
            generateEdges(dijkstraAlgorithm, numVertices);
        }
        return dijkstraAlgorithm;
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
}