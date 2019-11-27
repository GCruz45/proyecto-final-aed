package model;

import java.util.*;

import collections.CQueue;
import collections.ICollection;
import collections.Stack;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import exceptions.WrongGraphTypeException;

/**
 * This class is meant to provide different algorithms useful in managing graphs.
 *
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth-first search traversal of this graph.
     *
     * @param <V> type that represent a vertex within the graph
     * @param g   graph to traverse
     * @param u   vertex from which to begin traversing
     * @return a list whose order follows that of a BFS
     * @throws ElementNotFoundException if 'u' does not belong to the graph
     */
    <V> List<V> bfs(IGraph<V> g, V u) throws ElementNotFoundException {
        return traversal(g, u, new CQueue<>());
    }

    /**
     * Performs a depth-first search traversal of this graph.
     *
     * @param <V> type that represent a vertex within the graph
     * @param g   graph to traverse
     * @param u   vertex from which to begin traversing
     * @return a list whose order follows that of a DFS
     * @throws ElementNotFoundException if 'u' does not belong to the graph
     */
    <V> List<V> dfs(IGraph<V> g, V u) throws ElementNotFoundException {
        return traversal(g, u, new Stack<>());
    }

    /**
     * This method will traverse the graph using the given -appropriate- data structure.
     *
     * @param <V> type that represent a vertex within the graph
     * @param g   graph to traverse
     * @param u   vertex from which to begin traversing
     * @param ds  the data structure to be used in the traversal. Either a Stack for a DFS or a CQueue for BFS
     * @return a list whose order follows that of the given data structure
     * @throws ElementNotFoundException if 'u' does not belong to the graph
     */
    private static <V> List<V> traversal(IGraph<V> g, V u, ICollection<V> ds) throws ElementNotFoundException {
        if (g.getVertices().get(u) == null)
            throw new ElementNotFoundException("Parameter not found in graph");

        List<V> trav = new ArrayList<>();
        //Invariant: Each algorithm adds the given element first.

        V placeholder = u;
        ds.add(placeholder);

        boolean[] visited = new boolean[g.getVertexSize()];

        //Invariant: While the traversal occurs, the given DS to be used will have, at least, one element.
        while (!ds.isEmpty()) {
            //Invariant: Element added is always removed from the DS
            placeholder = ds.poll();
            int indexU = g.getVertices().get(placeholder);

            if (!visited[indexU]) {
                trav.add(placeholder);
                visited[indexU] = true;

                List<V> adjacent = g.vertexAdjacent(placeholder);
                ds.addAll(adjacent);
            }
        }
        return trav;
    }

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
        double[][] shortestPath = new double[w.length][2];
        int logicalSize = g.getVertexSize();
        int indexOfS = g.getIndex(s);

        //----------------------------Comienzo segun Cormen----------------------------//
        initializeSingleSource(indexOfS, shortestPath, w);
        Queue<Double[]> q = new PriorityQueue<>(logicalSize, new Comparator<Double[]>() {
            @Override
            public int compare(Double[] weight1, Double[] weight2) {
                return weight1[1].compareTo(weight2[1]);
            }
        });
        for (int i = 0; i < w.length; i++) {
            if (indexOfS != i)
                q.add(new Double[]{(double) i, w[indexOfS][i]});
        }

        while (!q.isEmpty()) {
            Double[] u = q.poll();//extract-Min
            if (u[1] < 0)
                throw new WrongEdgeTypeException("Graph contains a negative edge");
            int indexOfU = u[0].intValue();
            for (int v = 0; v < w.length; v++)
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
        for (int i = 0; i < w.length; i++) {
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
        if (suDistance + uvDistance < svDistance) {//Distance from s to index + distance from index to v < distance from s to v.
            shortestPath[indexOfV][0] = suDistance + uvDistance;
            shortestPath[indexOfV][1] = indexOfU;
            q.add(new Double[]{(double) indexOfV, shortestPath[indexOfV][0]});
        }
    }

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
        if (!g.isWeighted())
            throw new WrongGraphTypeException("Expected weighted graph");

        double[][] d = g.weightMatrix();
        int n = g.getVertexSize();
        for (int k = 0; k < n; k++) {
            double[][] dk = new double[n][n];
            for (int j = 0; j < n; j++)
                for (int i = 0; i < n; i++)
                    if (d[i][k] != Double.MAX_VALUE || d[k][j] != Double.MAX_VALUE)
                        dk[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
                    else
                        dk[i][j] = d[i][j];
            d = dk;
        }
        return d;
    }

    /**
     * An algorithm based on Prim's approach to finding the minimum spanning tree in a connected, undirected graph from
     * a given source node. Returns an array of int representing the index of the vertex each node must move through to
     * arrive at source node 's'.
     *
     * @param g   the graph to be queried
     * @param s   the source node from which to construct the MST
     * @param <V> the type of nodes in the graph
     * @return an array of int representing the index of the vertex each node must move through to arrive at source
     * node 's'.
     * @throws ElementNotFoundException if node 's' is not in the graph
     * @throws WrongGraphTypeException  if graph 'g' is directed
     */
    <V> int[] prim(IGraph<V> g, V s) throws ElementNotFoundException, WrongGraphTypeException {
        if (g.isDirected())
            throw new WrongGraphTypeException("Graph needs to be undirected");
        else if (!g.isWeighted())
            throw new WrongGraphTypeException("Graph needs to be weighted");

        double[][] w = g.weightMatrix();
        int vertices = g.getVertexSize();
        int indexOfS = g.getIndex(s);

        double[] key = new double[vertices];//Stores the smallest distance to a given vertex.
        int[] parent = new int[vertices];//Index of a vertex's parent (-1 => parent == nill).
        Boolean[] marked = new Boolean[vertices];

        Queue<Double[]> indexKeyPair = new PriorityQueue<>(vertices, new Comparator<Double[]>() {
            @Override
            public int compare(Double[] pair1, Double[] pair2) {
                Double key1 = pair1[0];
                Double key2 = pair2[0];
                return key1.compareTo(key2);
            }
        });

        for (int index : g.getVertices().values()) {
            key[index] = Double.MAX_VALUE;
            parent[index] = Integer.MAX_VALUE;//Since parent.length >= logical size, we need a way to identify missing vertices.
            marked[index] = false;
        }
        indexKeyPair.add(new Double[]{(double) indexOfS, 0.0});
        key[indexOfS] = 0.0;
        parent[indexOfS] = indexOfS;

        while (!indexKeyPair.isEmpty()) {
            Double[] u = indexKeyPair.poll();
            int indexOfU = u[0].intValue();
            marked[indexOfU] = true;
            for (int i = 0; i < w[0].length; i++) {
                //Checks if the vertices to be compared share an edge
                if (w[indexOfU][i] != Double.MAX_VALUE) {
                    //Checks if the second vertex has not been added yet and if its
                    if (!marked[i] && w[indexOfU][i] < key[i]) {//Checks if v belongs to Q.
                        key[i] = w[indexOfU][i];
                        parent[i] = indexOfU;
                        indexKeyPair.add(new Double[]{(double) i, key[i]});
                    }
                }
            }
        }
        return parent;
    }

    /**
     * An algorithm based on Kruskal's approach to building the MST of a graph.
     *
     * @param g   the graph whose MST is to be returned
     * @param <V> the type of node in the graph
     * @return a list of type Edge which contains information on the starting and ending vertices and its weight
     * @throws WrongGraphTypeException if graph 'g' is not undirected
     */
    <V> List<Edge> kruskal(IGraph<V> g) throws WrongGraphTypeException {
        if (g.isDirected())
            throw new WrongGraphTypeException("Graph is not undirected");

        List<Edge> vertexSet = new LinkedList<>();
        Map<Integer, subset> forest = new HashMap<>(g.getVertexSize());
        NavigableSet<Double[]> orderedEdges = new TreeSet<>(Comparator.comparing(o -> o[2]));

        for (int index : g.getVertices().values())
            forest.put(index, makeSet(index));

        for (int i = 0; i < g.getVertexSize(); i++) {
            for (int j = 0; j < g.getVertexSize(); j++)
                if (g.weightMatrix()[i][j] != Double.MAX_VALUE && i != j)
                    orderedEdges.add(new Double[]{(double) i, (double) j, g.weightMatrix()[i][j]});
        }

        Double[] lightestEdge;
        while (!orderedEdges.isEmpty()) {
            lightestEdge = orderedEdges.pollFirst();
            subset subsetOfU = forest.get(lightestEdge[0].intValue());
            subset subsetOfV = forest.get(lightestEdge[1].intValue());
            if (findSet(subsetOfU) != findSet(subsetOfV)) {
                vertexSet.add(new Edge(lightestEdge[0].intValue(), lightestEdge[1].intValue(), lightestEdge[2]));
                union(subsetOfU, subsetOfV);
            }
        }
        return vertexSet;
    }

    /**
     * An auxiliary method to Kruskal's approach to building the MST of an undirected graph. Returns a new instance of
     * the nested class 'subset' which represents a set or tree.
     *
     * @param index index of the vertex in the graph that this subset represents
     * @return a new 'subset' whose only member is the vertex represented by the given index
     */
    private subset makeSet(int index) {
        subset toAdd = new subset();
        toAdd.parent = toAdd;
        toAdd.index = index;
        toAdd.rank = 0;
        return toAdd;
    }

    /**
     * An auxiliary method to Kruskal's approach to building the MST of an undirected graph. Finds the index of the
     * vertex that acts as the root of the tree of the given subset.
     *
     * @param s the subset whose root is to be found
     * @return the index of the root vertex in the graph
     */
    private int findSet(subset s) {
        int index = -1;
        if (s != s.parent)
            findSet(s.parent);
        else
            index = s.index;
        return index;
    }

    /**
     * An auxiliary method to Kruskal's approach to building the MST of an undirected graph. Joins the two given
     * subsets into a single one so that one becomes the parent of the other.
     *
     * @param u one of the subsets to be joined
     * @param v one of the subsets to be joined
     */
    private void union(subset u, subset v) {
        if (u.rank > v.rank)
            v.parent = u;
        else
            u.parent = v;
        if (u.rank == v.rank)
            v.rank++;
    }

    /**
     * Nested class used by Kruskal's approach to building the MST of an undirected graph. It models nodes of a tree
     * that represent individual sets.
     */
    private class subset {
        subset parent;
        int index;
        int rank;
    }
}