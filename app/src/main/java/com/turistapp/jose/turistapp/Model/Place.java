package com.turistapp.jose.turistapp.Model;


import com.google.android.gms.maps.model.LatLng;

public class Place {
    private int id;
    private String name;
    private LatLng coordinates;
    private String imgurl;
    private Horario horario;
    private String descripcion;

    public Place() {
    }

    public Place(String name, LatLng coordinates, String imgurl, int apertura, int cierre) {
        this.name = name;
        this.coordinates = coordinates;
        this.imgurl = imgurl;
        this.horario = new Horario();
        this.horario.apertura = apertura;
        this.horario.cierre = cierre;
    }

    public Place(int id, String name, LatLng coordinates, String imgurl, int apertura, int cierre, String descripcion) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.imgurl = imgurl;
        this.horario = new Horario();
        this.horario.apertura = apertura;
        this.horario.cierre = cierre;
        this.descripcion = descripcion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public class Horario {
        public int apertura;
        public int cierre;

        public Horario(){}
    }
}
