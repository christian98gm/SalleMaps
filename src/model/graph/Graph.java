package model.graph;

import Model.City;
import Model.Connection;
import model.DestinationCity;
import model.Route;
import model.avl.StringTree;
import model.hash.OpenHashTable;
import model.list.CustomList;
import model.Cost;

import java.util.List;

public class Graph {

    //Possible optimization values
    public final static int NO_OPTIMIZATION = 1;
    public final static int AVL_OPTIMIZATION = 2;
    public final static int HASH_OPTIMIZATION = 3;

    private final static int DEFAULT_LENGTH = 10;

    private AdjacencyVertex[] adjacencyList;
    private int size;
    private int mode;

    //Optimization
    private StringTree tree;
    private OpenHashTable hashTable;

    public Graph() {

        adjacencyList = new AdjacencyVertex[DEFAULT_LENGTH];
        size = 0;

        //Optimization params
        mode = NO_OPTIMIZATION;
        tree = new StringTree();
        hashTable = new OpenHashTable();

    }

    public int size() {
        return size;
    }

    public void setMode(int mode) {
        if(mode == NO_OPTIMIZATION || mode == AVL_OPTIMIZATION || mode == HASH_OPTIMIZATION) {
            this.mode = mode;
        }
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

        //Optimization
        tree.add(city.getName());
        hashTable.add(city.getName(), size);

        size++;
        return true;

    }

    //
    @SuppressWarnings("Duplicates")
    public boolean addConnection(Connection connection) {

        //Null connection or values
        if(connection == null || connection.getFrom() == null || connection.getTo() == null) {
            throw new NullPointerException();
        }

        //Get cities
        String from = connection.getFrom();
        String to = connection.getTo();

        //Check if are the same
        if(from.equalsIgnoreCase(to)) {
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

        switch(mode) {
            case NO_OPTIMIZATION:

                int found = 0;
                for (int i = 0; found < 2 && i < size; i++) {

                    //From city
                    if(adjacencyList[i].getCity().getName().equalsIgnoreCase(from)) {
                        if(adjacencyList[i].getSuccessor() == null) {
                            adjacencyList[i].setSuccessor(newNode);
                        } else {
                            AdjacencyNode currentNode = adjacencyList[i].getSuccessor();
                            while (currentNode.getSuccessor() != null) {
                                currentNode = currentNode.getSuccessor();
                            }
                            currentNode.setSuccessor(newNode);
                        }
                        found++;
                    }

                    //To city
                    if (adjacencyList[i].getCity().getName().equalsIgnoreCase(to)) {
                        if (adjacencyList[i].getPredecessor() == null) {
                            adjacencyList[i].setPredecessor(newNode);
                        } else {
                            AdjacencyNode currentNode = adjacencyList[i].getPredecessor();
                            while (currentNode.getPredecessor() != null) {
                                currentNode = currentNode.getPredecessor();
                            }
                            currentNode.setPredecessor(newNode);
                        }
                        found++;
                    }

                }

                break;
            case AVL_OPTIMIZATION:

                //Add successor
                int treeFromIndex = tree.getStringIndex(from);
                if(adjacencyList[treeFromIndex].getSuccessor() == null) {
                    adjacencyList[treeFromIndex].setSuccessor(newNode);
                } else {
                    AdjacencyNode currentNode = adjacencyList[treeFromIndex].getSuccessor();
                    while(currentNode.getSuccessor() != null) {
                        currentNode = currentNode.getSuccessor();
                    }
                    currentNode.setSuccessor(newNode);
                }

                //Add predecessor
                int treeToIndex = tree.getStringIndex(to);
                if(adjacencyList[treeToIndex].getPredecessor() == null) {
                    adjacencyList[treeToIndex].setPredecessor(newNode);
                } else {
                    AdjacencyNode currentNode = adjacencyList[treeToIndex].getPredecessor();
                    while (currentNode.getPredecessor() != null) {
                        currentNode = currentNode.getPredecessor();
                    }
                    currentNode.setPredecessor(newNode);
                }

                break;
            case HASH_OPTIMIZATION:

                //Add successor
                int hashFromIndex = hashTable.getValue(from);
                if(adjacencyList[hashFromIndex].getSuccessor() == null) {
                    adjacencyList[hashFromIndex].setSuccessor(newNode);
                } else {
                    AdjacencyNode currentNode = adjacencyList[hashFromIndex].getSuccessor();
                    while (currentNode.getSuccessor() != null) {
                        currentNode = currentNode.getSuccessor();
                    }
                    currentNode.setSuccessor(newNode);
                }

                //Add predecessor
                int hashToIndex = hashTable.getValue(to);
                if(adjacencyList[hashToIndex].getPredecessor() == null) {
                    adjacencyList[hashToIndex].setPredecessor(newNode);
                } else {
                    AdjacencyNode currentNode = adjacencyList[hashToIndex].getPredecessor();
                    while (currentNode.getPredecessor() != null) {
                        currentNode = currentNode.getPredecessor();
                    }
                    currentNode.setPredecessor(newNode);
                }

                break;
        }

        return true;

    }

    public boolean containsCity(String cityName) {

        //Null city name
        if(cityName == null) {
            throw new NullPointerException();
        }

        switch(mode) {
            case NO_OPTIMIZATION:
                for(int i = 0; i < size; i++) {
                    if(adjacencyList[i].getCity().getName().equalsIgnoreCase(cityName)) {
                        return true;
                    }
                }
                break;
            case AVL_OPTIMIZATION:
                return tree.containsString(cityName);
            case HASH_OPTIMIZATION:
                return hashTable.containsKey(cityName);
        }

        return false;

    }

    private boolean containsConnection(String from, String to) {
        switch(mode) {
            case NO_OPTIMIZATION:
                for (int i = 0; i < size; i++) {
                    if (adjacencyList[i].getCity().getName().equalsIgnoreCase(from)) {
                        AdjacencyNode currentNode = adjacencyList[i].getSuccessor();
                        while (currentNode != null) {
                            if (currentNode.getConnection().getTo().equals(to)) {
                                return true;
                            }
                            currentNode = currentNode.getSuccessor();
                        }
                    }
                }
                break;
            case AVL_OPTIMIZATION:
                AdjacencyNode treeCurrentNode = adjacencyList[tree.getStringIndex(from)].getSuccessor();
                while (treeCurrentNode != null) {
                    if (treeCurrentNode.getConnection().getTo().equalsIgnoreCase(to)) {
                        return true;
                    }
                    treeCurrentNode = treeCurrentNode.getSuccessor();
                }
                break;
            case HASH_OPTIMIZATION:
                AdjacencyNode hashCurrentNode = adjacencyList[hashTable.getValue(from)].getSuccessor();
                while (hashCurrentNode != null) {
                    if (hashCurrentNode.getConnection().getTo().equalsIgnoreCase(to)) {
                        return true;
                    }
                    hashCurrentNode = hashCurrentNode.getSuccessor();
                }
                break;
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

        switch(mode) {
            case NO_OPTIMIZATION:
                for(int i = 0; i < size; i++) {
                    if(adjacencyList[i].getCity().getName().equalsIgnoreCase(cityName)) {
                        return adjacencyList[i].getCity();
                    }
                }
                break;
            case AVL_OPTIMIZATION:
                return adjacencyList[tree.getStringIndex(cityName)].getCity();
            case HASH_OPTIMIZATION:
                return adjacencyList[hashTable.getValue(cityName)].getCity();
        }

        return null;    //Never happens

    }

    @SuppressWarnings("Duplicates")
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
        switch(mode) {
            case NO_OPTIMIZATION:
                for (int i = 0; i < size; i++) {
                    if(adjacencyList[i].getCity().getName().equalsIgnoreCase(city.getName())) {
                        if(adjacencyList[i].getSuccessor() != null) {
                            AdjacencyNode currentNode;
                            currentNode = adjacencyList[i].getSuccessor();
                            while (currentNode != null) {
                                Connection currentConnection = currentNode.getConnection();
                                DestinationCity destinationCity = new DestinationCity();
                                String currentCity = currentConnection.getTo();
                                for (int j = 0; j < size; j++) {
                                    if (adjacencyList[j].getCity().getName().equalsIgnoreCase(currentCity)) {
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
                break;
            case AVL_OPTIMIZATION:
                int treeIndex = tree.getStringIndex(city.getName());
                if(adjacencyList[treeIndex].getSuccessor() != null) {
                    AdjacencyNode currentNode;
                    currentNode = adjacencyList[treeIndex].getSuccessor();
                    while (currentNode != null) {
                        Connection currentConnection = currentNode.getConnection();
                        DestinationCity destinationCity = new DestinationCity();
                        String currentCity = currentConnection.getTo();
                        for (int j = 0; j < size; j++) {
                            if (adjacencyList[j].getCity().getName().equalsIgnoreCase(currentCity)) {
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
            case HASH_OPTIMIZATION:
                int hashIndex = hashTable.getValue(city.getName());
                if(adjacencyList[hashIndex].getSuccessor() != null) {
                    AdjacencyNode currentNode;
                    currentNode = adjacencyList[hashIndex].getSuccessor();
                    while (currentNode != null) {
                        Connection currentConnection = currentNode.getConnection();
                        DestinationCity destinationCity = new DestinationCity();
                        String currentCity = currentConnection.getTo();
                        for (int j = 0; j < size; j++) {
                            if (adjacencyList[j].getCity().getName().equalsIgnoreCase(currentCity)) {
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

        return list;

    }

    public List<City> subList(int from, int to) {

        if(from < 0 || from >= size || to < 0 || to >= size || from > to) {
            throw new IndexOutOfBoundsException();
        }

        List<City> cities = new CustomList();

        for(int i = to; i < from; i++) {
            cities.add(adjacencyList[i].getCity());
        }

        return cities;

    }

    public Route getRoute(City from, City to, boolean shortest) {

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
        int vertices[] = new int[size];
        int fromIndex = indexOf(from.getName());

        for(int i = 0; i < size; i++) {

            if(i != fromIndex) {
                vertices[i] = i;
                costs[i] = getCost(adjacencyList[fromIndex], adjacencyList[i].getCity(), shortest);
            } else {
                vertices[i] = -1;
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
            for(int j = 0; j < vertices.length; j++) {
                //Update min cost
                int k = vertices[j];
                if(k != -1) {
                    if (minCost.getCost() == -1 || minCost.getCost() > costs[k].getCost() && costs[k].getCost() != -1) {
                        minCost = costs[k];
                        minIndex = k;
                        currentVertex = j;
                    }
                }
            }

            //Remove vertex
            vertices[currentVertex] = -1;

            if(minCost.getCost() != -1) {
                for(int j = 0; j < vertices.length; j++) {

                    int k = vertices[j];
                    if(k != -1) {
                        Cost newCost = getCost(adjacencyList[minIndex], adjacencyList[k].getCity(), shortest);
                        if (costs[k].getCost() == -1 && newCost.getCost() != -1) {
                            costs[k] = new Cost(minCost, newCost);
                            closerNode[k] = minIndex;
                        } else if (costs[k].getCost() != -1 && newCost.getCost() != -1 &&
                                minCost.getCost() + newCost.getCost() < costs[k].getCost()) {
                            costs[k] = new Cost(minCost, newCost);
                            closerNode[k] = minIndex;
                        }
                    }

                }
            } else {
                break;
            }

        }

        //Check if it's a reachable destiny
        int toIndex = indexOf(to.getName());
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
                if(connection.getTo().equalsIgnoreCase(to.getName())) {
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

        switch(mode) {
            case NO_OPTIMIZATION:
                for (int i = 0; i < size; i++) {
                    if(adjacencyList[i].getCity().getName().equalsIgnoreCase(cityName)) {
                        return i;
                    }
                }
                break;
            case AVL_OPTIMIZATION:
                return tree.getStringIndex(cityName);
            case HASH_OPTIMIZATION:
                return hashTable.getValue(cityName);
        }

        return -1;

    }

}