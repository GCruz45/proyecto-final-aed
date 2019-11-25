package model;

import exceptions.ElementAlreadyPresentException;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import exceptions.WrongGraphTypeException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    private IGraph g;
    private GraphAlgorithms algorithmTest = new GraphAlgorithms();
    private Object u = "u";
    private Object v = "v";
    private Object s = "s";
    private Object vertex4 = "vertex4";
    private Object vertex5 = "vertex5";


    void setStage1() {
        g = new AdjacencyMatrixGraph(false, false);
    }

    @SuppressWarnings("unchecked")
    void setStage2() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(false, false, 5);
        g.addVertex(u);
        g.addVertex(v);
        g.addVertex(s);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(u, v);
        g.addEdge(v, s);
        g.addEdge(s, vertex4);
        g.addEdge(vertex4, vertex5);
    }

    @SuppressWarnings("unchecked")
    void setStage3() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(true, true, 5);
        g.addVertex(u);
        g.addVertex(v);
        g.addVertex(s);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(u, v, 0);
        g.addEdge(v, s, -1);
        g.addEdge(s, vertex4, 1);
        g.addEdge(vertex4, vertex5, 2);
    }

    @SuppressWarnings("unchecked")
    void setStage4() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(true, true, 5);
        g.addVertex(u);
        g.addVertex(v);
        g.addVertex(s);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(u, v, 0);
        g.addEdge(v, s, 5);
        g.addEdge(s, vertex4, 7);
        g.addEdge(vertex4, vertex5, 3);
    }

    @SuppressWarnings("unchecked")
    void setStage5() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(false, true, 5);
        g.addVertex(u);
        g.addVertex(v);
        g.addVertex(s);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(u, v, 0);
        g.addEdge(v, s, -5);
        g.addEdge(s, vertex4, 7);
        g.addEdge(vertex4, vertex5, -3);
    }

    @SuppressWarnings("unchecked")
    @Test
    void bfs() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage3();
        List bfs = algorithmTest.bfs(g, u);
        assertEquals(bfs.get(0), u);
        assertEquals(bfs.get(1), v);
        assertEquals(bfs.get(2), s);
        assertEquals(bfs.get(3), vertex4);
        assertEquals(bfs.get(4), vertex5);
        u = "newU";
        assertThrows(ElementNotFoundException.class, () -> algorithmTest.bfs(g, u));
    }

    @SuppressWarnings("unchecked")
    @Test
    void dfs() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage3();
        List dfs = algorithmTest.dfs(g, u);
        assertEquals(dfs.get(0), u);
        assertEquals(dfs.get(1), v);
        assertEquals(dfs.get(2), s);
        assertEquals(dfs.get(3), vertex4);
        assertEquals(dfs.get(4), vertex5);
        u = "newU";
        assertThrows(ElementNotFoundException.class, () -> algorithmTest.bfs(g, u));
    }

    @SuppressWarnings("unchecked")
    @Test
    void dijkstra() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage4();

        double[][] dijkstraResult = algorithmTest.dijkstra(g, u);
        assert dijkstraResult[0][0] == 0;
        assert dijkstraResult[0][1] == 0;
        assert dijkstraResult[1][0] == 0;
        assert dijkstraResult[1][1] == 0;
        assert dijkstraResult[2][0] == 5;
        assert dijkstraResult[2][1] == 1;
        assert dijkstraResult[3][0] == 12;
        assert dijkstraResult[3][1] == 2;
        assert dijkstraResult[4][0] == 15;
        assert dijkstraResult[4][1] == 3;

        Object newVertex = "newVertex";
        assertThrows(ElementNotFoundException.class, () -> algorithmTest.dijkstra(g, newVertex));

        setStage3();
        assertThrows(WrongEdgeTypeException.class, () -> algorithmTest.dijkstra(g, u));
    }

    @Test
    @SuppressWarnings("unchecked")
    void floydWarshall() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException, WrongGraphTypeException {
        setStage3();
        double[][] floydWarshallResult = algorithmTest.floydWarshall(g);
        assertEquals(0, floydWarshallResult[0][1]);
        assert floydWarshallResult[0][2] == -1;
        assert floydWarshallResult[0][3] == 0;
        assert floydWarshallResult[0][4] == 2;
        assert floydWarshallResult[1][0] == Double.MAX_VALUE;
        assert floydWarshallResult[1][2] == -1;
        assert floydWarshallResult[1][3] == 0;
        assert floydWarshallResult[1][4] == 2;
        assert floydWarshallResult[2][0] == Double.MAX_VALUE;
        assert floydWarshallResult[2][1] == Double.MAX_VALUE;
        assert floydWarshallResult[2][3] == 1;
        assert floydWarshallResult[2][4] == 3;
        assert floydWarshallResult[3][0] == Double.MAX_VALUE;
        assert floydWarshallResult[3][1] == Double.MAX_VALUE;
        assert floydWarshallResult[3][2] == Double.MAX_VALUE;
        assert floydWarshallResult[3][4] == 2;
        assert floydWarshallResult[4][0] == Double.MAX_VALUE;
        assert floydWarshallResult[4][1] == Double.MAX_VALUE;
        assert floydWarshallResult[4][2] == Double.MAX_VALUE;
        assert floydWarshallResult[4][3] == Double.MAX_VALUE;

        setStage2();
        assertThrows(WrongGraphTypeException.class, () -> algorithmTest.floydWarshall(g));
    }

    @Test
    @SuppressWarnings("unchecked")
    void prim() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException, WrongGraphTypeException {
        setStage5();
        int[] primResult = algorithmTest.prim(g, s);
        assert primResult[0] == 1;
        assert primResult[1] == 2;
        assert primResult[2] == 2;
        assert primResult[3] == 2;
        assert primResult[4] == 3;

        Object s = "newS";
        assertThrows(ElementNotFoundException.class, () -> algorithmTest.prim(g, s));

        setStage2();
        assertThrows(WrongGraphTypeException.class, () -> algorithmTest.prim(g, s));

        setStage3();
        assertThrows(WrongGraphTypeException.class, () -> algorithmTest.prim(g, s));
    }

    @Test
    @SuppressWarnings("unchecked")
    void kruskal() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException, WrongGraphTypeException {
        setStage5();
        List<Edge> kruskalResult = algorithmTest.kruskal(g);
        Edge e1 = new Edge(1, 2, -5);
        Edge e2 = new Edge(3, 4, -3);
        Edge e3 = new Edge(0, 1, 0);
        Edge e4 = new Edge(2, 3, 7);
        assertEquals(e1.u, kruskalResult.get(0).u);
        assertEquals(e1.v, kruskalResult.get(0).v);
        assertEquals(e1.weight, kruskalResult.get(0).weight);
        assertEquals(e2.u, kruskalResult.get(1).u);
        assertEquals(e2.v, kruskalResult.get(1).v);
        assertEquals(e2.weight, kruskalResult.get(1).weight);
        assertEquals(e3.u, kruskalResult.get(2).u);
        assertEquals(e3.v, kruskalResult.get(2).v);
        assertEquals(e3.weight, kruskalResult.get(2).weight);
        assertEquals(e4.u, kruskalResult.get(3).u);
        assertEquals(e4.v, kruskalResult.get(3).v);
        assertEquals(e4.weight, kruskalResult.get(3).weight);

        setStage4();
        assertThrows(WrongGraphTypeException.class, () -> algorithmTest.kruskal(g));
    }
}