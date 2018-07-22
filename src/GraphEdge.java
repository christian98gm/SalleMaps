public class GraphEdge {

    private Connection connection;
    private GraphEdge successor;
    private GraphEdge predecessor;

    public GraphEdge(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public GraphEdge getSuccessor() {
        return successor;
    }

    public void setSuccessor(GraphEdge successor) {
        this.successor = successor;
    }

    public GraphEdge getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(GraphEdge predecessor) {
        this.predecessor = predecessor;
    }

}