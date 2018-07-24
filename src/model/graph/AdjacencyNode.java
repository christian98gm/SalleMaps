package model.graph;

import Model.Connection;

public class AdjacencyNode {

    private Connection connection;
    private AdjacencyNode successor;
    private AdjacencyNode predecessor;

    public AdjacencyNode(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
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