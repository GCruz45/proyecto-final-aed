package model;

import exceptions.*;

import java.util.*;

/**
 * This class models a graph using an Adjacency List.
 *
 * @param <V> Abstract data type which represents an object from a natural problem that is going to be modeled as a vertex in a graph representation of the problem
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 */
public class AdjacencyListGraph<V> implements IGraph<V> {

    /**
     * Map with all the vertices within the graph.
     * Key of the map is the Edge and Value is the position of the vertex in the adjacencyList
     */
    private Map<V, Integer> vertices;

    /**
     * A list for each Edge within the graph which has a list with all its adjacent vertices.
     */
    private List<List<V>> adjacencyLists;

    /**
     * Property that indicates if the graph is directed.
     */
    private boolean isDirected;

    /**
     * Property that indicates if the graph is weighted.
     */
    private boolean isWeighted;

    /**
     * A map that pairs each vertex to a list representing all adjacent vertices along with the weight of the edge they share.
     */
    private Map<V, List<Map<V, Double>>> edges;

    /**
     * The edges in this graph.
     */
    private ArrayList<Edge> edgesArray = new ArrayList<>();

    /**
     * Basic constructor that is initialized with default values.
     */
    public AdjacencyListGraph() {
        initialize();
    }

    /**
     * Constructor that gets the value for "isDirected" attribute.
     * True if the graph is Directed or false otherwise.
     *
     * @param id value to set "isDirected"
     */
    public AdjacencyListGraph(boolean id, boolean iw) {
        initialize();
        isDirected = id;
        isWeighted = iw;
    }

    /**
     * Initializes all the data structures used for this graph.
     */
    private void initialize() {
        isDirected = false;
        isWeighted = false;
        adjacencyLists = new ArrayList<>();
        vertices = new HashMap<>();
        edges = new HashMap<>();
    }

    /**
     * Adds the given vertex to the adjacency list at the last position. Throws an exception if and only if said vertex
     * is already in the graph.
     *
     * @param u The new vertex to be added
     * @return true if the vertex was added
     * @throws ElementAlreadyPresentException if the vertex was already present
     */
    @Override
    public boolean addVertex(V u) throws ElementAlreadyPresentException {
        // Check if the vertex is not on the map already
        if (!searchVertex(u)) {
            // Create a new empty list for that vertex
            List<V> vList = new ArrayList<>();
            // Get the position for this new vertex
            int index = adjacencyLists.size();
            // Add the vertex to the map
            vertices.put(u, index);
            // Add the vertex empty list to the adjacencyLists
            adjacencyLists.add(vList);
            //
            edges.put(u, new LinkedList<>());
        } else
            throw new ElementAlreadyPresentException("Vertex already exists in graph");
        return true;
    }

    /**
     * Checks if a given vertex belongs to the graph.
     *
     * @param v edge to be searched
     * @return true if found or false otherwise
     */
    private boolean searchVertex(V v) {
        return vertices.containsKey(v);
    }

    /**
     * Adds an edge from 'u' to 'v'. If the graph is undirected, also adds an
     * edge from 'v' to 'u'.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @return true if said edge could be added
     * @throws WrongEdgeTypeException if adding a weighted edge to an unweighted graph
     */
    @Override
    public boolean addEdge(V u, V v) throws WrongEdgeTypeException, ElementNotFoundException {
        if (isWeighted)
            throw new WrongEdgeTypeException("Tried to add unweighted edge to weighted graph");
        Integer indexU = vertices.get(u);
        Integer indexV = vertices.get(v);
        if (indexU != null && indexV != null) {
            if (!isDirected) {
                adjacencyLists.get(indexV).add(u);
                Map<V, Double> mapOfU = new HashMap<>();
                mapOfU.put(u, Double.MAX_VALUE);
                edges.get(v).add(mapOfU);
            }
            adjacencyLists.get(indexU).add(v);
            Map<V, Double> mapOfV = new HashMap<>();
            mapOfV.put(v, Double.MAX_VALUE);
            edges.get(u).add(mapOfV);
        } else {
            if (indexU == null)
                throw new ElementNotFoundException("First element not found in graph");
            else
                throw new ElementNotFoundException("Second element not found in graph");
        }
        Edge e = new Edge(u, v);
        edgesArray.add(e);
        return true;
    }

    /**
     * Adds an edge from 'u' to 'v' of weight 'w'. If the graph is undirected, also adds an
     * edge from 'v' to 'u' of weight 'w'.
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @param w is the weight of the edge
     * @return true if said edge could be added
     * @throws WrongEdgeTypeException if adding an unweighted edge to a weighted graph.
     */
    @Override
    public boolean addEdge(V u, V v, double w) throws WrongEdgeTypeException, ElementNotFoundException {
        if (!isWeighted)
            throw new WrongEdgeTypeException("Tried to add weighted edge to unweighted graph");
        Integer indexU = vertices.get(u);
        Integer indexV = vertices.get(v);
        if (indexU != null && indexV != null) {
            if (!isDirected) {
                adjacencyLists.get(indexV).add(u);
                Map<V, Double> mapOfU = new HashMap<>();
                mapOfU.put(u, w);
                edges.get(v).add(mapOfU);
            }
            adjacencyLists.get(indexU).add(v);
            Map<V, Double> mapOfV = new HashMap<>();
            mapOfV.put(v, w);
            edges.get(u).add(mapOfV);
        } else {
            if (indexU == null)
                throw new ElementNotFoundException("First element not found in graph");
            else
                throw new ElementNotFoundException("Second element not found in graph");
        }
        Edge e = new Edge(u, v, w);
        edgesArray.add(e);return true;
    }

    /**
     * Removes the given vertex from the graph.
     *
     * @param u the vertex to be removed
     * @return true if the vertex was removed
     * @throws ElementNotFoundException if the given vertex is not found
     */
    @SuppressWarnings("unlikely-arg-type")
    @Override
    public boolean removeVertex(V u) throws ElementNotFoundException {
        // first looks if the vertex exists
        if (vertices.containsKey(u)) {

            // remove the existing list which represents the adjacent vertices of the vertex to remove
            adjacencyLists.remove(vertices.get(u));

            // remove any existing connection to the vertex
            for (int i = 0; i < adjacencyLists.size(); i++) {
                if (adjacencyLists.get(i).contains(u)) adjacencyLists.get(i).remove(i);
            }

            // removes the vertex form the map
            vertices.remove(u);
        } else
            throw new ElementNotFoundException("Parameter not present in graph");
        return true;
    }

    /**
     * Removes the edge from 'u' to 'v'. If the graph is undirected, also removes the edge
     * from 'v' to 'u'.
     *
     * @param u vertex from which the edge originates
     * @param v vertex to which the edge arrives
     * @return true if said edge/s was/were removed
     * @throws ElementNotFoundException if either 'u' or 'v' don't belong to the graph
     */
    @Override
    public boolean removeEdge(V u, V v) throws ElementNotFoundException {
        Integer indexOfU = vertices.get(u);
        Integer indexOfV = vertices.get(v);

        if (indexOfU == null)
            throw new ElementNotFoundException("First parameter does not belong to graph");
        else if (indexOfV == null)
            throw new ElementNotFoundException("Second parameter does not belong to graph");

        boolean foundEdge = false;

        List<V> currentVertex = adjacencyLists.get(indexOfU);
        for (Iterator<V> iterator = currentVertex.iterator(); iterator.hasNext(); ) {
            V next = iterator.next();
            if (next.equals(v)) {
                iterator.remove();
                foundEdge = true;
            }
        }

        currentVertex = adjacencyLists.get(indexOfV);
        for (Iterator<V> iterator = currentVertex.iterator(); iterator.hasNext(); ) {
            V next = iterator.next();
            if (next.equals(u)) {
                iterator.remove();
                foundEdge = true;
            }
        }

        if (!foundEdge)
            throw new ElementNotFoundException("No edge was found between the given vertices");

        return true;
    }

    /**
     * Returns a list of vertices adjacent to the given node.
     *
     * @param u vertex whose adjacent nodes are to be listed
     * @return list of its adjacent vertices
     * @throws ElementNotFoundException if 'u' doesn't belong to the graph
     */
    @Override
    public List<V> vertexAdjacent(V u) throws ElementNotFoundException {
        if (vertices.get(u) == null)
            throw new ElementNotFoundException("Given vertex not found in graph");

        return adjacencyLists.get(vertices.get(u));
    }

    /**
     * Indicates if there exists an edge starting from 'u' and ending in 'v'. If the graph is directed, both directions
     * are checked.
     *
     * @param u starting vertex
     * @param v ending vertex
     * @return true if said edge exists
     * @throws ElementNotFoundException if either 'u' or 'v' are not in the graph
     */
    @Override
    public boolean areConnected(V u, V v) throws ElementNotFoundException {
        Integer indexU = vertices.get(u);
        Integer indexV = vertices.get(v);

//		return adjacencyLists.get(indexU).contains(v) || adjacencyLists.get(indexU).contains(v);
//		This return exists in case there is no need of being specific about the direction
        if (indexU != null && indexV != null) {
            if (isDirected)
                return adjacencyLists.get(indexU).contains(v);
                // this returns if index connected and directed to v
            else
                return adjacencyLists.get(indexU).contains(v) && adjacencyLists.get(indexV).contains(u);
            // in case the graph is not connected then both should be connected to each other
        } else {
            if (indexU == null)
                throw new ElementNotFoundException("First element not found in graph");
            else
                throw new ElementNotFoundException("Second element not found in graph");
        }
    }

    /**
     * Returns the weight matrix that represents the weight of all edges between vertices in this graph.
     *
     * @return a matrix containing said values
     */
    @Override
    public double[][] weightMatrix() {
        int size = adjacencyLists.size();
        double[][] weightMatrix = new double[size][size];

        //Fills every cell with infinite.
        for (int i = 0; i < size; i++)
            Arrays.fill(weightMatrix[i], Double.MAX_VALUE);
        if (isWeighted) {
            for (V u : edges.keySet()) {
                List<Map<V, Double>> listOfU = edges.get(u);
                int indexOfU = vertices.get(u);
                for (Map<V, Double> edgeOfU : listOfU)
                    for (V v : edgeOfU.keySet()) {
                        int indexOfV = vertices.get(v);
                        weightMatrix[indexOfU][indexOfV] = edgeOfU.get(v);
                    }
                weightMatrix[indexOfU][indexOfU] = 0.0;
            }
        }
        return weightMatrix;
    }

    /**
     * Indicates whether this graph is directed.
     *
     * @return true if this graph is directed. False, otherwise
     */
    @Override
    public boolean isDirected() {
        return isDirected;
    }

    /**
     * Indicates whether this graph is weighted.
     *
     * @return true if this graph is weighted. False, otherwise
     */
    @Override
    public boolean isWeighted() {
        return isWeighted;
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
        Integer indexU = vertices.get(u);
        if (indexU == null)
            throw new ElementNotFoundException("Given vertex was not found in graph");
        return indexU;
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
     * Gives a Map that pairs vertices with their index
     *
     * @return said Map
     */
    @Override
    public Map<V, Integer> getVertices() {
        return vertices;
    }

    @Override
    public List<Edge> getEdgesArray(){
        return edgesArray;
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
}
