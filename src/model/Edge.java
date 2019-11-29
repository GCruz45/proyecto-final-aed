package model;

class Edge<V>{
    V u, v;
    Double weight;

    Edge(V u, V v) {
        this.u = u;
        this.v = v;
        this.weight = Double.MIN_VALUE;
    }

    Edge(V u, V v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }
}
