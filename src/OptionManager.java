import Model.City;
import Model.Connection;
import model.DestinationCity;
import model.GoogleManager;
import model.JsonManager;
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

    //Managers
    private Graph graph;
    private GoogleManager googleManager;

    public OptionManager() {
        googleManager = GoogleManager.getInstance();
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

                //Check if requested city exists
                if(!graph.containsCity(menu.getOptionString())) {
                    googleManager.getNewCity(menu.getOptionString(), this);
                } else {
                    showCityData(graph.getCityData(menu.getOptionString()));
                }

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
                googleManager.getCloserDestinies(city, cities, this);
                break;
            case GoogleManager.KO:
                Menu menu = new Menu();
                menu.notifyCityNotFound();
                break;
        }
    }

    @Override
    public void connectionsResult(City from, List<Connection> connections, int errorCode) {
        Menu menu = new Menu();
        switch(errorCode) {
            case GoogleManager.OK:
                graph.addCity(from);
                List<Connection> closerConnections = getCloserConnections(connections, MAX_CONNECTIONS);
                for(int i = 0; i < closerConnections.size(); i++) {
                    Connection connection = closerConnections.get(i);
                    Connection returnConnection = new Connection(connection.getTo(), from.getName(),
                            connection.getDistance(), connection.getDuration());
                    graph.addConnection(connection);
                    graph.addConnection(returnConnection);
                }
                menu.notifyCityAdded();
                showCityData(from);
                break;
            case GoogleManager.KO:
                menu.notifyConnectionsError();
                break;
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

                if(route != null) {
                    menu.showRoute(from, to, route);
                    exit = true;
                }

            }
        } while(!exit);

    }

}