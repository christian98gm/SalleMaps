package model;

import Model.Connection;
import Network.DistanceParser;
import Network.GeocodeParser;
import Network.HttpRequest;
import Network.WSGoogleMaps;
import Model.City;
import model.list.CustomList;

import java.util.List;

public class GoogleManager {

    private final static String API_KEY = "AIzaSyAprP-uJv4hnhyd_6svpjTT3S75XEp-owo";

    public final static int OK = 0;
    public final static int KO = 1;

    private static GoogleManager instance;

    private WSGoogleMaps googleMaps;

    private GoogleManager() {
        googleMaps = WSGoogleMaps.getInstance();
        googleMaps.setApiKey(API_KEY);
    }

    public static GoogleManager getInstance() {
        if(instance == null) {
            instance = new GoogleManager();
        }
        return instance;
    }

    public void getNewCity(String cityName, CityCallback cityCallback) {
        googleMaps.geolocate(cityName, new HttpRequest.HttpReply() {
            @Override
            public void onSuccess(String data) {
                List<City> cities = GeocodeParser.getCityData(data);
                cityCallback.cityResult(cities.get(0), OK);
            }
            @Override
            public void onError(String message) {
                cityCallback.cityResult(null, KO);
            }
        });

    }

    public void getCloserDestinies(City city, List<City> cities, ConnectionsCallback connectionsCallback) {

        //Prepare data
        int indices[] = new int[cities.size()];
        double latitudes[] = new double[cities.size()];
        double longitudes[] = new double[cities.size()];
        for(int i = 0; i < cities.size(); i++) {
            indices[i] = i;
            City destination = cities.get(i);
            latitudes[i] = destination.getLatitude();
            longitudes[i] = destination.getLongitude();
        }

        //Get distances
        googleMaps.distance(city.getLatitude(), city.getLongitude(), latitudes, longitudes,
                new HttpRequest.HttpReply() {
                    @Override
                    public void onSuccess(String data) {
                        List<Connection> connections = new CustomList();
                        List<String> errors = DistanceParser.parseDistances(data, city.getName(), cities, indices,
                                connections);
                        if(!errors.isEmpty()) {
                            connectionsCallback.connectionsResult(null, null, KO);
                        } else {
                            connectionsCallback.connectionsResult(city, connections, OK);
                        }
                    }
                    @Override
                    public void onError(String message) {
                        connectionsCallback.connectionsResult(null, null, KO);
                    }
                });

    }

    public interface CityCallback {
        void cityResult(City city, int errorCode);
    }

    public interface ConnectionsCallback {
        void connectionsResult(City from, List<Connection> connections, int errorCode);
    }

}