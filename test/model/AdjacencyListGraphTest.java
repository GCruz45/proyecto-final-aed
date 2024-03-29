package model;

import exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyListGraphTest {

    private IGraph g;
    private Object u = "u";
    private Object v = "v";
    private Object s = "s";
    private Object vertex4 = "vertex4";
    private Object vertex5 = "vertex5";


    void setStage7() {
        g = new AdjacencyListGraph(false, false);
    }

    @SuppressWarnings("unchecked")
    void setStage8() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyListGraph(false, false);
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
    void setStage9() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyListGraph(true, true);
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
    void setStage10() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyListGraph(true, true);
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
    void setStage11() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyListGraph(false, true);
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
    void setStage12() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyListGraph(true, false);
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

    @Test
    @SuppressWarnings("unchecked")
    void addVertex() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage7();
        Object newVertex = "newVertex";
        assert g.addVertex(newVertex);

        setStage8();
        assertThrows(ElementAlreadyPresentException.class, () -> g.addVertex(u));
    }

    @Test
    @SuppressWarnings("unchecked")
    void addEdge() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage8();
        Object newVertex = "newVertex";
        assertThrows(ElementNotFoundException.class, () -> g.addEdge(newVertex, v));

        assertThrows(ElementNotFoundException.class, () -> g.addEdge(u, newVertex));

        assertTrue(g.addEdge(u, v));

        assertThrows(WrongEdgeTypeException.class, () -> g.addEdge(u, v, 10));

        assertTrue(g.addEdge(u, u));

        setStage9();

        assertThrows(ElementNotFoundException.class, () -> g.addEdge(newVertex, v, 10));

        assertThrows(ElementNotFoundException.class, () -> g.addEdge(u, newVertex, 10));

        assertTrue(g.addEdge(u, v, 10));

        assertTrue(g.addEdge(u, u, 10));
    }

    @Test
    @SuppressWarnings("unchecked")
    void removeVertex() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage7();
        assertThrows(ElementNotFoundException.class, () -> g.removeVertex(u));

        setStage8();
        assertTrue(g.removeVertex(u));
    }

    @Test
    @SuppressWarnings("unchecked")
    void removeEdge() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage8();
        assertFalse(g.areConnected(u, s));

        assertTrue(g.areConnected(u, v));

        Object newVertex = "newVertex";
        assertThrows(ElementNotFoundException.class, () -> g.removeEdge(newVertex, v));

        assertThrows(ElementNotFoundException.class, () -> g.removeEdge(u, newVertex));

        setStage12();
        assertThrows(ElementNotFoundException.class, () -> g.removeEdge(u, s));

        assertTrue(g.removeEdge(u, v));
    }

    @Test
    @SuppressWarnings("unchecked")
    void vertexAdjacent() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage10();
        Object newVertex = "newVertex";
        assertThrows(ElementNotFoundException.class, () -> g.vertexAdjacent(newVertex));

        assertEquals(1, g.vertexAdjacent(u).size());
        assertEquals(v, g.vertexAdjacent(u).get(0));

        g.addVertex(newVertex);
        assertEquals(0,g.vertexAdjacent(newVertex).size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void areConnected() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage9();
        Object newVertex = "newVertex";
        assertThrows(ElementNotFoundException.class, () -> g.areConnected(newVertex, v));

        assertThrows(ElementNotFoundException.class, () -> g.areConnected(u, newVertex));

        assertTrue(g.areConnected(u, v));

        assertFalse(g.areConnected(u, vertex5));
    }

    @Test
    void weightMatrix() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage11();

        double[][] weightMatrix = g.weightMatrix();
        for (int i = 0; i < g.getVertexSize(); i++)
            assertEquals(0, weightMatrix[i][i]);
        assertEquals(0, weightMatrix[0][1]);
        assertEquals(Double.MAX_VALUE, weightMatrix[0][2]);
        assertEquals(Double.MAX_VALUE, weightMatrix[0][3]);
        assertEquals(Double.MAX_VALUE, weightMatrix[0][4]);
        assertEquals(0, weightMatrix[1][0]);
        assertEquals(-5, weightMatrix[1][2]);
        assertEquals(Double.MAX_VALUE, weightMatrix[1][3]);
        assertEquals(Double.MAX_VALUE, weightMatrix[1][4]);
        assertEquals(Double.MAX_VALUE, weightMatrix[2][0]);
        assertEquals(-5, weightMatrix[2][1]);
        assertEquals(7, weightMatrix[2][3]);
        assertEquals(Double.MAX_VALUE, weightMatrix[2][4]);
        assertEquals(Double.MAX_VALUE, weightMatrix[3][0]);
        assertEquals(Double.MAX_VALUE, weightMatrix[3][1]);
        assertEquals(7, weightMatrix[3][2]);
        assertEquals(-3, weightMatrix[3][4]);
        assertEquals(Double.MAX_VALUE, weightMatrix[4][0]);
        assertEquals(Double.MAX_VALUE, weightMatrix[4][1]);
        assertEquals(Double.MAX_VALUE, weightMatrix[4][2]);
        assertEquals(-3, weightMatrix[4][3]);
    }

    @Test
    void isDirected() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage8();
        assertFalse(g.isDirected());

        setStage9();
        assertTrue(g.isDirected());
    }

    @Test
    void getVertexSize() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage7();
        assertEquals(0, g.getVertexSize());

        setStage8();
        assertEquals(5, g.getVertexSize());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getVertices() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage9();
        Map toCompare = new HashMap();
        toCompare.put(u, 0);
        toCompare.put(v, 1);
        toCompare.put(s, 2);
        toCompare.put(vertex4, 3);
        toCompare.put(vertex5, 4);
        assertEquals(toCompare, g.getVertices());
    }

    @Test
    void getEdges() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage8();
        assertEquals(5, g.getEdges().size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void getIndex() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        setStage10();
        assertEquals(g.getIndex(u), 0);
        assertEquals(g.getIndex(v), 1);
        assertEquals(g.getIndex(s), 2);
        assertEquals(g.getIndex(vertex4), 3);
        assertEquals(g.getIndex(vertex5), 4);
    }
}