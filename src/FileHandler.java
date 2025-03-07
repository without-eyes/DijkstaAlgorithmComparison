import java.io.*;
import java.util.Random;

class FileHandler {
    private static final String filename = "inputData.txt";

    public static void saveToFile(DijkstraAlgorithm dijkstraAlgorithm, int numVertices) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(String.valueOf(numVertices));
            writer.newLine();

            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (new Random().nextInt(2) == 1) {
                        int weight = new Random().nextInt(numVertices + 10) + 1;
                        dijkstraAlgorithm.addEdge(i, j, weight);
                        writer.write(i + "\t" + j + "\t" + weight);
                        writer.newLine();
                    }
                }
            }
            System.out.println("Data saved to " + filename);
        }
    }

    public static DijkstraAlgorithm readFromFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
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
            System.out.println("Data loaded from " + filename);
            return dijkstraAlgorithm;
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found.");
            throw e;
        } catch (Exception e) {
            System.out.println("Error reading file. Please check the format.");
            throw e;
        }
    }
}