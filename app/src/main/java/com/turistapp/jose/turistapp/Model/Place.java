package com.turistapp.jose.turistapp.Model;


import com.google.android.gms.maps.model.LatLng;

public class Place {
    private String name;
    //private float latitude;
    //private float longitude;
    private LatLng coordinates;
    private String imgurl;
    private Horario horario;

    public Place() {
    }

    public Place(String name, LatLng coordinates, String imgurl, int apertura, int cierre) {
        this.name = name;
        this.coordinates = coordinates;
        this.imgurl = imgurl;
        this.horario.apertura = apertura;
        this.horario.cierre = cierre;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    */

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    class Horario {
        public int apertura;
        public int cierre;
    }
}
