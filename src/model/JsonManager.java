package model;

import model.list.CustomList;
import Model.City;
import Model.Connection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class JsonManager {

    //model.City params
    private final static String CITIES = "cities";
    private final static String CITY_NAME = "name";
    private final static String CITY_ADDRESS = "address";
    private final static String CITY_COUNTRY = "country";
    private final static String CITY_LATITUDE = "latitude";
    private final static String CITY_LONGITUDE = "longitude";

    //model.Connection params
    private final static String CONNECTIONS = "connections";
    private final static String CONNECTION_FROM = "from";
    private final static String CONNECTION_TO = "to";
    private final static String CONNECTION_DISTANCE = "distance";
    private final static String CONNECTION_DURATION = "duration";

    //String content
    private String content;

    public JsonManager(String content) {
        this.content = content;
    }

    public List<City> getCities() throws JSONException {

        //Prepare data
        JSONArray jsonArray = new JSONObject(content).getJSONArray(CITIES);
        List cities = new CustomList();

        //Get data
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String name = jsonObject.getString(CITY_NAME);
            String address = jsonObject.getString(CITY_ADDRESS);
            String country = jsonObject.getString(CITY_COUNTRY);
            double latitude = jsonObject.getDouble(CITY_LATITUDE);
            double longitude = jsonObject.getDouble(CITY_LONGITUDE);
            cities.add(new City(name, address, country, latitude, longitude));
        }

        return cities;

    }

    public List<Connection> getConnections() {

        //Prepare data
        JSONArray jsonArray = new JSONObject(content).getJSONArray(CONNECTIONS);
        List connections = new CustomList();

        //Get data
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String from = jsonObject.getString(CONNECTION_FROM);
            String to = jsonObject.getString(CONNECTION_TO);
            int distance = jsonObject.getInt(CONNECTION_DISTANCE);
            int duration = jsonObject.getInt(CONNECTION_DURATION);
            connections.add(new Connection(from, to, distance, duration));
        }

        return connections;

    }

}