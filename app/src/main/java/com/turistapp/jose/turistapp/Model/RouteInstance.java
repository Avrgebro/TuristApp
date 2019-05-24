package com.turistapp.jose.turistapp.Model;

import android.os.Parcelable;

import com.google.android.gms.maps.model.PolylineOptions;

import java.io.Serializable;
import java.util.List;

public class RouteInstance {

    private String routename;
    private List<PolylineOptions> lineoptions;

    public RouteInstance(String routename, List<PolylineOptions> lineoptions) {
        this.setRoutename(routename);
        this.setLineoptions(lineoptions);
    }


    public String getRoutename() {
        return routename;
    }

    public void setRoutename(String routename) {
        this.routename = routename;
    }

    public List<PolylineOptions> getLineoptions() {
        return lineoptions;
    }

    public void setLineoptions(List<PolylineOptions> lineoptions) {
        this.lineoptions = lineoptions;
    }
}
