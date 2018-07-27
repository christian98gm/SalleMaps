package model;

import Model.City;

public class DestinationCity {

    private City city;
    private int distance;  //meters
    private int duration;  //seconds

    public DestinationCity() {}

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
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