package model;

class Vertex<V> implements Comparable<Vertex> {
    Integer index;
    V info;

    Vertex(int index, V info) {
        this.index = index;
        this.info = info;
    }

    @Override
    public int compareTo(Vertex vertexToCompare) {
        return index.compareTo(vertexToCompare.index);
    }
}
