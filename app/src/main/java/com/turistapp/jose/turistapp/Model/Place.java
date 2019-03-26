package com.turistapp.jose.turistapp.Model;

public class Place {
    private String name;
    private float latitude;
    private float longitude;
    private String imgurl;

    public Place() {
    }

    public Place(String name, float latitude, float longitude, String imgurl) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }
}
