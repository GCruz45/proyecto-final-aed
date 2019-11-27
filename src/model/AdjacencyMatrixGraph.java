package model;

import exceptions.*;

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
     * vertices to their indices in the matrix and indices in the matrix to their vertices.
     */
    public AdjacencyMatrixGraph() {
        initialize(DEFAULT_CAPACITY);
    }

    /**
     * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
     * vertices to their indices in the matrix and indices in the matrix to their vertices. The graph represented by the
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
     * vertices to their indices in the matrix and indices in the matrix to their vertices.
     *
     * @param capacity the initial size of the adjacency matrix
     */
    public AdjacencyMatrixGraph(int capacity) {
        initialize(capacity);
    }

    /**
     * Constructs a new, empty matrix of double values of default length, along with two Map objects that interconnect
     * vertices to their indices in the matrix and indices in the matrix to their vertices. The graph represented by the
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
        weightMatrix = new double[capacity][capacity];
        vertices = new HashMap<>();
        verticesIndices = new HashMap<>();
        emptySlots = new TreeSet<>();
        edges = new HashMap<>();
    }

    /**
     * Adds the given vertex to the graph.
     *
     * @param u the new vertex to be added
     * @return true if the vertex did not already exist in the graph
     * @throws ElementAlreadyPresentException if u is already present in the graph
     */
    @SuppressWarnings("ConstantConditions, unchecked")
    @Override
    public boolean addVertex(V u) throws ElementAlreadyPresentException {
        boolean added;
        int index;
        if (verticesIndices.get(u) == null) {
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
                index = emptySlots.pollFirst();
            vertices.put(index, new Vertex(index, u));
            verticesIndices.put(u, index);
            edges.put(u, new ArrayList<>());
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
        } else
            throw new ElementAlreadyPresentException("Element is already present");
        return added;
    }

    /**
     * Adds a directed edge from vertex 'u' to vertex 'v' if the graph is directed. Otherwise, adds an edge between
     * vertices 'u' and 'v'. Admits loops.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @return true if said edge could be added
     * @throws WrongEdgeTypeException if adding a weighted edge to an unweighted graph
     */
    @Override
    public boolean addEdge(V u, V v) throws WrongEdgeTypeException, ElementNotFoundException {
        if (isWeighted)
            throw new WrongEdgeTypeException("Tried to add an unweighted edge to a weighted graph.");
        Integer x = verticesIndices.get(u);
        Integer y = verticesIndices.get(v);
        if (x != null && y != null) {
            Map<V, Double> mapOfV = new HashMap<>();
            mapOfV.put(v, Double.MAX_VALUE);
            edges.get(u).add(mapOfV);
            adjacencyMatrix[x][y] = 1;
            if (!isDirected) {
                adjacencyMatrix[y][x] = 1;
                Map<V, Double> mapOfU = new HashMap<>();
                mapOfU.put(u, Double.MAX_VALUE);
                edges.get(v).add(mapOfU);
            }
        } else {
            if (x == null)
                throw new ElementNotFoundException("First element not found in graph");
            else
                throw new ElementNotFoundException("Second element not found in graph");
        }
        return true;
    }

    /**
     * Adds a directed edge from vertex 'u' to vertex 'v' if isDirected with weight 'w'. Adds an edge between vertices
     * 'u' and 'v' of weight 'w' otherwise. Admits loops.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @param w is the weight of the edge between 'index' and 'v'
     * @return true if said edge could be added
     * @throws WrongEdgeTypeException if adding an uweighted edge to a weighted graph
     */
    @Override
    public boolean addEdge(V u, V v, double w) throws WrongEdgeTypeException, ElementNotFoundException {
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
        return true;
    }

    /**
     * Attempts to remove vertex 'u' from the graph.
     *
     * @param u vertex to be removed from the graph
     * @return true if 'u' exists in the graph. False otherwise
     * @throws ElementNotFoundException if the given vertex is not found
     */
    @Override
    public boolean removeVertex(V u) throws ElementNotFoundException {
        Integer position = verticesIndices.get(u);
        if (position != null) {
            vertices.remove(position);
            verticesIndices.remove(u);
            emptySlots.add(position);

            edges.remove(u);
            for (List<Map<V, Double>> list : edges.values()) {
                for (Map<V, Double> edge : list)
                    edge.remove(u);
            }

            for (int i = 0; i < size; i++) {//Removes 'u' from both matrices.
                adjacencyMatrix[position][i] = 0;
                adjacencyMatrix[i][position] = 0;
                weightMatrix[position][i] = Double.MAX_VALUE;
                weightMatrix[i][position] = Double.MAX_VALUE;
            }
            size--;
        } else
            throw new ElementNotFoundException("Element is not in the graph");
        return true;
    }

    /**
     * Removes the edge from 'u' to 'v' if present. If the graph is undirected, also removes the edge
     * from 'v' to 'u'.
     *
     * @param u vertex from which the edge originates
     * @param v vertex to which the edge arrives
     * @return true if said edge/s was/were removed
     * @throws ElementNotFoundException if either 'u' or 'v' don't belong to the graph
     */
    @Override
    public boolean removeEdge(V u, V v) throws ElementNotFoundException {
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
        return removed;
    }

    /**
     * Returns a List<V> containing all vertices adjacent to 'v'.
     *
     * @param v vertex whose adjacent vertices are to be consulted
     * @return a List<V> containing all vertices adjacent to 'v'
     * @throws ElementNotFoundException if 'u' doesn't belong to the graph
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<V> vertexAdjacent(V v) throws ElementNotFoundException {
        Integer position = verticesIndices.get(v);
        List<V> adjacentVertices;
        if (position != null) {
            adjacentVertices = new LinkedList<>();
            for (int i = 0; i < size; i++)
                if (adjacencyMatrix[position][i] == 1)
                    adjacentVertices.add((V) vertices.get(i).info);
        } else
            throw new ElementNotFoundException("Parameter not found in graph");
        return adjacentVertices;
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
     * Returns whether the graph is directed.
     *
     * @return true if and only if graph is directed
     */
    @Override
    public boolean isDirected() {
        return isDirected;
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
     * Gives the amount of vertices in the graph.
     *
     * @return an int with said amount.
     */
    @Override
    public Map<V, Integer> getVertices() {
        return verticesIndices;
    }

    /**
     * Gives a Map that pairs the starting vertex with a List of maps that represent all vertices
     * it arrives to along with the value of its edge. If the graph is unweighted, the edges' value
     * is set to Double.MAX_VALUE by default.
     *
     * @return a Map that represents a vertex and all edges that originate from itself
     */
    @Override
    public Map<V, List<Map<V, Double>>> getEdges() {
        return edges;
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