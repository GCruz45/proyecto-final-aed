package model;

import exceptions.ElementAlreadyPresentException;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GraphAlgorithmsTest {

    private IGraph g;
    private GraphAlgorithms algorithmTest = new GraphAlgorithms();
    private Object u;
    private Object v;
    private Object s;

    public void setStage1() {
        g = new AdjacencyMatrixGraph(false, false);
    }

    @SuppressWarnings("unchecked")
    public void setStage2() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(false, false, 5);
        u = new Object();
        v = new Object();
        s = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
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
    public void setStage3() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(true, true, 5);
        u = new Object();
        v = new Object();
        s = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
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
    public void setStage4() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(true, true, 5);
        u = new Object();
        v = new Object();
        s = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
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
    public void setStage5() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(false, true, 5);
        u = new Object();
        v = new Object();
        s = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
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

    @Test
    void bfs() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage3();
        assertNotNull(algorithmTest.bfs(g, u));
        u = new Object();
        assertThrows(ElementNotFoundException.class, () -> algorithmTest.bfs(g, u));
    }

    @Test
    void dfs() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage3();
        assertNotNull(algorithmTest.dfs(g, u));
        u = new Object();
        assertThrows(ElementNotFoundException.class, () -> algorithmTest.dfs(g, u));
    }

    @Test
    void dijkstra() {

    }

    @Test
    void floydWarshall() {
    }

    @Test
    void prim() {
    }

    @Test
    void kruskal() {
    }
}