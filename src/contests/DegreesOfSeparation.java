package contests;

import exceptions.ElementAlreadyPresentException;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import exceptions.WrongGraphTypeException;
import model.AdjacencyListGraph;
import model.AdjacencyMatrixGraph;
import model.GraphAlgorithms;
import model.IGraph;

import java.util.*;

import java.io.*;

/**
 * Class meant to be used in the virtual judge for the UVa 1056 - Degrees of Separation
 */
public class DegreesOfSeparation {
//    private static BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));

    public static void main(String[] args) throws IOException, ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException, WrongGraphTypeException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        GraphAlgorithms algorithms = new GraphAlgorithms();

        int edges;
        int degreeOfSeparation;
        int counter = 0;
        String[] input;
        String[] header;
        String person1;
        String person2;
        double[][] solution;
        IGraph<String> graph;

        while (!(header = br.readLine().split(" "))[0].equals("0")) {//Checks if there are no people in the network.
            graph = new AdjacencyMatrixGraph<>(false, true);
            edges = Integer.parseInt(header[1]);
            degreeOfSeparation = -1;
            counter++;

            for (int i = 0; i < edges; i++) {
                input = br.readLine().split(" ");
                person1 = input[0];
                person2 = input[1];

                if (graph.getVertices().get(person1) == null)//Checks presence of lang1 in the graph.
                    graph.addVertex(person1);
                if (graph.getVertices().get(person2) == null)//Checks presence of lang2 in the graph.
                    graph.addVertex(person2);

                graph.addEdge(person1, person2, 1);
            }
            solution = algorithms.floydWarshall(graph);
            for (int i = 0; i < graph.getVertexSize(); i++) {
                for (int j = 0; j < graph.getVertexSize(); j++) {
                    if (solution[i][j] > degreeOfSeparation) {
                        degreeOfSeparation = (int) solution[i][j];
                    }
//                    System.out.print(graph.weightMatrix()[i][j] + "\t");
                }
//                System.out.println();
            }
//            System.out.println(solution.length);

            System.out.println("Network " + counter + ": " +
                    (degreeOfSeparation == Integer.MAX_VALUE ? "DISCONNECTED" : degreeOfSeparation) + "\n");
        }


//        String input = rd.readLine();;
//        String output = "";
//        int count = 1;
//        while(!input.equals("0 0")) {
//            Graph g = new Graph(input);
//            String[] network = rd.readLine().split(" ");
//            for(int i = 0; i<network.length-1; i+=2){
//                g.addEdge(network[i], network[i+1]);
//            }
//            output += "Network " + count + g.returnSolution()+"\n";
//            count++;
//            input = rd.readLine();
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
        final static int INFINITY = Integer.MAX_VALUE;

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
                weightMatrix[vertices.indexOf(u)][vertices.indexOf(u)] = 0;
            }
            if (!vertices.contains(v)) {
                vertices.add(v);
                weightMatrix[vertices.indexOf(v)][vertices.indexOf(v)] = 0;
            }
            //Connect both vertexes by assigning their weight in the matrix to 1.
            weightMatrix[vertices.indexOf(u)][vertices.indexOf(v)] = 1;
            weightMatrix[vertices.indexOf(v)][vertices.indexOf(u)] = 1;
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
                        if (i != j)
                            if (d[i][k] != INFINITY || d[k][j] != INFINITY)
                                dk[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                            else
                                dk[i][j] = d[i][j];
                d = dk;
            }
            return d;
        }

        String returnSolution() {
            System.out.println("Pre algorithm Matrix:\n" + printMatrix(weightMatrix));
            int[][] matrix = solve();
            System.out.println("Post algorithm Matrix:\n" + printMatrix(matrix));
            int max = 0;
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    if (matrix[i][j] != INFINITY) {
                        if (max < matrix[i][j])
                            max = matrix[i][j];
                    } else
                        return ": DISCONNECTED";
                }
            }
            return ": " + max;
        }
    }

    static String printMatrix(int[][] matrix) {
        int n = matrix.length;
        int m = matrix[0].length;
        String out1 = "/ ";
        String out2 = "";
        for (int i = 0; i < m; i++) {
            out1 += i + " ";
        }

        for (int i = 0; i < n; i++) {
            out2 += i + " ";
            for (int j = 0; j < m; j++) {
                out2 += matrix[i][j] + " ";
            }
            out2 += "\n";
        }
        return out1 + "\n" + out2;
    }
}