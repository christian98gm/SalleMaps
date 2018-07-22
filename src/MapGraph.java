import java.util.List;

public class MapGraph {

    private GraphVertex[] graphVertices;

    public MapGraph(List<City> cities, List<Connection> connections) {

        //Set vertices
        graphVertices = new GraphVertex[cities.size()];
        for(int i = 0; i < cities.size(); i++) {
            City city = cities.get(i);
            graphVertices[i] = new GraphVertex(city);
        }

        //Set edges
        for(int i = 0; i < connections.size(); i++) {

            Connection connection = connections.get(i);
            GraphEdge graphEdge = new GraphEdge(connection);
            int count = 0;

            for(int j = 0; j < graphVertices.length && count < 2; j++) {
                if(graphVertices[j].getCity().getName().equals(connection.getTo())) {
                    if(graphVertices[j].getSuccessor() == null) {
                        graphVertices[j].setSuccessor(graphEdge);
                    } else {
                        GraphEdge successor = graphVertices[j].getSuccessor();
                        while(successor.getSuccessor() != null) {
                            successor = successor.getSuccessor();
                        }
                        successor.setSuccessor(graphEdge);
                    }
                    count++;
                } else if(graphVertices[j].getCity().getName().equals(connection.getFrom())) {
                    if(graphVertices[j].getPredecessor() == null) {
                        graphVertices[j].setPredecessor(graphEdge);
                    } else {
                        GraphEdge predecessor = graphVertices[j].getPredecessor();
                        while(predecessor.getPredecessor() != null) {
                            predecessor = predecessor.getPredecessor();
                        }
                        predecessor.setPredecessor(graphEdge);
                    }
                    count++;
                }
            }

        }

    }

}