package model;

import exceptions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class models a graph using an Adjacency list
 *
 * @param <V> Abstract data type which represents an object from a natural problem that is going to be modeled as a vertex in a graph representation of the problem
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 * TODO: Check contracts grammar/spelling.
 */
public class AdjacencyListGraph<V> implements IGraph<V> {

    /**
     * Map with all the vertices within the graph.
     * Key of the map is the Edge and Value is the position of the vertex in the adjacencyList
     */
    private Map<V, Integer> vertices;

    /**
     * A list for each Edge within the graph which has a list with all its adjacent Vertices
     */
    private List<List<V>> adjacencyLists;

    /**
     * Property that say if a graph is directed or not
     */
    private boolean isDirected;

    /**
     * TODO
     */
    private boolean isWeighted;

    /**
     * Basic constructor that is initialized with default values
     */
    public AdjacencyListGraph() {
        initialize();
    }

    /**
     * Constructor that gets the value for "isDirected" attribute.
     * True if the graph is Directed or false if it's Indirected
     *
     * @param id value to set "isDirected"
     */
    public AdjacencyListGraph(boolean id, boolean iw) {
        initialize();
        isDirected = id;
        isWeighted = iw;
    }

    /**
     * Initializes all the data structures for this graph.
     * Set "isDirected" attribute in false
     */
    private void initialize() {
        isDirected = false;
        isWeighted = false;
        adjacencyLists = new ArrayList<>();
        vertices = new HashMap<>();
    }

    /**
     * TODO
     *
     * @param u The new vertex to be added
     * @return
     * @throws ElementAlreadyPresentException
     */
    @Override
    public boolean addVertex(V u) throws ElementAlreadyPresentException {
        boolean added = false;
        // Check if the vertex is not on the map already
        if (!searchVertex(u)) {
            @SuppressWarnings("unchecked")
            // Create a new empty list for that vertex
                    List<V> vList = (List<V>) new ArrayList<Object>();
            // Get the position for this new vertex
            int index = adjacencyLists.size();
            // Add the vertex to the map
            vertices.put(u, index);
            // Add the vertex empty list to the adjacencyLists
            adjacencyLists.add(vList);
            // Change the value to true indicating that it was possible to add the vertex
            added = true;
        }
        return added;
    }

    /**
     * checks if a vertex is within the graph
     *
     * @param v Edge to be searched
     * @return True if found or false if not
     */
    private boolean searchVertex(V v) {
        return vertices.containsValue(v);
    }

    /**
     * TODO
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @return
     * @throws WrongEdgeTypeException
     */
    @Override
    public boolean addEdge(V u, V v) throws WrongEdgeTypeException, ElementNotFoundException {
        // TODO Auto-generated method stub

        int ValueU = vertices.get(u);
        int ValueV = vertices.get(v);

        if (!isDirected) {
            adjacencyLists.get(ValueU).add(v);
            adjacencyLists.get(ValueV).add(u);
        } else {

            adjacencyLists.get(ValueU).add(v);
        }
        return false;//TODO: Finish implementation
    }

    /**
     * TODO
     *
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @param w is the weight of the edge
     * @return
     * @throws WrongEdgeTypeException
     */
    @Override
    public boolean addEdge(V u, V v, double w) throws WrongEdgeTypeException, ElementNotFoundException {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * TODO
     *
     * @param u A vertex to be removed of the graph
     * @return
     * @throws ElementNotFoundException
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

            return true;

        } else {
            return false;
        }
    }

    /**
     * TODO
     *
     * @param u A vertex connected with V
     * @param v A vertex connected with U
     * @return
     * @throws ElementNotFoundException
     */
    @Override
    public boolean removeEdge(V u, V v) throws ElementNotFoundException {
        // TODO Auto-generated method stub
        return false;
    }//TODO: error por no contener a u, v o el edge.

    /**
     * TODO
     *
     * @param u The vertex to be consulted its adjacent vertices
     * @return
     * @throws ElementNotFoundException
     */
    @Override
    public List<V> vertexAdjacent(V u) throws ElementNotFoundException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * TODO
     *
     * @param u Is a vertex
     * @param v Is a vertex
     * @return
     * @throws ElementNotFoundException
     */
    @Override
    public boolean areConnected(V u, V v) throws ElementNotFoundException {

        int uValor = vertices.get(u);
        int vValor = vertices.get(v);

//		return adjacencyLists.get(uValor).contains(v) || adjacencyLists.get(uValor).contains(v);
//		This return exists in case there is no need of being specific about the direction

        if (isDirected) {
            return adjacencyLists.get(uValor).contains(v);
            // this returns if index connected and directed to v
        } else {
            return adjacencyLists.get(uValor).contains(v) && adjacencyLists.get(vValor).contains(u);
            // in case the graph is not connected then both should be connected to each other
        }

    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public double[][] weightMatrix() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public boolean isDirected() {
        // TODO Auto-generated method stub
        return isDirected;
    }

    /**
     * TODO
     * @return
     */
    @Override
    public boolean isWeighted() {//TODO: Implement
        return true;
    }

//    /**
//     * @return The graph. A list with lists of vertices and its adjacent vertices
//     */
//    public List<List<V>> getAdjacencyList() {
//        return adjacencyLists;
//    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public int getIndex(V u) throws ElementNotFoundException {
        return vertices.get(u);
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public int getVertexSize() {
        return vertices.size();
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public Map<V, Integer> getVertices(){
        return vertices;
    }

    /**
     * TODO
     *
     * @return
     */
    @Override
    public ArrayList<Edge> getEdges() {
        return null;
    }
}
