import java.util.*;
import java.util.concurrent.*;

class DijkstraAlgorithm {
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

    void addEdge(int start, int end, int weight) {
        adjacencyList.get(start).add(new Node(end, weight));
        adjacencyList.get(end).add(new Node(start, weight));
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

    void runRegular(int source, int destination) {
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

    public void runMultithreaded(int source, int destination) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        int[] shortestDistances = new int[numVertices];
        int[] previousVertices = new int[numVertices];
        Arrays.fill(shortestDistances, Integer.MAX_VALUE);
        Arrays.fill(previousVertices, -1);
        shortestDistances[source] = 0;
        ConcurrentLinkedQueue<Node> priorityQueue = new ConcurrentLinkedQueue<>();
        priorityQueue.add(new Node(source, 0));

        forkJoinPool.submit(() -> priorityQueue.parallelStream().forEach(node -> {
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
        })).join();

        forkJoinPool.shutdown();
        printResults(shortestDistances, previousVertices, source, destination);
    }
}