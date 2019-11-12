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
     * TODO
     */
    private Map<V, Integer> verticesIndices;

    /**
     * TODO
     */
    private ArrayList<Edge> edges;

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
        vertices = new HashMap<>();
        emptySlots = new TreeSet<>();
        edges = new ArrayList<>();
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
        boolean added = false;
        int index;
        if (verticesIndices.get(u) != null) {
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
            vertices.put(index, new Vertex(index, u));
            verticesIndices.put(u, index);
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
     * vertices 'u' and 'v'. Admits loops.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @return
     * @throws WrongEdgeTypeException if attempting to add to a weighted graph
     */
    @Override
    public boolean addEdge(V u, V v) throws WrongEdgeTypeException, ElementNotFoundException {
        if (!isWeighted)
            throw new WrongEdgeTypeException("Tried to add an unweighted edge to a weighted graph.");
        boolean added = false;
        Integer x = verticesIndices.get(u);
        Integer y = verticesIndices.get(v);
        if (x != null && y != null) {
            added = true;
            edges.add(new Edge(x, y));
            adjacencyMatrix[x][y] = 1;
            if (!isDirected) {
                adjacencyMatrix[y][x] = 1;
            }
        }
        return added;
    }

    /**
     * Adds a directed edge from vertex 'u' to vertex 'v' if isDirected with weight 'w'. Adds an edge between vertices
     * 'u' and 'v' of weight 'w' otherwise. Admits loops.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @param w is the weight of the edge between 'index' and 'v'
     * @return
     * @throws WrongEdgeTypeException
     */
    @Override
    public boolean addEdge(V u, V v, double w) throws WrongEdgeTypeException, ElementNotFoundException {//TODO: Modificar para usar ElementNotfound en vez del if()
        if (!isWeighted)
            throw new WrongEdgeTypeException("Tried to add a weighted edge to an unweighted graph.");
        boolean added = false;
        Integer x = verticesIndices.get(u);
        Integer y = verticesIndices.get(v);
        if (x != null && y != null) {
            added = true;
            edges.add(new Edge(x, y, w));
            adjacencyMatrix[x][y] = 1;
            weightMatrix[x][y] = w;
            if (!isDirected) {
                adjacencyMatrix[y][x] = 1;
                weightMatrix[y][x] = w;
            }
        }
        return added;
    }

    /**
     * Attempts to remove vertex 'u' from the graph.
     *
     * @param u vertex to be removed from the graph
     * @return true if 'u' exists in the graph. False otherwise
     * @throws ElementNotFoundException
     */
    @Override
    public boolean removeVertex(V u) throws ElementNotFoundException {
        boolean removed = false;
        Integer position = verticesIndices.get(u);
        if (position != null) {
            vertices.remove(position);
            verticesIndices.remove(u);
            emptySlots.add(position);
            for (int i = 0; i < edges.size(); i++) {//Removes from edges list.
                Edge toRemove = edges.get(i);
                if (toRemove.u == position || toRemove.v == position)
                    edges.remove(toRemove);
            }
            for (int i = 0; i < size; i++) {//Removes from its row position in both matrices.
                adjacencyMatrix[position][i] = 0;
                weightMatrix[position][i] = Double.MIN_VALUE;
            }
            for (int i = 0; i < size; i++) {//Removes from its column position in both matrices.
                adjacencyMatrix[i][position] = 0;
                weightMatrix[i][position] = Double.MIN_VALUE;
            }
            removed = true;
        }
        return removed;
    }

    /**
     * Removes a directed edge from vertex 'u' to vertex 'v' if the graph is directed. Otherwise, removes an undirected
     * edge between vertices 'u' and 'v'.
     *
     * @param u vertex connected with V
     * @param v vertex connected with U
     * @return
     * @throws ElementNotFoundException
     */
    @Override
    public boolean removeEdge(V u, V v) throws ElementNotFoundException {
        boolean removed = false;
        Integer x = verticesIndices.get(u);
        Integer y = verticesIndices.get(v);
        if (x != null && y != null) {
            removed = true;
            adjacencyMatrix[x][y] = 0;
            weightMatrix[x][y] = Double.MIN_VALUE;
            if (!isDirected) {
                adjacencyMatrix[y][x] = 0;
                weightMatrix[y][x] = Double.MIN_VALUE;
            }
            for (int i = 0; i < edges.size(); i++) {//Removes from edges list.
                Edge toRemove = edges.get(i);
                if (isDirected) {
                    if (toRemove.u == x && toRemove.v == y)
                        edges.remove(toRemove);
                } else if ((toRemove.u == x && toRemove.v == y) || (toRemove.u == y && toRemove.v == x))
                    edges.remove(toRemove);
            }
        }
        return removed;
    }

    /**
     * Returns a List<V> containing all vertices adjacent to 'v'.
     *
     * @param v vertex whose adjacent vertices are to be consulted
     * @return a List<V> containing all vertices adjacent to 'v'
     * @throws ElementNotFoundException
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<V> vertexAdjacent(V v) throws ElementNotFoundException {
        Integer position = verticesIndices.get(v);
        List<V> adjacentVertices = null;
        if (position != null) {
            adjacentVertices = new LinkedList<>();
            for (int i = 0; i < size; i++) {
                if (adjacencyMatrix[position][i] == 1) {
                    adjacentVertices.add((V) vertices.get(i).info);
                }
            }
        }
        return adjacentVertices;
    }

    /**
     * If the graph is undirected, determines if vertices 'u' and 'v' share an edge. Otherwise, determines if there is
     * a directed edge from 'u' to 'v' and 'v' to 'u'.
     *
     * @param u a vertex
     * @param v a vertex
     * @return true if and only if said edge exists
     * @throws ElementNotFoundException
     */
    @Override
    public boolean areConnected(V u, V v) throws ElementNotFoundException {
        Integer x = verticesIndices.get(u);
        Integer y = verticesIndices.get(v);

        // This return exists in case there is no need of being specific about the direction
        if (x != null && y != null) {
            return adjacencyMatrix[x][y] == 1;
            // this returns if index connected and directed to v
        } else
            return false;
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
     * Gives the amount of vertices in the graph.
     *
     * @return an int with said amount.
     */
    @Override
    public int getVertexSize() {
        return vertices.size();
    }

    /**
     * TODO
     *
     * @return
     * @throws ElementNotFoundException
     */
    @Override
    public Map<V, Integer> getVertices() throws ElementNotFoundException {
        return null;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Returns the index of vertex 'u' in the matrix.
     *
     * @param u the vertex whose index will be returned
     * @return the index of the vertex in the matrix
     * @throws ElementNotFoundException
     */
    @Override
    public int getIndex(V u) throws ElementNotFoundException {
        return verticesIndices.get(u) != null ? verticesIndices.get(u) : -1;
    }
}