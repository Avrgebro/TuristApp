package com.turistapp.jose.turistapp.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.turistapp.jose.turistapp.R;


public class Route extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Routes Fragment";

    private OnFragmentInteractionListener mListener;
    private SlidingUpPanelLayout slidingLayout;
    private LinearLayout dzone;

    MapView mapView;
    GoogleMap googleMap;
    View view;

    public Route() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_route, container, false);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slidingLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        dzone = (LinearLayout) view.findViewById(R.id.dragzone);
        slidingLayout.setDragView(dzone);

        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());



        LatLng ll = new LatLng(-12.1064917,-76.9674603);


        googleMap.addMarker(new MarkerOptions().position(ll));

        CameraPosition pos = CameraPosition.builder().target(ll).zoom(16).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
