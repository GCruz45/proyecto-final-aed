package contests;

import exceptions.ElementAlreadyPresentException;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import model.AdjacencyListGraph;
import model.AdjacencyMatrixGraph;
import model.GraphAlgorithms;
import model.IGraph;

import java.util.*;

import java.io.*;

/**
 * Class meant to be used in the virtual judge for the UVa 11492 - Babel problem
 */
public class Babel {
//    private static BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        GraphAlgorithms algorithms = new GraphAlgorithms();

        int words;
        String[] input;
        String startLang;
        String endLang;
        String lang1;
        String lang2;
        String edge;
        int edgeSize;
        double[][] solution;
        IGraph<String> graph;

        while ((words = Integer.parseInt(br.readLine())) != 0) {
            graph = new AdjacencyMatrixGraph<>(false, true);
            input = br.readLine().split(" ");
            startLang = input[0];
            endLang = input[1];
            graph.addVertex(startLang);
            graph.addVertex(endLang);
            for (int i = 0; i < words; i++) {
                input = br.readLine().split(" ");
                lang1 = input[0];
                lang2 = input[1];
                edge = input[2];
                edgeSize = input[2].length();

                if (graph.getVertices().get(lang1) == null)//Checks presence of lang1 in the graph.
                    graph.addVertex(lang1);
                if (graph.getVertices().get(lang2) == null)//Checks presence of lang2 in the graph.
                    graph.addVertex(lang2);

                if (graph.areConnected(lang1, lang2)) {
                    int index1 = graph.getIndex(lang1);
                    int index2 = graph.getIndex(lang2);
                    if (edge < graph.weightMatrix()[index1][index2]) {
                        graph.removeEdge(lang1, lang2);
                        graph.addEdge(lang1, lang2, edge);
                    }
                } else
                    graph.addEdge(lang1, lang2, edge);
            }
            solution = algorithms.dijkstra(graph, startLang);
            if (solution[graph.getIndex(endLang)][0] != Double.MAX_VALUE)
                System.out.println((int) solution[graph.getIndex(endLang)][0]);
            else
                System.out.println("impossivel");
        }

//        String input = "";
//        String output = "";
//        int count = 1;
//        IGraph graph = new AdjacencyMatrixGraph(false,false);
//        while(!input.equals("0 0")) {
//            input = rd.readLine();
//            Graph g = new Graph(input);
//            String[] network = rd.readLine().split(" ");
//            for(int i = 0; i<network.length; i++){
//                g.addEdge(network[i], network[i+1]);
//            }
//            output += "Network " + count + g.returnSolution();
//        }
//        System.out.println(output);
    }


    private static class Graph {
        /**
         * Vertices already added in the graph
         */
        private List<String> vertices;
        /**
         * Weight between vertices in the graph. -1 (or INFINITY) means not connected. 1 means already connected.
         */
        private int[][] weightMatrix;

        /**
         * Representative value to be used when two vertices in the graph are not connected.
         */
        final static int INFINITY = -1;

        /**
         * Creates a new Graph represented in a weight matrix. The input String is the number of vertices this graph has.
         */
        Graph(String size) {
            int s = Integer.parseInt(size.split(" ")[0]);
            weightMatrix = new int[s][s];
            for (int i = 0; i < weightMatrix.length; i++)
                for (int j = 0; j < weightMatrix[0].length; j++)
                    weightMatrix[i][j] = INFINITY;
            vertices = new ArrayList<>();
        }

        /**
         * Connects vertex u with vertex v by assigning their weight to 1.
         *
         * @param u The first of the vertex pair to be added.
         * @param v The second of the vertex pair to be added.
         */
        void addEdge(String u, String v) {
            if (!vertices.contains(u)) {
                vertices.add(u);
            }
            if (!vertices.contains(v)) {
                vertices.add(v);
            }
            //Connect both vertexes by assigning their weight in the matrix to 1.
            weightMatrix[vertices.indexOf(u)][vertices.indexOf(v)] = 1;
        }

        /**
         * This method solves the main problem by using the Floyd-Warshall algorithm over the adjacency matrix of this graph.
         *
         * @return An adjacency matrix with the weight of the shortest path between all of the people in the graph.
         */
        int[][] solve() {
            int[][] d = weightMatrix;
            int n = vertices.size();
            for (int k = 0; k < n; k++) {
                int[][] dk = new int[n][n];
                for (int j = 0; j < n; j++)
                    for (int i = 0; i < n; i++)
                        if (d[i][k] != INFINITY || d[k][j] != INFINITY)
                            dk[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                        else
                            dk[i][j] = d[i][j];
                d = dk;
            }
            return d;
        }

        String returnSolution() {
            int[][] matrix = solve();
            int max = 0;
            for (int[] ints : matrix) {
                for (int j = 0; j < matrix.length; j++) {
                    if (ints[j] != INFINITY) {
                        if (max < ints[j])
                            max = ints[j];
                    } else
                        return ": DISCONNECTED";
                }
            }
            return ": " + max;
        }
    }
}
