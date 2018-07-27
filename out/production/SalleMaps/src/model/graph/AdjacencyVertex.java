package model.graph;

import Model.City;

public class AdjacencyVertex {

    private City city;
    private AdjacencyNode successor;
    private AdjacencyNode predecessor;

    public AdjacencyVertex(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
    }

    public AdjacencyNode getSuccessor() {
        return successor;
    }

    public void setSuccessor(AdjacencyNode successor) {
        this.successor = successor;
    }

    public AdjacencyNode getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(AdjacencyNode predecessor) {
        this.predecessor = predecessor;
    }

}