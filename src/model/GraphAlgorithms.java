package model;

import java.util.*;

import collections.CQueue;
import collections.ICollection;
import collections.Stack;

/**
 * This class is meant to execute different algorithms on graphs.
 *
 * @author AED Class # 003 // 2019
 * @version 1.0 - 10/2019
 */
public class GraphAlgorithms {

    /**
     * Performs a breadth-first search to traverse a graph
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   Graph which is going to be traversed
     * @param v   Vertex where it's going to start the BFS
     * @return A list with a resultant order due to a BFS
     */
    public static <V> List<V> bfs(IGraph<V> g, V v) {
        return traversal(g, v, new Stack<>());
    }

    /**
     * Performs a depth-first search to traverse a graph
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   Graph which is going to be traversed
     * @param v   Vertex where it's going to start the DFS
     * @return A list with a resultant order due to a DFS
     */
    public static <V> List<V> dfs(IGraph<V> g, V v) {
        return traversal(g, v, new CQueue<>());
    }

    /**
     * This method will traverse the graph given a data structure. This will perform  BFS or DFS, given the case.
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   The graph to be traversed.
     * @param v   The vertex from which the traversal will begin.
     * @param ds  The data structure to be used in this traversal. Either a Stack for a DFS or a CQueue for BFS.<br>
     *            <pre> ds Must be empty.
     *                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          @return A List with the resulting traversal performed on the given graph from the given vertex.
     */
    private static <V> List<V> traversal(IGraph<V> g, V v, ICollection<V> ds) {
        List<V> trav = new ArrayList<>();
        //Invariant: Each algorithm adds the given element first.

        V vertex = v;
        ds.add(vertex);

        boolean[] visited = new boolean[g.getVertexSize()];

        //Invariant: While the traversal occurs, the given DS to be used will have, at least, one element.
        while (!ds.isEmpty()) {
            //Invariant: Element added is always retired from the DS
            vertex = ds.poll();
            int indexV = g.getIndex(vertex);

            if (!visited[indexV]) {
                trav.add(vertex);
                visited[indexV] = true;

                List<V> adjacents = g.vertexAdjacent(vertex);
                ds.addAll(adjacents);
            }
        }
        return trav;
    }

    /**
     * An algorithm based on Dijkstra's approach to find the shortest path from a given source vertex to all other vertices in the graph.
     *
     * @param <V> Abstract data type that represent a vertex within the graph
     * @param g   The graph to be traversed.
     * @param s   The vertex where Dijkstra is going to be used.
     * @return A  map with the shortest paths founded
     */
    public static <V> double[][] dijkstra(IGraph<V> g, V s) {
        double[][] w = g.weightMatrix();
        double[][] shortestPath = new double[w.length][2];
        int logicalSize = g.getVertexSize();
        int indexOfS = g.getIndex(s);

        //----------------------------Comienzo segun Cormen----------------------------//
        initializeSingleSource(indexOfS, shortestPath, w);
        Queue<Double[]> q = new PriorityQueue<>(logicalSize, new Comparator<>() {
            @Override
            public int compare(Double[] weight1, Double[] weight2) {
                int comparison = 0;
                if (weight1[1] > weight2[1])
                    comparison = 1;
                else if (weight1[1] < weight2[1])
                    comparison = -1;
                return comparison;
            }
        });
        for (int i = 0; i < w.length; i++)
            q.add(new Double[]{(double) i, w[indexOfS][i]});
        while (!q.isEmpty()) {
            Double[] u = q.poll();//extract-Min
            int indexOfU = u[0].intValue();
            for (int v = 0; v < w.length; v++) {
                if (w[indexOfU][v] < Double.MAX_VALUE) {//u shares and edge with i
                    relax(shortestPath, q, u, v, w);
                    q.add(new Double[]{(double) v, shortestPath[v][0]});
                }
            }
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
    private static void initializeSingleSource(int indexOfS, double[][] shortestPath, double[][] w) {
        System.arraycopy(w[indexOfS], 0, shortestPath[0], 0, w.length);//TODO: Check if works.
        for (int i = 0; i < w.length; i++) {
            shortestPath[i][1] = Double.MAX_VALUE;
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
    private static void relax(double[][] shortestPath, Queue<Double[]> q, Double[] u, int indexOfV, double[][] w) {//Pre: s and u exist in the graph.
        int indexOfU = u[0].intValue();
        double suDistance = shortestPath[indexOfU][0];
        double uvDistance = w[indexOfU][indexOfV];
        double svDistance = shortestPath[indexOfV][0];
        if (suDistance + uvDistance < svDistance) {//Distance from s to u + distance from u to v < distance from s to v.
            shortestPath[indexOfV][0] = suDistance + uvDistance;
            shortestPath[indexOfV][1] = indexOfU;
        }
    }

    /**
     * @param g
     * @param <V>
     * @return
     */
    public static <V> double[][] floydWarshall(IGraph<V> g) {//TODO: Se cambio el parametro. Revisar consecuencias.
        double[][] d = g.weightMatrix();
        int n = d.length;
        for (int k = 1; k <= n; k++) {
            double[][] dk = new double[n][n];
            for (int i = 1; i <= n; i++)
                for (int j = 1; j <= n; j++)
                    dk[i][j] = Math.min(d[i][j], d[i][k] + d[k][j]);
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
    public static <V> int[] prim(IGraph<V> g, V s) {
        //TODO: Assert if is undirected. Check comments.
        double[][] w = g.weightMatrix();
        int vertices = w.length;
        double[] key = new double[vertices];//Stores the smallest edge to a given vertex.
        int[] parent = new int[vertices];//Index of a vertex's parent (-1 == nill).
        Boolean[] marked = new Boolean[vertices];
        int logicalSize = g.getVertexSize();
        int indexOfS = g.getIndex(s);
        Queue<Double[]> indexKeyPair = new PriorityQueue<>(logicalSize, new Comparator<>() {
            @Override
            public int compare(Double[] pair1, Double[] pair2) {
                Double key1 = pair1[0];
                Double key2 = pair2[0];
                return key1.compareTo(key2);
            }
        });
        for (int index : g.getIndices().values()
        ) {
            key[index] = Double.MAX_VALUE;
            parent[index] = Integer.MAX_VALUE;//Since parent.length >= logical size, we need a way to identify missing vertices.
            marked[index] = false;
        }
        indexKeyPair.add(new Double[]{(double) indexOfS, 0.0});
        key[indexOfS] = 0.0;
        parent[indexOfS] = -1;
        while (!indexKeyPair.isEmpty()) {
            Double[] u = indexKeyPair.poll();
            int indexOfU = u[1].intValue();
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
    public static <V> int[] kruskal(IGraph<V> g) {//TODO: Se cambio el return type. Revisar consecuencias. Assert si es no dirigido.
        double[][] w = g.weightMatrix();
        Set<LinkedList<V>> vertexSet = new LinkedHashSet<>(g.getVertexSize());
        Map<Integer, subset> forest = new HashMap<>(g.getVertexSize());
        NavigableSet<Double[]> orderedEdges = new TreeSet<>(new Comparator<>() {
            @Override
            public int compare(Double[] pair1, Double[] pair2) {
                Double key1 = pair1[2];
                Double key2 = pair2[2];
                return key1.compareTo(key2);
            }
        });
        for (int index : g.getIndices().values()) {
            forest.put(index, makeSet(index));
        }
        for (int i = 0; i < w.length; i++) {
            for (int j = 0; j < i; j++) {
                if (w[i][j] != Double.MAX_VALUE)
                    orderedEdges.add(new Double[]{(double) i, (double) j, w[i][j]});
            }
        }
        Double[] lightestEdge;
        while (!orderedEdges.isEmpty()) {
            lightestEdge = orderedEdges.pollFirst();
            assert lightestEdge != null;//TODO: Necessary?
            int indexOfU = lightestEdge.u, indexOfV = lightestEdge.v;
            subset subsetOfU = forest.get(lightestEdge.u);
            subset subsetOfV = forest.get(lightestEdge.v);
            if (findSet(subsetOfU) != findSet(subsetOfV)) {
                LinkedList<V> definitePair = new LinkedList<>();
                V u = g.getVertex(indexOfU), v = g.getVertex(indexOfV);
                definitePair.add(u);
                definitePair.add(v);
                vertexSet.add(definitePair);
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
    private static subset makeSet(int index) {
        subset toAdd = new subset();
        toAdd.parent = toAdd;
        toAdd.index = index;
        toAdd.rank = 0;//TODO: implement rank auto-balance features.
        return toAdd;
    }

    /**
     * TODO
     *
     * @param s
     * @return
     */
    private static int findSet(subset s) {
        int index = -1;
        if (s != s.parent) {
            findSet(s.parent);
        } else {
            index = s.index;
        }
        return index;
    }

    /**
     * TODO
     *
     * @param u
     * @param v
     */
    private static void union(subset u, subset v) {
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
    private static class subset {
        subset parent;
        int index;
        int rank;
    }

}
