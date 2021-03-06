package com.turistapp.jose.turistapp.MapsUtils;

import android.support.annotation.StringDef;
import android.support.v4.util.Pair;

import com.google.android.gms.maps.model.LatLng;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class MapsUrlBuilder {

    private static final String DIRECTIONS_API_KEY = "AIzaSyDJ09WeQGy-A5sKgrMiZhmtQSPszo6MR3M";
    private static final String REQUEST_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    @Retention(RetentionPolicy.SOURCE)

    @StringDef({DRIVING_MODE, WALKING_MODE})

    public @interface DirectionsMode {}

    public static final String DRIVING_MODE = "driving";
    public static final String WALKING_MODE = "walking";


    private ArrayList<LatLng> coors;
    private long time;
    private String mode;
    private LatLng orig;

    public MapsUrlBuilder(ArrayList<LatLng> coors, long time, @DirectionsMode String mode, LatLng orig){
        this.coors = coors;
        this.time = time;
        this.mode = mode;
        this.orig = orig;
    }

    public String build() {

        String origin_param = "origin=" + orig.latitude + "," + orig.longitude;
        String destination_param = "&destination=" + orig.latitude + "," + orig.longitude;;
        String mode_param = "&mode=" + mode;
        String time_param = "&departure_time=" + (time == 0 ? "now" : time);
        String waypoints_param = "&waypoints=optimize:true";


        for(LatLng ll : coors) {
            waypoints_param = waypoints_param + "|" + ll.latitude + "," + ll.longitude;
        }



        String request_url = REQUEST_BASE_URL + origin_param + destination_param + waypoints_param + mode_param + time_param + "&key=" + DIRECTIONS_API_KEY;

        return request_url;
    }




}
