package model;

class Edge implements Comparable<Edge> {
    int u, v;
    Double weight;

    Edge(int u, int v) {
        this.u = u;
        this.v = v;
        this.weight = Double.MIN_VALUE;
    }

    Edge(int u, int v, double weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge edgeToCompare) {
        Double weightToCompare = edgeToCompare.weight;
        return weight.compareTo(weightToCompare);
    }
}
