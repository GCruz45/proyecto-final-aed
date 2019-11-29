package contests;

import java.util.*;

import java.io.*;

/**
 * Class meant to be used in the virtual judge for the UVa 11492 - Babel problem
 */
class Babel {
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
        String[][] solution;
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

                if (graph.getVertices().get(lang1) == null)//Checks presence of lang1 in the graph.
                    graph.addVertex(lang1);
                if (graph.getVertices().get(lang2) == null)//Checks presence of lang2 in the graph.
                    graph.addVertex(lang2);

                graph.addEdge(lang1, lang2, edge);
            }
            solution = algorithms.dijkstra(graph, startLang);
            if (!solution[graph.getIndex(endLang)][0].equals(""))
                System.out.println(solution[graph.getIndex(endLang)][0]);
            else
                System.out.println("impossivel");
        }
    }

    /**
     * This interface has the minimum and general features that a graph should implement no matter which would be its representation. Does not admit multi-graphs.
     *
     * @param <V> Abstract data type which represents an object from a natural problem that is going to be modeled as a vertex in a graph representation of the problem
     * @author AED Class # 003 // 2019
     * @version 1.0 - 10/2019
     */
    interface IGraph<V> {

        /**
         * Adds a vertex to the graph
         *
         * @param u The new vertex to be added
         */
        void addVertex(V u) throws ElementAlreadyPresentException;

        void addEdge(V u, V v, String w) throws WrongEdgeTypeException, ElementNotFoundException;

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
        LinkedList[][] weightMatrix();

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
    static class AdjacencyMatrixGraph<V> implements IGraph<V> {

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
        private LinkedList<String>[][] weightMatrix = new LinkedList[DEFAULT_CAPACITY][DEFAULT_CAPACITY];

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
        private Map<V, List<Map<V, String>>> edges;

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
         */
        private void initialize() {
            isDirected = false;
            isWeighted = false;
            size = 0;
            vertices = new HashMap<>();
            verticesIndices = new HashMap<>();
            emptySlots = new TreeSet<>();
            edges = new HashMap<>();
            adjacencyMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
            for (int i = 0; i < DEFAULT_CAPACITY; i++)
                for (int j = 0; j < DEFAULT_CAPACITY; j++)
                    weightMatrix[i][j] = new LinkedList<>();
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

                        LinkedList[][] weightPlaceholder = new LinkedList[newLength][newLength];//Temporarily holds the data of weightMatrix.
                        for (int i = 0; i < weightMatrix.length; i++)
                            for (int j = 0; j < weightMatrix.length; j++)
                                weightPlaceholder[i][j] = weightMatrix[i][j];
                        weightMatrix = weightPlaceholder;
                    }
                    size++;
                    index = size - 1;
                } else
                    index = emptySlots.pollFirst();
                vertices.put(index, new Vertex(index, u));
                verticesIndices.put(u, index);
                edges.put(u, new ArrayList<>());
                weightMatrix[index][index] = new LinkedList<>();
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
        @SuppressWarnings("unchecked")
        public void addEdge(V u, V v, String w) throws WrongEdgeTypeException, ElementNotFoundException {
            if (!isWeighted)
                throw new WrongEdgeTypeException("Tried to add a weighted edge to an unweighted graph.");

            Integer x = verticesIndices.get(u);
            Integer y = verticesIndices.get(v);
            if (x != null && y != null) {
                Map<V, String> mapOfV = new HashMap<>();
                mapOfV.put(v, w);
                edges.get(u).add(mapOfV);
                adjacencyMatrix[x][y] = 1;
                weightMatrix[x][y].add(w);
                if (!isDirected) {
                    adjacencyMatrix[y][x] = 1;
                    weightMatrix[y][x].add(w);
                    Map<V, String> mapOfU = new HashMap<>();
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
        public LinkedList<String>[][] weightMatrix() {
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
    static class GraphAlgorithms {
        /**
         * An algorithm based on Dijkstra's approach to finding the shortest path from a given vertex to all vertices in
         * the graph.
         *
         * @param g graph to traverse
         * @param s vertex from which to begin traversing
         * @return a matrix of two columns which are both the distance from the origin to 'i' (cell [i][0]) and the
         * parent of 'i' through which the shortest path is achieved (cell [i][1])
         * @throws ElementNotFoundException if 's' does not belong to the graph
         */
        @SuppressWarnings("unchecked")
        String[][] dijkstra(IGraph<String> g, String s) throws ElementNotFoundException {
            LinkedList<String>[][] w = g.weightMatrix();
            int logicalSize = g.getVertexSize();
            String[][] shortestPath = new String[logicalSize][2];
            int indexOfS = g.getIndex(s);

            //----------------------------Comienzo segun Cormen----------------------------//
            initializeSingleSource(indexOfS, s, shortestPath, w);
            Queue<String[]> q = new PriorityQueue<>(logicalSize, new Comparator<String[]>() {
                @Override
                public int compare(String[] weight1, String[] weight2) {
                    return Integer.compare(weight1[1].length(), weight2[1].length());
                }
            });

            for (int i = 0; i < logicalSize; i++) {
                if (indexOfS != i)
                    for (int j = 0; j < w[indexOfS][i].size(); j++)
                        //Solo se estan agregando los que estan conectados a 's'.
                        // Segundo parametro: Ultima palabra usada para llegar a el.
                        // Tercer parametro: Distancia hasta el.
                        q.add(new String[]{String.valueOf(i), w[indexOfS][i].get(j),
                                String.valueOf(w[indexOfS][i].get(j).length())});
            }

//            for (int i = 0; i < logicalSize; i++) {
//                for (int j = 0; j < logicalSize; j++) {
//                    System.out.print(w[i][j].size() + ", ");

//                    if (!w[i][j].isEmpty()) {
//                        System.out.print(w[i][j].get(0)+", ");
//
//                    }else{
//                        System.out.print(0+", ");
//                    }
//                }
//                System.out.println();
//            }
//            for (int i = 0; i < logicalSize; i++) {
//                System.out.println(shortestPath[i][0]);
//            }
            while (!q.isEmpty()) {
                String[] u = q.poll();//extract-Min
                int indexOfU = Integer.parseInt(u[0]);
                for (int v = 0; v < logicalSize; v++)
                    if (w[indexOfU][v].size() != 0) {//Only vertices with which 'u' shares an edge.
//                         && indexOfU != v && indexOfU != indexOfS
//                        System.out.println(indexOfU == 4 && v ==2);
                        relax(shortestPath, q, u, v, w);
                    }
            }
//            System.out.println("---------");
//            for (int i = 0; i < logicalSize; i++) {
//                System.out.println(shortestPath[i][1]+",");
//            }
            return shortestPath;
        }

        /**
         * Auxiliary method of Dijkstra's which checks for illegal, negative edges and sets the default values of
         * the matrix returned by Dijkstra.
         *
         * @param indexOfS     index used by the graph of the source node
         * @param shortestPath matrix which is to be returned by Dijkstra
         * @param w            weight matrix of the graph
         */
        private void initializeSingleSource(int indexOfS, String wordOfS, String[][] shortestPath, LinkedList<String>[][] w) {
            for (int i = 0; i < shortestPath.length; i++) {
                LinkedList<String> currentLinkedList = w[indexOfS][i];
                String minimum = currentLinkedList.isEmpty() ? "" : currentLinkedList.get(0);
                for (String s : currentLinkedList) {
                    if (s.length() < minimum.length())
                        minimum = s;
                }
                shortestPath[i][0] = minimum.length() > 0 ? String.valueOf(minimum.length()) : "";
                shortestPath[i][1] = minimum.length() > 0 ? minimum : "";
            }
            shortestPath[indexOfS][0] = "";//Distance from s to s.
            shortestPath[indexOfS][1] = "";//Distance from s to s. TODO: Puede haber problemas con esto.
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
        private void relax(String[][] shortestPath, Queue<String[]> q, String[] u, int indexOfV, LinkedList<String>[][] w) {//Pre: s and index exist in the graph.
            int indexOfU = Integer.parseInt(u[0]);
//            System.out.println(Integer.parseInt(shortestPath[indexOfU][0])==Integer.parseInt(u[1]));
            String previousWord = shortestPath[indexOfU][1];
            String previousLetter = previousWord.length() > 0 ? String.valueOf(previousWord.charAt(0)) : "";

            int suDistance = Integer.parseInt(u[2]);
            String uvDistance = null;
            int svDistance = shortestPath[indexOfV][0].length() > 0 ? Integer.parseInt(shortestPath[indexOfV][0]) : Integer.MAX_VALUE;

            for (String edgeUV : w[indexOfU][indexOfV]) {
//                System.out.println(previousLetter+", "+edgeUV);
                boolean areSameLetter = String.valueOf(edgeUV.charAt(0)).equals(previousLetter);
                if (uvDistance == null && !areSameLetter)
                    uvDistance = edgeUV;
                else if (uvDistance != null && !areSameLetter && edgeUV.length() < uvDistance.length())
                    uvDistance = edgeUV;
            }

            if (indexOfU == indexOfV)
                uvDistance = "";
            if (uvDistance != null) {
//                System.out.println("next vertices: " + indexOfU + ", " + indexOfV);
//                System.out.println(suDistance + ", " + uvDistance.length() + ", sv: " + svDistance);
                if (suDistance + uvDistance.length() < svDistance) {//Distance from s to index + distance from index to v < distance from s to v.
//                    System.out.println(suDistance + uvDistance.length());
                    shortestPath[indexOfV][0] = String.valueOf(suDistance + uvDistance.length());
                    shortestPath[indexOfV][1] = uvDistance;
                    q.add(new String[]{String.valueOf(indexOfV), uvDistance, shortestPath[indexOfV][0]});
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

    /**
     * Custom exception to be thrown when a vertex or edge is not found in the graph.
     */
    static class ElementNotFoundException extends Exception {

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
    static class ElementAlreadyPresentException extends Exception {

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
    static class WrongEdgeTypeException extends Exception {

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