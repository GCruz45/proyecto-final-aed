package model;

import java.util.*;

import collections.CQueue;
import collections.ICollection;
import collections.Stack;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import exceptions.WrongGraphTypeException;

/**
 * This class is meant to execute different algorithms on graphs.
 *
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 * TODO: Check all contract's grammar/spelling.
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth-first search to traverse a graph
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   Graph which is going to be traversed
     * @param u   Edge where it's going to start the BFS
     * @return A list with a resultant order due to a BFS
     */
    <V> List<V> bfs(IGraph<V> g, V u) throws ElementNotFoundException {
        return traversal(g, u, new CQueue<>());
    }

    /**
     * Performs a depth-first search to traverse a graph
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   Graph which is going to be traversed
     * @param u   Edge where it's going to start the DFS
     * @return A list with a resultant order due to a DFS
     */
    <V> List<V> dfs(IGraph<V> g, V u) throws ElementNotFoundException {
        return traversal(g, u, new Stack<>());
    }

    /**
     * This method will traverse the graph given a data structure. This will perform  BFS or DFS, given the case.
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   The graph to be traversed.
     * @param u   The vertex from which the traversal will begin.
     * @param ds  The data structure to be used in this traversal. Either a Stack for a DFS or a CQueue for BFS.<br>
     *            <pre> ds Must be empty.
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              @return A List with the resulting traversal performed on the given graph from the given vertex.
     */
    private static <V> List<V> traversal(IGraph<V> g, V u, ICollection<V> ds) throws ElementNotFoundException {
        if (g.getVertices().get(u)==null)
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
     * An algorithm based on Dijkstra's approach to finding the shortest path from a given vertex to all vertices in the graph.
     *
     * @param <V> Abstract data type that represents a vertex within the graph
     * @param g   The graph to be traversed
     * @param s   The vertex where Dijkstra is going to be used
     * @return A  map with the shortest paths found
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
     * TODO
     *
     * @param indexOfS
     * @param shortestPath
     * @param w
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
     * TODO
     *
     * @param shortestPath
     * @param q
     * @param u
     * @param indexOfV
     * @param w
     */
    private void relax(double[][] shortestPath, Queue<Double[]> q, Double[] u, int indexOfV, double[][] w) {//Pre: s and index exist in the graph.
        int indexOfU = u[0].intValue();
        double suDistance = shortestPath[indexOfU][0];
        double uvDistance = w[indexOfU][indexOfV];
        double svDistance = shortestPath[indexOfV][0];
        //TODO: Check possible overflow of Double.MAX_VALUE
        if (suDistance + uvDistance < svDistance) {//Distance from s to index + distance from index to v < distance from s to v.
            shortestPath[indexOfV][0] = suDistance + uvDistance;
            shortestPath[indexOfV][1] = indexOfU;
            q.add(new Double[]{(double) indexOfV, shortestPath[indexOfV][0]});
        }
    }

    /**
     * TODO
     *
     * @param g
     * @param <V>
     * @return
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
     * TODO
     *
     * @param g
     * @param s
     * @param <V>
     * @return
     */
    <V> int[] prim(IGraph<V> g, V s) throws ElementNotFoundException, WrongGraphTypeException {
        //TODO: Update comments.
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
     * TODO
     *
     * @param g
     * @param <V>
     * @return
     */
    <V> List<Edge> kruskal(IGraph<V> g) throws WrongGraphTypeException {//TODO: Se cambio el return type, revisar consecuencias. Assert si es no dirigido.
        if (g.isDirected())
            throw new WrongGraphTypeException("Graph is not undirected");
        List<Edge> vertexSet = new LinkedList<>();
        Map<Integer, subset> forest = new HashMap<>(g.getVertexSize());
        NavigableSet<Edge> orderedEdges = new TreeSet<>(g.getEdges());

        for (int index : g.getVertices().values())
            forest.put(index, makeSet(index));

        Edge lightestEdge;
        while (!orderedEdges.isEmpty()) {
            lightestEdge = orderedEdges.pollFirst();
            assert lightestEdge != null;//TODO: Necessary?
            subset subsetOfU = forest.get(lightestEdge.u);
            subset subsetOfV = forest.get(lightestEdge.v);
            if (findSet(subsetOfU) != findSet(subsetOfV)) {
                vertexSet.add(lightestEdge);
                union(subsetOfU, subsetOfV);
            }
        }
        return vertexSet;
    }

    /**
     * TODO
     *
     * @param index
     * @return
     */
    private subset makeSet(int index) {
        subset toAdd = new subset();
        toAdd.parent = toAdd;
        toAdd.index = index;
        toAdd.rank = 0;
        return toAdd;
    }

    /**
     * TODO
     *
     * @param s
     * @return
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
     * TODO
     *
     * @param u
     * @param v
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
     * TODO
     */
    private class subset {
        subset parent;
        int index;
        int rank;
    }
}