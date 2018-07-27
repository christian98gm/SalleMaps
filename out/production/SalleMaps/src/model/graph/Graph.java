package model.graph;

import Model.City;
import Model.Connection;
import model.DestinationCity;
import model.Route;
import model.avl.StringTree;
import model.list.CustomList;
import model.Cost;

import java.util.List;

public class Graph {

    private final static int DEFAULT_LENGTH = 10;

    private AdjacencyVertex[] adjacencyList;
    private int size;

    //AVL
    private StringTree tree;

    public Graph() {

        adjacencyList = new AdjacencyVertex[DEFAULT_LENGTH];
        size = 0;

        //AVL
        tree = new StringTree();

    }

    public boolean addCity(City city) {

        //Null city
        if(city == null) {
            throw new NullPointerException();
        }

        //Check if exists
        if(containsCity(city.getName())) {
            return false;
        }

        //Check if current length is enough
        if(adjacencyList.length - size == 0) {
            AdjacencyVertex[] newAdjacencyList = new AdjacencyVertex[adjacencyList.length * 2];
            System.arraycopy(adjacencyList, 0, newAdjacencyList, 0, adjacencyList.length);
            adjacencyList = newAdjacencyList;
        }

        //Add vertex
        adjacencyList[size] = new AdjacencyVertex(city);
        size++;

        //AVL
        tree.add(city.getName());

        return true;

    }

    public boolean addConnection(Connection connection) {

        //Null connection or values
        if(connection == null || connection.getFrom() == null || connection.getTo() == null) {
            throw new NullPointerException();
        }

        //Get cities
        String from = connection.getFrom();
        String to = connection.getTo();

        //Check if are the same
        if(from.equals(to)) {
            return false;
        }

        //Check if both cities exist
        if(!containsCity(from) || !containsCity(to)) {
            return false;
        }

        //Check if connection exists
        if(containsConnection(from, to)) {
            return false;
        }

        AdjacencyNode newNode = new AdjacencyNode(connection);
        int found = 0;
        for(int i = 0; found < 2 && i < size; i++) {

            //From city
            if(adjacencyList[i].getCity().getName().equals(from)) {
                if(adjacencyList[i].getSuccessor() == null) {
                    adjacencyList[i].setSuccessor(newNode);
                } else {
                    AdjacencyNode currentNode = adjacencyList[i].getSuccessor();
                    while(currentNode.getSuccessor() != null) {
                        currentNode = currentNode.getSuccessor();
                    }
                    currentNode.setSuccessor(newNode);
                }
                found++;
            }

            //To city
            if(adjacencyList[i].getCity().getName().equals(to)) {
                if(adjacencyList[i].getPredecessor() == null) {
                    adjacencyList[i].setPredecessor(newNode);
                } else {
                    AdjacencyNode currentNode = adjacencyList[i].getPredecessor();
                    while(currentNode.getPredecessor() != null) {
                        currentNode = currentNode.getPredecessor();
                    }
                    currentNode.setPredecessor(newNode);
                }
                found++;
            }

        }

        return true;

    }

    public boolean containsCity(String cityName) {

        //Null city name
        if(cityName == null) {
            throw new NullPointerException();
        }

        for(int i = 0; i < size; i++) {
            if(adjacencyList[i].getCity().getName().equals(cityName)) {
                return true;
            }
        }

        return false;

    }

    private boolean containsConnection(String from, String to) {
        for(int i = 0; i < size; i++) {
            if(adjacencyList[i].getCity().getName().equals(from)) {
                AdjacencyNode currentNode = adjacencyList[i].getSuccessor();
                while(currentNode != null) {
                    if(currentNode.getConnection().getTo().equals(to)) {
                        return true;
                    }
                    currentNode = currentNode.getSuccessor();
                }
            }
        }
        return false;
    }

    public City getCityData(String cityName) {

        //Null city name
        if(cityName == null) {
            throw new NullPointerException();
        }

        //Check if exists
        if(!containsCity(cityName)) {
            return null;
        }

        for(int i = 0; i < size; i++) {
            if(adjacencyList[i].getCity().getName().equals(cityName)) {
                return adjacencyList[i].getCity();
            }
        }

        return null;    //Never happens

    }

    public List<DestinationCity> getSuccessors(City city) {

        //Null city
        if(city == null) {
            throw new NullPointerException();
        }

        //Check if exists
        if(!containsCity(city.getName())) {
            return null;
        }

        List list = new CustomList();

        //Add successor cities
        for(int i = 0; i < size; i++) {
            if(adjacencyList[i].getCity().equals(city)) {
                if(adjacencyList[i].getSuccessor() != null) {
                    AdjacencyNode currentNode;
                    currentNode = adjacencyList[i].getSuccessor();
                    while(currentNode != null) {
                        Connection currentConnection = currentNode.getConnection();
                        DestinationCity destinationCity = new DestinationCity();
                        String currentCity = currentConnection.getTo();
                        for (int j = 0; j < size; j++) {
                            if (adjacencyList[j].getCity().getName().equals(currentCity)) {
                                destinationCity.setCity(adjacencyList[j].getCity());
                                break;
                            }
                        }
                        destinationCity.setDistance(currentConnection.getDistance());
                        destinationCity.setDuration(currentConnection.getDuration());
                        list.add(destinationCity);
                        currentNode = currentNode.getSuccessor();
                    }
                }
                break;
            }
        }

        return list;

    }

    public List<City> getCities() {

        List<City> cities = new CustomList();

        for(int i = 0; i < size; i++) {
            cities.add(adjacencyList[i].getCity());
        }

        return cities;

    }

    public Route getShortestRoute(City from, City to) {
        return getRoute(from, to, true);
    }

    public Route getFastestRoute(City from, City to) {
        return getRoute(from, to, false);
    }

    private Route getRoute(City from, City to, boolean shortest) {

        long init = System.nanoTime();

        //Null cities
        if(from == null || to == null) {
            throw new NullPointerException();
        }

        //Not existent cities
        if(!containsCity(from.getName()) || !containsCity(to.getName())) {
            return null;
        }

        //Dijkstra
        //Init values
        Cost costs[] = new Cost[size];
        int closerNode[] = new int[size];
        List<Integer> vertices = new CustomList();
        int fromIndex = indexOf(from.getName());
        int fromIndexAVL = tree.getStringIndex(from.getName());

        for(int i = 0; i < size; i++) {

            if(i != fromIndex) {
                vertices.add(i);
                costs[i] = getCost(adjacencyList[fromIndex], adjacencyList[i].getCity(), shortest);
            } else {
                Cost cost = new Cost();
                cost.setCost(0);
                cost.setDistance(0);
                cost.setDuration(0);
                costs[i] = cost;
            }

            if(costs[i].getCost() == -1) {
                closerNode[i] = -1;
            } else {
                closerNode[i] = fromIndex;
            }

        }

        //Calculate distances
        for(int i = 0; i < size - 1; i++) {

            Cost minCost = new Cost();
            minCost.setUndefined();
            int minIndex = -1;
            int currentVertex = -1;

            //Find current min cost
            for(int j = 0; j < vertices.size(); j++) {
                //Update min cost
                int k = vertices.get(j);
                if(minCost.getCost() == -1 || minCost.getCost() > costs[k].getCost() && costs[k].getCost() != -1) {
                    minCost = costs[k];
                    minIndex = k;
                    currentVertex = j;
                }
            }

            //Remove vertex
            vertices.remove(currentVertex);

            if(minCost.getCost() != -1) {
                for(int j = 0; j < vertices.size(); j++) {

                    int k = vertices.get(j);
                    Cost newCost = getCost(adjacencyList[minIndex], adjacencyList[k].getCity(), shortest);

                    if(costs[k].getCost() == -1 && newCost.getCost() != -1) {
                        costs[k] = new Cost(minCost, newCost);
                        closerNode[k] = minIndex;
                    } else if(costs[k].getCost() != -1 && newCost.getCost() != -1 &&
                            minCost.getCost() + newCost.getCost() < costs[k].getCost()) {
                        costs[k] = new Cost(minCost, newCost);
                        closerNode[k] = minIndex;
                    }

                }
            } else {
                break;
            }

        }

        System.out.print((System.nanoTime() - init) + " nanos");

        //Check if it's a reachable destiny
        int toIndex = indexOf(to.getName());
        int toIndexAVL = tree.getStringIndex(to.getName());
        if(costs[toIndex].getCost() == -1) {
            return null;
        }

        //Calculate route
        List<Integer> routeTracer = new CustomList();
        routeTracer.add(toIndex);

        int currentIndex = toIndex;
        while(currentIndex != fromIndex) {
            currentIndex = closerNode[currentIndex];
            routeTracer.add(currentIndex);
        }

        //Get route
        Route route = new Route();
        List<City> cities = new CustomList();
        for(int i = routeTracer.size() - 1; i >= 0; i--) {
            cities.add(adjacencyList[routeTracer.get(i)].getCity());
        }
        route.setCities(cities);
        route.setDistance(costs[toIndex].getDistance());
        route.setDuration(costs[toIndex].getDuration());

        return route;

    }

    private Cost getCost(AdjacencyVertex from, City to, boolean shortest) {

        int cost = -1;
        int distance = -1;
        int duration = -1;

        if(from.getSuccessor() != null) {
            AdjacencyNode currentNode = from.getSuccessor();
            while(currentNode != null) {
                Connection connection = currentNode.getConnection();
                if(connection.getTo().equals(to.getName())) {
                    if(shortest) {
                        cost = connection.getDistance();
                    } else {
                        cost = connection.getDuration();
                    }
                    distance = connection.getDistance();
                    duration = connection.getDuration();
                    break;
                }
                currentNode = currentNode.getSuccessor();
            }
        }

        Cost totalCost = new Cost();
        totalCost.setCost(cost);
        totalCost.setDistance(distance);
        totalCost.setDuration(duration);

        return totalCost;

    }

    private int indexOf(String cityName) {

        for(int i = 0; i < size; i++) {
            if(adjacencyList[i].getCity().getName().equals(cityName)) {
                return i;
            }
        }

        return -1;

    }

}