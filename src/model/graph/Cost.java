package model.graph;

public class Cost {

    private int cost;
    private int distance;
    private int duration;

    public Cost() {
        cost = 0;
        distance = 0;
        duration = 0;
    }

    public Cost(Cost cost1, Cost cost2) {
        cost = cost1.cost + cost2.cost;
        distance = cost1.distance + cost2.distance;
        duration = cost1.duration + cost2.duration;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
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

    public void setUndefined() {
        cost = -1;
        distance = -1;
        duration = -1;
    }

    @Override
    public String toString() {
        return String.valueOf(cost);
    }

}