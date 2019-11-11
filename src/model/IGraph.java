package model;

import java.util.List;
import java.util.Map;

/**
 * This interface has the minimum and general features that a graph should implement no matter which would be its representation.
 *
 * @param <V> Abstract data type which represents an object from a natural problem that is going to be modeled as a vertex in a graph representation of the problem
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 */
public interface IGraph<V> {

    /**
     * Adds a vertex to the graph
     *
     * @param v The new vertex to be added
     * @return True if was added and false if it was already in the graph
     */
    boolean addVertex(V v);

    /**
     * Adds an edge to the graph
     * If the graph is directed the connection will be from U to V
     * <pre> U and V have to exist in the graph
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     */
    boolean addEdge(V u, V v);

    /**
     * Adds a weighted edge to the graph
     * If the graph is directed the connection will be from U to V
     * <pre> U and V have to exist in the graph
     * @param u a vertex within the graph
     * @param v a vertex within the graph
     * @param w    is the weight of the edge
     */
    boolean addEdge(V u, V v, double w);

    /**
     * Removes a vertex within the graph
     *
     * @param v A vertex to be removed of the graph
     * @return True if the vertex was removed or false if the vertex didn't exist
     */
    boolean removeVertex(V v);

    /**
     * Removes an edge within the graph
     * <pre> U and V are within the graph
     * @param u A vertex connected with V
     * @param v A vertex connected with U
     */
    boolean removeEdge(V u, V v);

    /**
     * Gives a list of adjacent vertices of V
     * <pre> V Is within the graph
     * @param u The vertex to be consulted its adjacent vertices
     * @return A list with all the adjacent vertices of V
     */
    List<V> vertexAdjacent(V u);

    /**
     * Check if U and V are connected
     * <pre> U and V are within the graph
     * @param u Is a vertex
     * @param v Is a vertex
     * @return True if U and V are connected or false if they're not
     */
    boolean areConnected(V u, V v);

    /**
     * <pre> The graph is weighted
     * @return A matrix with the weight of all the connections
     */
    double[][] weightMatrix();

    /**
     * TODO
     * @return True if the graph is directed or false if it isn't
     */
    boolean isDirected();

    /**
     * TODO
     * @param u
     * @return
     */
    int getIndex(V u);

    /**
     * TODO
     * @return
     */
    int getVertexSize();

    /**
     * TODO
     * @return
     */
    Map<Integer, V> getVertices();

    /**
     * TODO
     * @return
     */
    Map<V, Integer> getIndices();
}
