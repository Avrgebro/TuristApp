package com.turistapp.jose.turistapp.MapsUtils;

import android.support.annotation.StringDef;

import com.google.gson.JsonObject;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class Directions {

    private static final String DIRECTIONS_API_KEY = "AIzaSyDJ09WeQGy-A5sKgrMiZhmtQSPszo6MR3M";
    private static final String REQUEST_BASE_URL = "https://maps.googleapis.com/maps/api/directions/json";

    @Retention(RetentionPolicy.SOURCE)

    @StringDef({DRIVING_MODE, WALKING_MODE})

    public @interface DirectionsMode {}

    public static final String DRIVING_MODE = "driving";
    public static final String WALKING_MODE = "walking";



    /**********************************************************************
     * Metodo para obtener la ruta entre 2 coordenadas                    *
     * origin: coordenada inicial                                         *
     * destination: coordenada final                                      *
     * time: tiempo de salida del viaje                                   *
     * mode: a pie o en carro                                             *
     *                                                                    *
     * Jose Bejarano 26/03/2019                                           *
     **********************************************************************/
    public static JsonObject requestDirection(String origin, String destination, int time, @DirectionsMode String mode){

        JsonObject direction = null;

        String origin_param = "?origin=" + origin;
        String destination_param = "?destination=" + destination;
        String mode_param = mode == null ? "" : "?mode=" + mode;
        String time_param = "?departure_time=" + (time == 0 ? "now" : time);

        String request_url = REQUEST_BASE_URL + origin_param + destination_param + mode_param + time_param + "?key=" + DIRECTIONS_API_KEY;

        return direction;


    }
}
