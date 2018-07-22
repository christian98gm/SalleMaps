import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonManager {

    //City params
    private final static String CITIES = "cities";
    private final static String CITY_NAME = "name";
    private final static String CITY_ADDRESS = "address";
    private final static String CITY_COUNTRY = "country";
    private final static String CITY_LATITUDE = "latitude";
    private final static String CITY_LONGITUDE = "longitude";

    //Connection params
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
        List<City> cities = new ArrayList<>();

        //Get data
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            City city = new City();
            city.setName(jsonObject.getString(CITY_NAME));
            city.setAddress(jsonObject.getString(CITY_ADDRESS));
            city.setCountry(jsonObject.getString(CITY_COUNTRY));
            city.setLatitude(jsonObject.getDouble(CITY_LATITUDE));
            city.setLongitude(jsonObject.getDouble(CITY_LONGITUDE));
            cities.add(city);
        }

        return cities;

    }

    public List<Connection> getConnections() {

        //Prepare data
        JSONArray jsonArray = new JSONObject(content).getJSONArray(CONNECTIONS);
        List<Connection> connections = new ArrayList<>();

        //Get data
        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            Connection connection = new Connection();
            connection.setFrom(jsonObject.getString(CONNECTION_FROM));
            connection.setTo(jsonObject.getString(CONNECTION_TO));
            connection.setDistance(jsonObject.getLong(CONNECTION_DISTANCE));
            connection.setDuration(jsonObject.getLong(CONNECTION_DURATION));
            connections.add(connection);
        }

        return connections;

    }

}