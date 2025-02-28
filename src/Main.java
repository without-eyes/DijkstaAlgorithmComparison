public class Main {
    public static void main(String[] args) {
        // run dijkstra algorithm without multithreading
        long startTime = System.nanoTime();
        DijkstraAlgorithm g = new DijkstraAlgorithm(6);
        g.addEdge(0, 1, 4);
        g.addEdge(0, 2, 4);
        g.addEdge(1, 2, 2);
        g.addEdge(1, 3, 5);
        g.addEdge(2, 3, 8);
        g.addEdge(3, 4, 6);
        g.addEdge(4, 5, 9);

        System.out.println("Dijksta's algorithm without multithreading: ");
        g.dijkstraRegular(0, 5);
        System.out.println("Estimated time: " + (System.nanoTime() - startTime));

        // run dijkstra algorithm with multithreading


        // print results

    }
}