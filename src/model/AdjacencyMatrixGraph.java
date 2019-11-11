package model;

import java.util.*;

/**
 * An Object that models a graph using an Adjacency Matrix.
 *
 * @param <V> the type of vertex in the graph
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 */
public class AdjacencyMatrixGraph<V> implements IGraph<V> {

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
    private Map<Integer, V> verticesMap;

    /**
     * A Map that uses any vertex as a key to access its corresponding index in the matrix.
     */
    private Map<V, Integer> verticesIndicesMap;

    /**
     * A Set that contains ordered, non-duplicate Integers of empty row/columns in the matrix whose values are lesser
     * than the logical size.
     */
    private NavigableSet<Integer> emptySlots = new TreeSet<>();//TODO: relocate initialization

    /**
     * TODO
     */
    private NavigableSet<Integer> usedIndices = new TreeSet<>();

    /**
     * Only for non-directed graphs.
     * TODO
     */
    private ArrayList<Vertex> connections = new ArrayList<>();

    /**
     * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
     * verticesMap to their indices in the matrix and indices in the matrix to their verticesMap.
     */
    public AdjacencyMatrixGraph() {
        initialize(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
     * verticesMap to their indices in the matrix and indices in the matrix to their verticesMap. The graph represented by the
     * matrix is directed if id is true.
     *
     * @param id true if the graph is directed.
     * @param iw true if the graph is weighted.
     */
    public AdjacencyMatrixGraph(boolean id, boolean iw) {
        initialize(DEFAULT_CAPACITY);
        isDirected = id;
        isWeighted = iw;
    }

    /**
     * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
     * verticesMap to their indices in the matrix and indices in the matrix to their verticesMap.
     *
     * @param capacity the initial size of the adjacency matrix
     */
    public AdjacencyMatrixGraph(int capacity) {
        initialize(capacity);
    }

    /**
     * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
     * verticesMap to their indices in the matrix and indices in the matrix to their verticesMap. The graph represented by the
     * matrix is directed if id is true.
     *
     * @param id       true if the graph is directed.
     * @param iw       true if the graph is weighted.
     * @param capacity the initial size of the adjacency matrix
     */
    public AdjacencyMatrixGraph(boolean id, boolean iw, int capacity) {
        initialize(capacity);
        isDirected = id;
        isWeighted = iw;
    }

    /**
     * Auxiliary method used by the Constructor to set values to the class' fields. Creates the adjacency matrix.
     *
     * @param capacity the initial size of the adjacency matrix
     */
    private void initialize(int capacity) {
        isDirected = false;
        isWeighted = false;
        size = 0;
        adjacencyMatrix = new double[capacity][capacity];
        verticesMap = new HashMap<>();
        verticesIndicesMap = new HashMap<>();
    }

    /**
     * Adds the given vertex to the graph.
     *
     * @param v The new vertex to be added
     * @return true if the vertex did not already exist in the graph
     */
    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean addVertex(V v) {
        boolean added = false;
        int index;
        if (verticesIndicesMap.get(v) != null) {
            if (emptySlots.isEmpty()) {//No reusable rows/columns in the matrix
                if (size == adjacencyMatrix.length) {//Needs to initialize a bigger array
                    double[][] placeHolder = adjacencyMatrix;
                    int newLength = (int) (adjacencyMatrix.length * GROWTH_FACTOR);
                    adjacencyMatrix = new double[newLength][newLength];
                    for (int i = 0; i < placeHolder.length; i++) {
                        System.arraycopy(placeHolder[i], 0, adjacencyMatrix[i], 0, placeHolder.length);
                    }
                    if (isWeighted) {
                        weightMatrix = new double[newLength][newLength];
                        for (int i = 0; i < placeHolder.length; i++) {
                            System.arraycopy(placeHolder[i], 0, weightMatrix[i], 0, placeHolder.length);
                        }
                    }
                }
                size++;
                index = size - 1;
            } else
                index = emptySlots.pollFirst();//TODO: May assign null?
            verticesMap.put(index, v);
            verticesIndicesMap.put(v, index);
            usedIndices.add(index);
            for (int i = 0; i < 2; i++) {//Sets the weight of all edges from the new vertex to infinity.
                for (int j = 0; j < size; j++) {
                    switch (i) {
                        case 0:
                            weightMatrix[index][j] = Double.MAX_VALUE;
                            break;
                        case 1:
                            weightMatrix[j][index] = Double.MAX_VALUE;
                            break;
                    }
                }
            }
            weightMatrix[index][index] = 0.0;
            added = true;
        }
        return added;
    }

    /**
     * Adds a directed edge from vertex 'u' to vertex 'v' if the graph is directed. Otherwise, adds an edge between
     * verticesMap 'u' and 'v'.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     */
    @Override
    public boolean addEdge(V u, V v) {
        boolean added = false;
        Integer x = verticesIndicesMap.get(u);
        Integer y = verticesIndicesMap.get(v);
        if (x != null && y != null) {
            added = true;
            if (!isDirected) {
                adjacencyMatrix[x][y] = 1;
                adjacencyMatrix[y][x] = 1;
            } else
                adjacencyMatrix[x][y] = 1;
        }
        return added;
    }

    /**
     * Adds a directed edge from vertex 'u' to vertex 'v' if isDirected with weight 'w'. Adds an edge between verticesMap
     * 'u' and 'v' of weight 'w' otherwise.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @param w is the weight of the edge between 'u' and 'v'
     */
    @Override
    public boolean addEdge(V u, V v, double w) {
        boolean added = false;
        Integer x = verticesIndicesMap.get(u);
        Integer y = verticesIndicesMap.get(v);
        if (x != null && y != null) {
            added = true;
            adjacencyMatrix[x][y] = 1;
            weightMatrix[x][y] = w;
            connections.add(new Vertex(x, y, w));
            if (!isDirected) {
                adjacencyMatrix[y][x] = 1;
                weightMatrix[y][x] = w;
            }
        }
        return added;
    }

    /**
     * Attempts to remove vertex 'v' from the graph.
     *
     * @param v vertex to be removed from the graph
     * @return true if 'v' exists in the graph. False otherwise
     */
    @Override
    public boolean removeVertex(V v) {
        boolean removed = false;
        Integer position = verticesIndicesMap.get(v);
        if (position != null) {
            verticesMap.remove(position);
            verticesIndicesMap.remove(v);
            emptySlots.add(position);
            for (int i = 0; i < size; i++)
                adjacencyMatrix[position][i] = 0;
            for (int i = 0; i < size; i++)
                adjacencyMatrix[i][position] = 0;
            removed = true;
        }
        return removed;
    }

    /**
     * Removes a directed edge from vertex 'u' to vertex 'v' if the graph is directed. Otherwise, removes an undirected
     * edge between verticesMap 'u' and 'v'.
     *
     * @param u vertex connected with V
     * @param v vertex connected with U
     */
    @Override
    public boolean removeEdge(V u, V v) {
        boolean removed = false;
        Integer x = verticesIndicesMap.get(u);
        Integer y = verticesIndicesMap.get(v);
        if (x != null && y != null) {
            removed = true;
            if (!isDirected) {
                adjacencyMatrix[x][y] = 0;
                adjacencyMatrix[x][y] = 0;
                weightMatrix[x][y] = 0.0;
                weightMatrix[y][x] = 0.0;
            } else {
                adjacencyMatrix[x][y] = 0;
                weightMatrix[x][y] = 0.0;
            }
        }
        return removed;
    }

    /**
     * Returns a List<V> containing all verticesMap adjacent to 'v'.
     *
     * @param v vertex whose adjacent verticesMap are to be consulted
     * @return a List<V> containing all verticesMap adjacent to 'v'
     */
    @Override
    public List<V> vertexAdjacent(V v) {
        Integer position = verticesIndicesMap.get(v);
        List<V> adjacentVertices = null;
        if (position != null) {
            Set<Integer> adjacentVerticesPositions = new HashSet<>();
            for (int i = 0; i < size; i++)
                if (adjacencyMatrix[position][i] != 0)//Vertex at position i is adjacent
                    adjacentVerticesPositions.add(i);
            if (!isDirected) //Only necessary to execute if graph is not directed
                for (int i = 0; i < size; i++)
                    if (adjacencyMatrix[i][position] != 0)//Vertex at position i is adjacent
                        adjacentVerticesPositions.add(i);
            adjacentVertices = new ArrayList<>();
            for (Integer key : adjacentVerticesPositions)
                adjacentVertices.add(verticesMap.get(key));
        }
        return adjacentVertices;
    }

    /**
     * If the graph is undirected, determines if verticesMap 'u' and 'v' share an edge. Otherwise, determines if there is
     * a directed edge from 'u' to 'v' and 'v' to 'u'.
     *
     * @param u a vertex
     * @param v a vertex
     * @return true if and only if said edge exists
     */
    @Override
    public boolean areConnected(V u, V v) {
        Integer x = verticesIndicesMap.get(u);
        Integer y = verticesIndicesMap.get(v);

        // return adjacencyMatrix[uValor][vValor] == 1 && adjacencyMatrix[vValor][uValor] == 1;
        // This return exists in case there is no need of being specific about the direction
        if (x != null && y != null) {
            if (isDirected) {
                return adjacencyMatrix[x][y] == 1;
                // this returns if u connected and directed to v
            } else {
                return adjacencyMatrix[x][y] == 1 && adjacencyMatrix[x][y] == 1;
                // in case the graph is not connected then both should be connected to each other
            }
        } else
            return false;//TODO: Revisar buenas practicas.
    }

    /**
     * Returns the weight matrix containing the weights of every edge, directed or not, between all verticesMap in the
     * graph.
     *
     * @return the matrix containing all weights in the graph
     */
    @Override
    public double[][] weightMatrix() {
        return weightMatrix;
    }

    /**
     * Returns whether the graph is directed.
     *
     * @return true if and only if graph is directed
     */
    @Override
    public boolean isDirected() {
        return isDirected;
    }

    /**
     * Gives the amount of vertices in the graph.
     *
     * @return an int with said amount.
     */
    @Override
    public int getVertexSize() {
        return verticesMap.size();
    }

    /**
     * Returns the index of vertex 'u' in the matrix.
     *
     * @param u the vertex whose index will be returned
     * @return the index of the vertex in the matrix
     */
    @Override
    public int getIndex(V u) {
        return verticesIndicesMap.get(u) != null ? verticesIndicesMap.get(u) : -1;
    }

//    /**
//     * TODO
//     *
//     * @return
//     */
//    public int getSize() {
//        return size;
//    }

//    /**
//     * TODO
//     *
//     * @param i
//     * @return
//     */
//    public V getVertex(double i) {
//        return verticesMap.get((int) i);
//    }

//    /**
//     * TODO
//     *
//     * @return
//     */
//    public NavigableSet<Integer> getUsedIndices() {
//        return usedIndices;
//    }
//
//    /**
//     * @return
//     */
//    public ArrayList<Vertex> getConnections() {
//        return connections;
//    }

    /**
     * TODO
     */
    public class Vertex implements Comparable {
        int u;
        int v;
        double weight;

        Vertex(int u, int v, double weight) {
            this.u = u;
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(Object o) {
            double parameter = (double) o;
            if (weight > parameter) {
                return 1;
            } else return weight < parameter ? -1 : 0;
        }
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public Map<Integer, V> getVertices() {
        return verticesMap;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public Map<V, Integer> getIndices() {
        return verticesIndicesMap;
    }
}