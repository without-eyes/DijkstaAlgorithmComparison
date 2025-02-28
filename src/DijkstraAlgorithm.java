import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DijkstraAlgorithm {
    private final int numVertices;
    private final List<List<Node>> adjacencyList;

    static class Node {
        int vertex, weight;

        Node(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }
    }

    DijkstraAlgorithm(int vertices) {
        this.numVertices = vertices;
        adjacencyList = new ArrayList<>(numVertices);
        for (int i = 0; i < numVertices; i++) {
            adjacencyList.add(new ArrayList<>());
        }
    }

    void addEdge(int startVertex, int endVertex, int weight) {
        adjacencyList.get(startVertex).add(new Node(endVertex, weight));
        adjacencyList.get(endVertex).add(new Node(startVertex, weight));
    }

    void printResults(int[] shortestDistances, int[] previousVertices, int source, int destination) {
        List<Integer> path = new ArrayList<>();
        for (int currentVertex = destination; currentVertex != -1; currentVertex = previousVertices[currentVertex]) {
            path.add(currentVertex);
        }
        Collections.reverse(path);

        System.out.println("Shortest path from vertex " + source + " to " + destination + " = " + shortestDistances[destination]);
        System.out.println("Path: " + path);
    }

    void dijkstraRegular(int source, int destination) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.weight));
        int[] shortestDistances = new int[numVertices];
        int[] previousVertices = new int[numVertices];

        Arrays.fill(shortestDistances, Integer.MAX_VALUE);
        Arrays.fill(previousVertices, -1);
        shortestDistances[source] = 0;
        priorityQueue.add(new Node(source, 0));

        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.poll();
            int currentVertex = node.vertex;

            for (Node neighbor : adjacencyList.get(currentVertex)) {
                int adjacentVertex = neighbor.vertex;
                int weight = neighbor.weight;
                if (shortestDistances[currentVertex] + weight < shortestDistances[adjacentVertex]) {
                    shortestDistances[adjacentVertex] = shortestDistances[currentVertex] + weight;
                    previousVertices[adjacentVertex] = currentVertex;
                    priorityQueue.add(new Node(adjacentVertex, shortestDistances[adjacentVertex]));
                }
            }
        }

        printResults(shortestDistances, previousVertices, source, destination);
    }

    public void dijkstraMulthithreaded(int source, int destination) {
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.weight));
        int[] shortestDistances = new int[numVertices];
        int[] previousVertices = new int[numVertices];

        Arrays.fill(shortestDistances, Integer.MAX_VALUE);
        Arrays.fill(previousVertices, -1);
        shortestDistances[source] = 0;
        priorityQueue.add(new Node(source, 0));

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.poll();
            int currentVertex = node.vertex;

            List<Callable<Void>> tasks = new ArrayList<>();
            for (Node neighbor : adjacencyList.get(currentVertex)) {
                final int adjacentVertex = neighbor.vertex;
                final int weight = neighbor.weight;
                tasks.add(() -> {
                    if (shortestDistances[currentVertex] + weight < shortestDistances[adjacentVertex]) {
                        synchronized (this) {
                            if (shortestDistances[currentVertex] + weight < shortestDistances[adjacentVertex]) {
                                shortestDistances[adjacentVertex] = shortestDistances[currentVertex] + weight;
                                previousVertices[adjacentVertex] = currentVertex;
                                priorityQueue.add(new Node(adjacentVertex, shortestDistances[adjacentVertex]));
                            }
                        }
                    }
                    return null;
                });
            }

            try {
                executorService.invokeAll(tasks);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        executorService.shutdown();

        printResults(shortestDistances, previousVertices, source, destination);
    }
}
