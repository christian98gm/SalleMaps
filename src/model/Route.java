package model;

import Model.City;
import model.list.CustomList;

import java.util.List;

public class Route {

    private List<City> cities;
    private int distance;   //meters
    private int duration;   //seconds

    public Route() {
        cities = new CustomList();
    }

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}