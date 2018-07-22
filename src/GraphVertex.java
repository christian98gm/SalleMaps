public class GraphVertex {

    private City city;
    private GraphEdge successor;
    private GraphEdge predecessor;

    public GraphVertex(City city) {
        this.city = city;
    }

    public City getCity() {
        return city;
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