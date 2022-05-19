package amazon_assessment.graphs.java;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

class Main {
    public static void main(String[] args) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(System.out));
        bufferedWriter.write(String.valueOf(5));
    }

    public static int kruskals(int gNodes, List<Integer> gFrom, List<Integer> gTo, List<Integer> gWeight) {
        KruskalsGraph graph = new KruskalsGraph(gNodes);

        for (int i = 0; i < gFrom.size(); i++) {
            Edge edge = new Edge(gFrom.get(i), gTo.get(i), gWeight.get(i));
            graph.addEdge(edge);
        }

        ArrayList<Edge> minimumSpanningTree = graph.findMinimumSpanningTree();
        int sum = 0;

        for (Edge edge: minimumSpanningTree) {
            sum += edge.weight;
        }

        return sum;
    }
}

class Edge implements Comparable<Edge> {
    public int source;
    public int destination;
    public int weight;

    public Edge(int source, int destination, int weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public int compareTo(Edge o) {
        return weight - o.weight;
    }
}

class Subset {
    public int parent;
    public int rank;

    public Subset(int parent, int rank) {
        this.parent = parent;
        this.rank = rank;
    }
}

class KruskalsGraph {
    private PriorityQueue<Edge> edges = new PriorityQueue<>();
    private Subset[] subsets;

    public KruskalsGraph(int numberOfVertices) {
        this.subsets = new Subset[numberOfVertices + 1];
        for (int i = 0; i < subsets.length; i++) {
            subsets[i] = new Subset(i, 0);
        }
    }

    void addEdge(Edge edge) {
        edges.add(edge);
    }

    ArrayList<Edge> findMinimumSpanningTree() {
        ArrayList<Edge> minimumSpanningTree = new ArrayList<>();

        while (edges.size() > 0) {
            Edge smallestEdge = edges.poll();
            int sourceParentVertex = findParentVertex(smallestEdge.source);
            int destinationParentVertex = findParentVertex(smallestEdge.destination);

            // If they have the same parent a cycle would be created
            if (sourceParentVertex != destinationParentVertex) {
                minimumSpanningTree.add(smallestEdge);
                union(sourceParentVertex, destinationParentVertex);
            }
        }

        return minimumSpanningTree;
    }

    private int findParentVertex(int vertex) {
        if (subsets[vertex].parent != vertex) {
            subsets[vertex].parent = findParentVertex(subsets[vertex].parent);
        }

        return subsets[vertex].parent;
    }

    private void union(int source, int destination) {
        int sourceParentVertex = findParentVertex(source);
        int destinationParentVertex = findParentVertex(destination);

        if (subsets[sourceParentVertex].rank > subsets[destinationParentVertex].rank) {
            subsets[destinationParentVertex].parent = sourceParentVertex;
        } else if (subsets[destinationParentVertex].rank > subsets[sourceParentVertex].rank) {
            subsets[sourceParentVertex].parent = destinationParentVertex;
        } else {
            subsets[destinationParentVertex].parent = sourceParentVertex;
            subsets[sourceParentVertex].rank++;
        }
    }
}
