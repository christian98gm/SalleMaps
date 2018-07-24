package model.graph;

import Model.City;
import Model.Connection;
import model.DestinationCity;
import model.list.CustomList;

import java.util.List;

public class Graph {

    private final static int DEFAULT_LENGTH = 10;

    private AdjacencyVertex[] adjacencyList;
    private int size;

    public Graph() {
        adjacencyList = new AdjacencyVertex[DEFAULT_LENGTH];
        size = 0;
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

}