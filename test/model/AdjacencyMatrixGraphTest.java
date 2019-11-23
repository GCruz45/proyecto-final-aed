package model;

import exceptions.ElementAlreadyPresentException;
import exceptions.ElementNotFoundException;
import exceptions.WrongEdgeTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdjacencyMatrixGraphTest {

    private IGraph g;

    public void setStage1() {
        g = new AdjacencyMatrixGraph(false, false);
    }

    @SuppressWarnings("unchecked")
    public void setStage2() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(false, false, 5);
        Object vertex1 = new Object();
        Object vertex2 = new Object();
        Object vertex3 = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
        g.addVertex(vertex1);
        g.addVertex(vertex2);
        g.addVertex(vertex3);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(vertex1, vertex2);
        g.addEdge(vertex2, vertex3);
        g.addEdge(vertex3, vertex4);
        g.addEdge(vertex4, vertex5);
    }

    @SuppressWarnings("unchecked")
    public void setStage3() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(true, true, 5);
        Object vertex1 = new Object();
        Object vertex2 = new Object();
        Object vertex3 = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
        g.addVertex(vertex1);
        g.addVertex(vertex2);
        g.addVertex(vertex3);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(vertex1, vertex2, 0);
        g.addEdge(vertex2, vertex3,-1);
        g.addEdge(vertex3, vertex4,1);
        g.addEdge(vertex4, vertex5,2);
    }

    @SuppressWarnings("unchecked")
    public void setStage4() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(true, true, 5);
        Object vertex1 = new Object();
        Object vertex2 = new Object();
        Object vertex3 = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
        g.addVertex(vertex1);
        g.addVertex(vertex2);
        g.addVertex(vertex3);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(vertex1, vertex2, 0);
        g.addEdge(vertex2, vertex3,5);
        g.addEdge(vertex3, vertex4,7);
        g.addEdge(vertex4, vertex5,3);
    }

    @SuppressWarnings("unchecked")
    public void setStage5() throws ElementAlreadyPresentException, WrongEdgeTypeException, ElementNotFoundException {
        g = new AdjacencyMatrixGraph(false, true, 5);
        Object vertex1 = new Object();
        Object vertex2 = new Object();
        Object vertex3 = new Object();
        Object vertex4 = new Object();
        Object vertex5 = new Object();
        g.addVertex(vertex1);
        g.addVertex(vertex2);
        g.addVertex(vertex3);
        g.addVertex(vertex4);
        g.addVertex(vertex5);
        g.addEdge(vertex1, vertex2, 0);
        g.addEdge(vertex2, vertex3,-5);
        g.addEdge(vertex3, vertex4,7);
        g.addEdge(vertex4, vertex5,-3);
    }

    @Test
    void addVertex() {
    }

    @Test
    void addEdge() {
    }

    @Test
    void addEdge1() {
    }

    @Test
    void removeVertex() {
    }

    @Test
    void removeEdge() {
    }

    @Test
    void vertexAdjacent() {
    }

    @Test
    void areConnected() {
    }

    @Test
    void weightMatrix() {
    }

    @Test
    void isDirected() {
    }

    @Test
    void getVertexSize() {
    }

    @Test
    void getVertices() {
    }

    @Test
    void getEdges() {
    }

    @Test
    void getIndex() {
    }
}