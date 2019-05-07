package com.turistapp.jose.turistapp.Model;

import java.util.ArrayList;

import okhttp3.ResponseBody;

public class RouteInstance {

    private ResponseBody response;
    private Place origin;
    private ArrayList<Place> waypoints;

    public RouteInstance(ResponseBody response, Place origin, ArrayList<Place> waypoints) {
        this.setResponse(response);
        this.setOrigin(origin);
        this.setWaypoints(waypoints);
    }

    public ResponseBody getResponse() {
        return response;
    }

    public void setResponse(ResponseBody response) {
        this.response = response;
    }

    public Place getOrigin() {
        return origin;
    }

    public void setOrigin(Place origin) {
        this.origin = origin;
    }

    public ArrayList<Place> getWaypoints() {
        return waypoints;
    }

    public void setWaypoints(ArrayList<Place> waypoints) {
        this.waypoints = waypoints;
    }
}
