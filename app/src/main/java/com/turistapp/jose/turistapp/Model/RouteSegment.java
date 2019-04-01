package com.turistapp.jose.turistapp.Model;

import java.util.ArrayList;

public class RouteSegment {
    private ArrayList<String> segments;
    private int distance;
    private int time;

    public RouteSegment(ArrayList<String> segments, int distance, int time) {
        this.setSegments(segments);
        this.setDistance(distance);
        this.setTime(time);
    }

    public ArrayList<String> getSegments() {
        return segments;
    }

    public void setSegments(ArrayList<String> segments) {
        this.segments = segments;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
