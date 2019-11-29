package contests;

import java.util.*;

import java.io.*;

/**
 * Class meant to be used in the virtual judge for the UVa 11492 - Babel problem
 */
public class Babel {
    private static BufferedReader rd = new BufferedReader(new InputStreamReader(System.in));

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
                    /*if (edge < graph.weightMatrix()[index1][index2]) {
                        graph.removeEdge(lang1, lang2);
                        graph.addEdge(lang1, lang2, edge);
                    }*/
                } else{}
                   // graph.addEdge(lang1, lang2, edge);
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
         * Removes an edge within the graph
         * <pre> U and V are within the graph
         * @param u A vertex connected with V
         * @param v A vertex connected with U
         */
        void removeEdge(V u, V v) throws ElementNotFoundException;

        /**
         * Check if U and V are connected
         * <pre> U and V are within the graph
         * @param u Is a vertex
         * @param v Is a vertex
         * @return True if U and V are connected or false if they're not
         */
        boolean areConnected(V u, V v) throws ElementNotFoundException;

        /**
         * <pre> The graph is weighted
         * @return A matrix with the weight of all the connections
         */
        double[][] weightMatrix();

        /**
         * Returns the index of vertex 'u' in the matrix.
         *
         * @param u the vertex whose index will be returned
         * @return the index of the vertex in the matrix
         * @throws ElementNotFoundException if 'u' is not in the graph
         */
        int getIndex(V u) throws ElementNotFoundException;

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
            int index;
            if (verticesIndices.get(u) == null) {
                if (emptySlots.isEmpty()) {//No reusable rows/columns in the matrix
                    if (size == adjacencyMatrix.length) {//Needs to initialize a bigger array
                        int newLength = (int) (adjacencyMatrix.length * GROWTH_FACTOR);

                        double[][] placeHolder = new double[newLength][newLength];//Temporarily holds the data of weightMatrix.
                        for (int i = 0; i < adjacencyMatrix.length; i++) {
                            for (int j = 0; j < adjacencyMatrix.length; j++)
                                placeHolder[i][j] = adjacencyMatrix[i][j];
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
         * Removes the edge from 'u' to 'v' if present. If the graph is undirected, also removes the edge
         * from 'v' to 'u'.
         *
         * @param u vertex from which the edge originates
         * @param v vertex to which the edge arrives
         * @throws ElementNotFoundException if either 'u' or 'v' don't belong to the graph
         */
        @Override
        public void removeEdge(V u, V v) throws ElementNotFoundException {
            boolean removed = false;
            Integer x = verticesIndices.get(u);
            Integer y = verticesIndices.get(v);
            if (x != null && y != null) {
                if (adjacencyMatrix[x][y] != 0) {
                    removed = true;
                    adjacencyMatrix[x][y] = 0;
                    weightMatrix[x][y] = Double.MAX_VALUE;
                    if (!isDirected) {
                        adjacencyMatrix[y][x] = 0;
                        weightMatrix[y][x] = Double.MAX_VALUE;
                    }

                    edges.get(u).removeIf(next -> next.get(v) != null);

                    if (isDirected) {
                        for (Iterator<List<Map<V, Double>>> iterator = edges.values().iterator(); iterator.hasNext(); ) {
                            List<Map<V, Double>> next = iterator.next();
                            for (Map<V, Double> edgeMap : next)
                                if (edgeMap.get(u) != null)
                                    iterator.remove();
                        }
                    }
                }
            } else {
                if (x == null)
                    throw new ElementNotFoundException("First parameter was not found in graph");
                else
                    throw new ElementNotFoundException("Second parameter was not found in graph");
            }
        }

        /**
         * If the graph is undirected, determines if vertices 'u' and 'v' share an edge. Otherwise, determines if there is
         * a directed edge from 'u' to 'v' and 'v' to 'u'.
         *
         * @param u a vertex
         * @param v a vertex
         * @return true if and only if said edge exists
         * @throws ElementNotFoundException if either 'u' or 'v' are not in the graph
         */
        @Override
        public boolean areConnected(V u, V v) throws ElementNotFoundException {
            Integer x = verticesIndices.get(u);
            Integer y = verticesIndices.get(v);

            // This return exists in case there is no need of being specific about the direction
            if (x != null && y != null) {
                return adjacencyMatrix[x][y] == 1;
                // this returns if index connected and directed to v
            } else {
                if (x == null)
                    throw new ElementNotFoundException("First parameter was not found in graph");
                else
                    throw new ElementNotFoundException("Second parameter was not found in graph");
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

        /**
         * Returns the index of vertex 'u' in the matrix.
         *
         * @param u the vertex whose index will be returned
         * @return the index of the vertex in the matrix
         * @throws ElementNotFoundException if 'u' is not in the graph
         */
        @Override
        public int getIndex(V u) throws ElementNotFoundException {
            if (verticesIndices.get(u) == null)
                throw new ElementNotFoundException("Parameter not found in graph");
            return verticesIndices.get(u);
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
         * An algorithm based on Dijkstra's approach to finding the shortest path from a given vertex to all vertices in
         * the graph.
         *
         * @param <V> type that represents a vertex within the graph
         * @param g   graph to traverse
         * @param s   vertex from which to begin traversing
         * @return a matrix of two columns which are both the distance from the origin to 'i' (cell [i][0]) and the
         * parent of 'i' through which the shortest path is achieved (cell [i][1])
         * @throws ElementNotFoundException if 's' does not belong to the graph
         * @throws WrongEdgeTypeException   if a negative edge is found
         */
        <V> double[][] dijkstra(IGraph<V> g, V s) throws ElementNotFoundException, WrongEdgeTypeException {
            double[][] w = g.weightMatrix();
            int logicalSize = g.getVertexSize();
            double[][] shortestPath = new double[logicalSize][2];
            int indexOfS = g.getIndex(s);

            //----------------------------Comienzo segun Cormen----------------------------//
            initializeSingleSource(indexOfS, shortestPath, w);
            Queue<Double[]> q = new PriorityQueue<>(logicalSize, new Comparator<Double[]>() {
                @Override
                public int compare(Double[] weight1, Double[] weight2) {
                    return weight1[1].compareTo(weight2[1]);
                }
            });
            for (int i = 0; i < logicalSize; i++) {
                if (indexOfS != i)
                    q.add(new Double[]{(double) i, w[indexOfS][i]});
            }

            while (!q.isEmpty()) {
                Double[] u = q.poll();//extract-Min
                if (u[1] < 0)
                    throw new WrongEdgeTypeException("Graph contains a negative edge");
                int indexOfU = u[0].intValue();
                for (int v = 0; v < logicalSize; v++)
                    if (w[indexOfU][v] < Double.MAX_VALUE)//u shares and edge with v
                        relax(shortestPath, q, u, v, w);
            }
            return shortestPath;
        }

        /**
         * Auxiliary method of Dijkstra's which checks for illegal, negative edges and sets the default values of
         * the matrix returned by Dijkstra.
         *
         * @param indexOfS     index used by the graph of the source node
         * @param shortestPath matrix which is to be returned by Dijkstra
         * @param w            weight matrix of the graph
         * @throws WrongEdgeTypeException if a negative edge is found
         */
        private void initializeSingleSource(int indexOfS, double[][] shortestPath, double[][] w) throws WrongEdgeTypeException {
            for (int i = 0; i < shortestPath.length; i++) {
                if (w[indexOfS][i] < 0.0)
                    throw new WrongEdgeTypeException("Graph contains a negative edge");
                shortestPath[i][0] = w[indexOfS][i];
                shortestPath[i][1] = indexOfS;
            }
            shortestPath[indexOfS][0] = 0.0;//Distance from s to s.
            shortestPath[indexOfS][1] = indexOfS;//Predecessor of s.
        }

        /**
         * Auxiliary method of Dijkstra's which relaxes all edges from vertex 'u'.
         *
         * @param shortestPath the temporary solution to Dijkstra. May be modified by this method
         * @param q            priority queue of edges sorted by their increasing value
         * @param u            first element of the given priority queue which contains both its index in the graph and the distance
         *                     to it from the source node
         * @param indexOfV     index of the vertex whose distance is to be compared
         * @param w            weight matrix of the graph
         */
        private void relax(double[][] shortestPath, Queue<Double[]> q, Double[] u, int indexOfV, double[][] w) {//Pre: s and index exist in the graph.
            int indexOfU = u[0].intValue();
            double suDistance = shortestPath[indexOfU][0];
            double uvDistance = w[indexOfU][indexOfV];
            double svDistance = shortestPath[indexOfV][0];
            if (suDistance < Double.MAX_VALUE && uvDistance < Double.MAX_VALUE) {
                if (suDistance + uvDistance < svDistance) {//Distance from s to index + distance from index to v < distance from s to v.
                    shortestPath[indexOfV][0] = suDistance + uvDistance;
                    shortestPath[indexOfV][1] = indexOfU;
                    q.add(new Double[]{(double) indexOfV, shortestPath[indexOfV][0]});
                }
            }
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