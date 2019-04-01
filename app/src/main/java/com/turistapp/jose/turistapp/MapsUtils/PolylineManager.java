package com.turistapp.jose.turistapp.MapsUtils;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class PolylineManager {

    //members
    private List<LatLng> decoded;
    private String encoded;

    //constructors
    public PolylineManager(List<LatLng> decoded) {
        this.setDecoded(decoded);
        this.setEncoded("");
    }

    public PolylineManager(String encoded) {
        this.setEncoded(encoded);
        this.setDecoded(null);
    }

    public PolylineManager(){
        this.setDecoded(null);
        this.setEncoded("");
    }

    //methods
    public void decode() {
        if(this.decoded == null) {

            String poly = this.encoded;

            int len = poly.length();
            int index = 0;
            List<LatLng> decoded = new ArrayList<LatLng>();
            int lat = 0;
            int lng = 0;

            while (index < len){
                int b;
                int shift = 0;
                int result = 0;
                do{
                    b = poly.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                } while (b >= 0x20);
                int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                lat += dlat;

                shift = 0;
                result = 0;
                do {
                    b = poly.charAt(index++) - 63;
                    result |= (b & 0x1f) << shift;
                    shift += 5;
                }while (b >= 0x20);
                int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >>1));
                lng += dlng;

                decoded.add(new LatLng(
                        lat / 100000d,
                        lng / 100000d
                ));
            }

            this.decoded = decoded;
        }
    }

    public List<LatLng> decode(String poly) {

        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;

        while (index < len){
            int b;
            int shift = 0;
            int result = 0;
            do{
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >>1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d,
                    lng / 100000d
            ));
        }

        return decoded;
    }

    public List<LatLng> decode_multiple(ArrayList<String> polys){
        List<LatLng> ret = new ArrayList<LatLng>();

        for(String p : polys) {
            ret.addAll(this.decode(p));
        }

        return ret;
    }

    //accessors and mutators
    public List<LatLng> getDecoded() {
        return decoded;
    }

    public void setDecoded(List<LatLng> decoded) {
        this.decoded = decoded;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }
}
