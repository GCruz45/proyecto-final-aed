package contests;

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

    /**
     * This interface has the minimum and general features that a graph should implement no matter which would be its representation. Does not admit multi-graphs.
     *
     * @param <V> Abstract data type which represents an object from a natural problem that is going to be modeled as a vertex in a graph representation of the problem
     * @author AED Class # 003 // 2019
     * @version 1.0 - 10/2019
     */
    public interface IGraph<V> {

        /**
         * Adds a vertex to the graph
         *
         * @param u The new vertex to be added
         */
        void addVertex(V u) throws ElementAlreadyPresentException;

        /**
         * Adds a weighted edge to the graph
         * If the graph is directed the connection will be from U to V
         * <pre> U and V have to exist in the graph
         * @param u a vertex within the graph
         * @param v a vertex within the graph
         * @param w    is the weight of the edge
         */
        void addEdge(V u, V v, double w) throws WrongEdgeTypeException, ElementNotFoundException;

        /**
         * <pre> The graph is weighted
         * @return A matrix with the weight of all the connections
         */
        double[][] weightMatrix();

        /**
         * Returns whether the graph is weighted.
         *
         * @return true if and only if graph is weighted
         */
        boolean isWeighted();

        /**
         * Gives the amount of vertices in the graph.
         *
         * @return an int with said amount.
         */
        int getVertexSize();

        /**
         * Gives a Map that pairs vertices with their index
         *
         * @return said Map
         */
        Map<V, Integer> getVertices();
    }

    /**
     * An Object that models a graph using an Adjacency Matrix.
     *
     * @param <V> the type of vertex in the graph
     * @author AED Class # 003 // 2019
     * @version 1.0 - 10/2019
     */
    public static class AdjacencyMatrixGraph<V> implements IGraph<V> {

        /**
         * The length of the matrix when using the default Constructor.
         */
        private static final int DEFAULT_CAPACITY = 10;

        /**
         * The rate at which the matrix's length increases as it becomes full.
         */
        private static final double GROWTH_FACTOR = 1.5;

        /**
         * The last index in the matrix at which a vertex exists.
         */
        private int size; //logic size

        /**
         * If the graph is directed.
         */
        private boolean isDirected;

        /**
         * If the graph is weighted.
         */
        private boolean isWeighted;

        /**
         * The matrix itself.
         */
        private double[][] adjacencyMatrix;

        /**
         * The associated matrix containing the weight of all edged between nodes in the graph.
         */
        private double[][] weightMatrix;

        /**
         * A Map that accesses any vertex in the graph through its index in the matrix.
         */
        private Map<Integer, Vertex> vertices;

        /**
         * A map that pairs vertices to their index.
         */
        private Map<V, Integer> verticesIndices;

        /**
         * A map that pairs each vertex to a list representing all adjacent vertices along with the weight of the edge they share.
         */
        private Map<V, List<Map<V, Double>>> edges;

        /**
         * A Set that contains ordered, non-duplicate Integers of empty row/columns in the matrix whose values are lesser
         * than the logical size.
         */
        private NavigableSet<Integer> emptySlots;

        /**
         * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
         * vertices to their indices in the matrix and indices in the matrix to their vertices. The graph represented by the
         * matrix is directed if id is true.
         *
         * @param id true if the graph is directed.
         * @param iw true if the graph is weighted.
         */
        AdjacencyMatrixGraph(boolean id, boolean iw) {
            initialize();
            isDirected = id;
            isWeighted = iw;
        }

        /**
         * Auxiliary method used by the Constructor to set values to the class' fields. Creates the adjacency matrix.
         *
         */
        private void initialize() {
            isDirected = false;
            isWeighted = false;
            size = 0;
            adjacencyMatrix = new double[AdjacencyMatrixGraph.DEFAULT_CAPACITY][AdjacencyMatrixGraph.DEFAULT_CAPACITY];
            weightMatrix = new double[AdjacencyMatrixGraph.DEFAULT_CAPACITY][AdjacencyMatrixGraph.DEFAULT_CAPACITY];
            for (int i = 0; i < AdjacencyMatrixGraph.DEFAULT_CAPACITY; i++) {
                for (int j = 0; j < AdjacencyMatrixGraph.DEFAULT_CAPACITY; j++)
                    weightMatrix[i][j] = Double.MAX_VALUE;
            }
            vertices = new HashMap<>();
            verticesIndices = new HashMap<>();
            emptySlots = new TreeSet<>();
            edges = new HashMap<>();
        }

        /**
         * Adds the given vertex to the graph.
         *
         * @param u the new vertex to be added
         * @throws ElementAlreadyPresentException if u is already present in the graph
         */
        @SuppressWarnings("ConstantConditions, unchecked")
        @Override
        public void addVertex(V u) throws ElementAlreadyPresentException {
            boolean added;
            int index;
            if (verticesIndices.get(u) == null) {
                if (emptySlots.isEmpty()) {//No reusable rows/columns in the matrix
                    if (size == adjacencyMatrix.length) {//Needs to initialize a bigger array
                        int newLength = (int) (adjacencyMatrix.length * GROWTH_FACTOR);

                        double[][] placeHolder = new double[newLength][newLength];//Temporarily holds the data of weightMatrix.
                        for (int i = 0; i < adjacencyMatrix.length; i++) {
                            System.arraycopy(adjacencyMatrix[i], 0, placeHolder[i], 0, adjacencyMatrix.length);
                        }
                        adjacencyMatrix = placeHolder;

                        if (isWeighted) {
                            double[][] weightPlaceholder = new double[newLength][newLength];//Temporarily holds the data of weightMatrix.
                            for (int i = 0; i < newLength; i++) {
                                for (int j = 0; j < newLength; j++) {
                                    if (i >= weightMatrix.length || j >= weightMatrix.length)
                                        weightPlaceholder[i][j] = Double.MAX_VALUE;
                                    else weightPlaceholder[i][j] = weightMatrix[i][j];
                                }
                            }
                            weightMatrix = weightPlaceholder;
                        }
                    }
                    size++;
                    index = size - 1;
                } else
                    index = emptySlots.pollFirst();
                vertices.put(index, new Vertex(index, u));
                verticesIndices.put(u, index);
                edges.put(u, new ArrayList<>());
                weightMatrix[index][index] = 0.0;
                added = true;
            } else
                throw new ElementAlreadyPresentException("Element is already present");
        }

        /**
         * Adds a directed edge from vertex 'u' to vertex 'v' if isDirected with weight 'w'. Adds an edge between vertices
         * 'u' and 'v' of weight 'w' otherwise. Admits loops.
         *
         * @param u a vertex within the graph
         * @param v a vertex within the graph
         * @param w is the weight of the edge between 'index' and 'v'
         * @throws WrongEdgeTypeException if adding an uweighted edge to a weighted graph
         */
        @Override
        public void addEdge(V u, V v, double w) throws WrongEdgeTypeException, ElementNotFoundException {
            if (!isWeighted)
                throw new WrongEdgeTypeException("Tried to add a weighted edge to an unweighted graph.");

            Integer x = verticesIndices.get(u);
            Integer y = verticesIndices.get(v);
            if (x != null && y != null) {
                Map<V, Double> mapOfV = new HashMap<>();
                mapOfV.put(v, w);
                edges.get(u).add(mapOfV);
                adjacencyMatrix[x][y] = 1;
                weightMatrix[x][y] = w;
                if (!isDirected) {
                    adjacencyMatrix[y][x] = 1;
                    weightMatrix[y][x] = w;
                    Map<V, Double> mapOfU = new HashMap<>();
                    mapOfU.put(u, w);
                    edges.get(v).add(mapOfU);
                }
            } else {
                if (x == null)
                    throw new ElementNotFoundException("First element not found in graph");
                else
                    throw new ElementNotFoundException("Second element not found in graph");
            }
        }

        /**
         * Returns the weight matrix containing the weights of every edge, directed or not, between all vertices in the
         * graph.
         *
         * @return the matrix containing all weights in the graph
         */
        @Override
        public double[][] weightMatrix() {
            return weightMatrix;
        }

        /**
         * Returns whether the graph is weighted.
         *
         * @return true if and only if graph is weighted
         */
        @Override
        public boolean isWeighted() {
            return isWeighted;
        }

        /**
         * Gives the amount of vertices in the graph.
         *
         * @return an int with said amount.
         */
        @Override
        public int getVertexSize() {
            return size;
        }

        /**
         * Gives a Map that pairs vertices with their index
         *
         * @return said Map
         */
        @Override
        public Map<V, Integer> getVertices() {
            return verticesIndices;
        }
    }

    /**
     * This class is meant to provide different algorithms useful in managing graphs.
     *
     * @author AED Class # 003 // 2019
     * @version 1.0 - 10/2019
     */
    public static class GraphAlgorithms {
        /**
         * An algorithm based on the approach by Floyd and Warshall for finding the minimum distance from all nodes in a
         * graph to every other node. If reaching vertex 'j' from vertex 'i' is not possible, position [i][j] returns
         * Double.MAX_VALUE.
         *
         * @param g   the graph to be queried
         * @param <V> the type of nodes in the graph
         * @return a matrix of length 'n' by 'n', where 'n' is the amount of vertices in the graph. Position [i][j] returns the
         * minimum distance required to traverse the graph from vertex 'i' to vertex 'j'
         * @throws WrongGraphTypeException if the given graph is unweighted
         */
        <V> double[][] floydWarshall(IGraph<V> g) throws WrongGraphTypeException {
            if (!g.isWeighted()) {
                throw new WrongGraphTypeException("Expected weighted graph");
            }

            int n = g.getVertexSize();
            double[][] d = new double[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++)
                    d[i][j] = g.weightMatrix()[i][j];
            }

            for (int k = 0; k < n; k++) {
                double[][] dk = new double[n][n];
                for (int j = 0; j < n; j++)
                    for (int i = 0; i < n; i++) {
                        if (d[i][k] == Double.MAX_VALUE || d[k][j] == Double.MAX_VALUE) {
                            dk[i][j] = d[i][j];
                        } else
                            dk[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                    }
                d = dk;
            }
            return d;
        }
    }

    static class Vertex<V> implements Comparable<Vertex> {
        Integer index;
        V info;

        Vertex(int index, V info) {
            this.index = index;
            this.info = info;
        }

        @Override
        public int compareTo(Vertex vertexToCompare) {
            return index.compareTo(vertexToCompare.index);
        }
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

    /**
     * Custom exception to be thrown when a vertex or edge is not found in the graph.
     */
    public static class ElementNotFoundException extends Exception {

        /**
         * Constructor that replaces the message shown by the super class by the one provided.
         *
         * @param message is to be shown when the exception is thrown
         */
        public ElementNotFoundException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception to be thrown when a vertex or edge is not found in the graph.
     */
    public static class ElementAlreadyPresentException extends Exception {

        /**
         * Constructor that replaces the message shown by the super class by the one provided.
         *
         * @param message is to be shown when the exception is thrown
         */
        ElementAlreadyPresentException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception to be thrown when a vertex or edge is not found in the graph.
     */
    public static class WrongGraphTypeException extends Exception {

        /**
         * Constructor that replaces the message shown by the super class by the one provided.
         *
         * @param message is to be shown when the exception is thrown
         */
        WrongGraphTypeException(String message) {
            super(message);
        }
    }

    /**
     * Custom exception to be thrown when a vertex or edge is not found in the graph.
     */
    public static class WrongEdgeTypeException extends Exception {

        /**
         * Constructor that replaces the message shown by the super class by the one provided.
         *
         * @param message is to be shown when the exception is thrown
         */
        public WrongEdgeTypeException(String message) {
            super(message);
        }
    }
}