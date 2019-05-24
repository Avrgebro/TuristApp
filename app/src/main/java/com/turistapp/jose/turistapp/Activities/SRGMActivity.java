package com.turistapp.jose.turistapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.turistapp.jose.turistapp.Model.RouteInstance;
import com.turistapp.jose.turistapp.R;

public class SRGMActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srgm);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        Bundle bundle = getIntent().getExtras();
        mRoute = bundle.getString("ruta");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng ll = new LatLng(-0.9681658,-80.7127556);

        CameraPosition pos = CameraPosition.builder().target(ll).zoom(14).build();

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));

        Gson gson = new Gson();

        RouteInstance l = gson.fromJson(mRoute, RouteInstance.class);

        for(PolylineOptions p : l.getLineoptions()){
            mMap.addPolyline(p);
        }

    }
}
