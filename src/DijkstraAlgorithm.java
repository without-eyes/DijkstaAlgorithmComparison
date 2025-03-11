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

    int getNumVertices() {
        return numVertices;
    }

    void addEdge(int start, int end, int weight) {
        adjacencyList.get(start).add(new Node(end, weight));
        adjacencyList.get(end).add(new Node(start, weight));
    }

    public boolean hasEdge(int i, int j) {
        return adjacencyList.get(i).stream().anyMatch(n -> n.vertex == j);
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

    void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        System.out.println("Used memory: " + usedMemory / 1024 / 1024 + " MB");
    }

    void runRegular(int source, int destination) {
        printMemoryUsage();

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

        printMemoryUsage();
    }

    public void runAllVerticesMultithreaded(int source, int destination) {
        printMemoryUsage();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        PriorityBlockingQueue<Node> priorityQueue = new PriorityBlockingQueue<>(numVertices, Comparator.comparingInt(n -> n.weight));
        int[] shortestDistances = new int[numVertices];
        int[] previousVertices = new int[numVertices];
        Arrays.fill(shortestDistances, Integer.MAX_VALUE);
        Arrays.fill(previousVertices, -1);
        shortestDistances[source] = 0;
        priorityQueue.add(new Node(source, 0));
        boolean[] visited = new boolean[numVertices];

        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.poll();
            int currentVertex = node.vertex;
            if (visited[currentVertex]) continue;
            visited[currentVertex] = true;

            List<Callable<Void>> tasks = new ArrayList<>();
            for (Node neighbor : adjacencyList.get(currentVertex)) {
                tasks.add(() -> {
                    int adjacentVertex = neighbor.vertex;
                    int weight = neighbor.weight;
                    synchronized (shortestDistances) {
                        if (shortestDistances[currentVertex] + weight < shortestDistances[adjacentVertex]) {
                            shortestDistances[adjacentVertex] = shortestDistances[currentVertex] + weight;
                            previousVertices[adjacentVertex] = currentVertex;
                            priorityQueue.add(new Node(adjacentVertex, shortestDistances[adjacentVertex]));
                        }
                    }
                    return null;
                });
            }

            try {
                executor.invokeAll(tasks);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        executor.shutdown();
        printResults(shortestDistances, previousVertices, source, destination);

        printMemoryUsage();
    }

    void runFirstNeighboursMultithreaded(int source, int destination) {
        printMemoryUsage();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        PriorityQueue<Node> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(n -> n.weight));
        int[] shortestDistances = new int[numVertices];
        int[] previousVertices = new int[numVertices];
        boolean[] visited = new boolean[numVertices];

        Arrays.fill(shortestDistances, Integer.MAX_VALUE);
        Arrays.fill(previousVertices, -1);
        shortestDistances[source] = 0;
        priorityQueue.add(new Node(source, 0));

        List<Future<Void>> futures = new ArrayList<>();

        for (Node neighbor : adjacencyList.get(source)) {
            Future<Void> future = executor.submit(() -> {
                int adjacentVertex = neighbor.vertex;
                int weight = neighbor.weight;
                synchronized (shortestDistances) {
                    if (shortestDistances[source] + weight < shortestDistances[adjacentVertex]) {
                        shortestDistances[adjacentVertex] = shortestDistances[source] + weight;
                        previousVertices[adjacentVertex] = source;
                        priorityQueue.add(new Node(adjacentVertex, shortestDistances[adjacentVertex]));
                    }
                }
                return null;
            });
            futures.add(future);
        }

        try {
            for (Future<Void> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        executor.shutdown();

        while (!priorityQueue.isEmpty()) {
            Node node = priorityQueue.poll();
            int currentVertex = node.vertex;
            if (visited[currentVertex]) continue;
            visited[currentVertex] = true;

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

        printMemoryUsage();
    }
}
