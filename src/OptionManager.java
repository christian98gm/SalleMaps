import Model.City;
import Model.Connection;
import model.DestinationCity;
import model.list.CustomList;
import model.utils.GoogleManager;
import model.utils.JsonManager;
import model.Route;
import model.graph.Graph;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class OptionManager implements GoogleManager.CityCallback, GoogleManager.ConnectionsCallback {

    //Raw path
    private final static String RAW_DIR = "raw" + System.getProperty("file.separator");

    //Possible main menu options
    private final static int IMPORT = 1;
    private final static int SEARCH = 2;
    private final static int ROUTE = 3;
    private final static int EXIT = 4;

    //Possible route menu options
    private final static int SHORTEST = 1;
    private final static int FASTEST = 2;

    //Max connections
    private final static int MAX_CONNECTIONS = 20;
    private final static int MAX_CITIES_PER_CALL = 100;

    //Managers
    private Graph graph;
    private GoogleManager googleManager;

    //Search aux params
    private List<Connection> connections;
    private boolean error;

    //Time
    private long initTime;

    public OptionManager() {
        googleManager = GoogleManager.getInstance();
        connections = new CustomList();
    }

    public boolean optionChosen(int option) {
        Menu menu = new Menu();
        switch(option) {
            case IMPORT:
                importMap();
                return false;
            case SEARCH:
                if(graph != null) {
                    searchCity();
                } else {
                    menu.notifyUnavailableOption();
                }
                return false;
            case ROUTE:
                if(graph != null) {
                    routingProcess();
                } else {
                    menu.notifyUnavailableOption();
                }
                return false;
            case EXIT:
                menu.notifyExit();
                return true;
            default:
                menu.notifyInvalidIntRange();
                return false;
        }
    }

    private void importMap() {

        Menu menu = new Menu();
        boolean importOk;

        //Ask for file until ok
        do {

            importOk = false;
            menu.askForFile();
            if(!menu.isOptionEmpty()) {

                try {

                    //Read file as text
                    BufferedReader br = new BufferedReader(new FileReader(RAW_DIR  + menu.getOptionString()));
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while(line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }

                    //Read file content
                    JsonManager jsonManager = new JsonManager(sb.toString());
                    List<City> cities = jsonManager.getCities();
                    List<Connection> connections = jsonManager.getConnections();

                    //Create graph
                    graph = new Graph();
                    for(City city : cities) {
                        graph.addCity(city);
                    }
                    for(Connection connection : connections) {
                        graph.addConnection(connection);
                    }

                    //All ok
                    importOk = true;
                    System.out.println();
                    System.out.println("Map loaded!");

                } catch(IOException e1) {
                    System.out.println();
                    System.out.println("File not found!");
                } catch(JSONException e2) {
                    System.out.println();
                    System.out.println(e2.getMessage());
                }

            } else {
                menu.notifyEmptyOption();
            }

        } while(!importOk);

    }

    private void searchCity() {

        Menu menu = new Menu();
        boolean notEmpty;

        do {

            notEmpty = false;
            menu.askForCity();

            if(!menu.isOptionEmpty()) {

                notEmpty = true;
                String cityName = menu.getOptionString();
                boolean exit = false;

                do {

                    menu.showOptimizationMenu();
                    if(menu.isOptionEmpty()) {
                        menu.notifyEmptyOption();
                    } else if(!menu.isOptionInt()) {
                        menu.notifyNoIntOption();
                    } else {
                        int option = menu.getOptionInt();
                        if(option != Graph.NO_OPTIMIZATION && option != Graph.AVL_OPTIMIZATION &&
                                option != Graph.HASH_OPTIMIZATION) {
                            menu.notifyInvalidIntRange();
                        } else {
                            graph.setMode(option);
                            if(!graph.containsCity(cityName)) {
                                initTime = System.nanoTime();
                                error = false;
                                connections.clear();
                                googleManager.getNewCity(cityName, this);
                            } else {
                                showCityData(graph.getCityData(cityName));
                            }
                            exit = true;
                        }
                    }

                } while(!exit);

            } else {
                menu.notifyEmptyOption();
            }

        } while(!notEmpty);

    }

    private void showCityData(City city) {
        List<DestinationCity> successors = graph.getSuccessors(city);
        Menu menu = new Menu();
        menu.showCityInformation(city, successors);
    }

    @Override
    public void cityResult(City city, int errorCode) {
        switch(errorCode) {
            case GoogleManager.OK:
                List<City> cities = graph.getCities();
                int count = 0;
                int size = cities.size();
                while(count < size) {
                    int newCount = count + MAX_CITIES_PER_CALL;
                    googleManager.getCloserDestinies(city,
                            cities.subList(count, Integer.min(newCount - 1, cities.size() - 1)), this,
                            newCount >= cities.size() - 1);
                    count = newCount;
                }
                break;
            case GoogleManager.KO:
                Menu menu = new Menu();
                menu.notifyCityNotFound();
                break;
        }
    }

    @Override
    public void connectionsResult(City from, List<Connection> newConnections, int errorCode, boolean endInformation) {

        if(errorCode == GoogleManager.KO) {
            error = true;
        }

        connections.addAll(newConnections);

        if(endInformation) {

            graph.addCity(from);
            List<Connection> closerConnections = getCloserConnections(connections, MAX_CONNECTIONS);
            for(int i = 0; i < closerConnections.size(); i++) {
                Connection connection = closerConnections.get(i);
                Connection returnConnection = new Connection(connection.getTo(), from.getName(),
                        connection.getDistance(), connection.getDuration());
                graph.addConnection(connection);
                graph.addConnection(returnConnection);
            }

            Menu menu = new Menu();

            if(error) {
                menu.notifyConnectionsError();
            }

            if(!connections.isEmpty()) {
                menu.notifyCityAdded();
                showCityData(from);
            }

            menu.notifyOperationTime(System.nanoTime() - initTime);
            error = false;
            connections.clear();

        }

    }

    private List<Connection> getCloserConnections(List<Connection> connections, int maxConnections) {
        if(connections.size() <= maxConnections) {
            return connections;
        } else {
            connections.sort(Comparator.comparingInt(Connection::getDistance));
            return connections.subList(0, maxConnections - 1);
        }
    }

    private void routingProcess() {

        Menu menu = new Menu();
        boolean exit = false;

        do {
            menu.showOptimizationMenu();
            if(menu.isOptionEmpty()) {
                menu.notifyEmptyOption();
            } else if(!menu.isOptionInt()) {
                menu.notifyNoIntOption();
            } else {
                int option = menu.getOptionInt();
                if(option != Graph.NO_OPTIMIZATION && option != Graph.AVL_OPTIMIZATION &&
                        option != Graph.HASH_OPTIMIZATION) {
                    menu.notifyInvalidIntRange();
                } else {
                    graph.setMode(option);
                    exit = true;
                }
            }
        } while(!exit);

        exit = false;
        do {
            menu.askForOrigin();
            if(!menu.isOptionEmpty()) {
                String from = menu.getOptionString();
                if(graph.containsCity(from)) {
                    City fromCity = graph.getCityData(from);
                    do {
                        menu.askForDestiny();
                        if(menu.isOptionEmpty()) {
                            menu.notifyEmptyOption();
                        } else {
                            String to = menu.getOptionString();
                            if(graph.containsCity(to)) {
                                calculateRoute(fromCity, graph.getCityData(to));
                            } else {
                                menu.notifyCityNotFound();
                            }
                            exit = true;
                        }
                    } while(menu.isOptionEmpty());
                } else {
                    menu.notifyCityNotFound();
                    exit = true;
                }
            } else {
                menu.notifyEmptyOption();
            }
        } while(!exit);

    }

    private void calculateRoute(City from, City to) {

        Menu menu = new Menu();
        boolean exit = false;

        do {
            menu.showRouteMenu();
            if(menu.isOptionEmpty()) {
                menu.notifyEmptyOption();
            } else if(!menu.isOptionInt()) {
                menu.notifyNoIntOption();
            } else {

                int option = menu.getOptionInt();
                Route route = null;
                initTime = System.nanoTime();

                switch(option) {
                    case SHORTEST:
                        route = graph.getShortestRoute(from, to);
                        if(route == null) {
                            menu.notifyUnreachableCity(from, to);
                            exit = true;
                        }
                        break;
                    case FASTEST:
                        route = graph.getFastestRoute(from, to);
                        if(route == null) {
                            menu.notifyUnreachableCity(from, to);
                            exit = true;
                        }
                        break;
                    default:
                        menu.notifyInvalidIntRange();
                        break;
                }

                menu.notifyOperationTime(System.nanoTime() - initTime);

                if(route != null) {
                    menu.showRoute(from, to, route);
                    exit = true;
                }

            }
        } while(!exit);

    }

}