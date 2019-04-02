package com.turistapp.jose.turistapp.Fragments;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.turistapp.jose.turistapp.MapsUtils.PolylineManager;
import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.Model.RouteSegment;
import com.turistapp.jose.turistapp.R;

import java.util.ArrayList;
import java.util.List;


public class Route extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Routes Fragment";

    private OnFragmentInteractionListener mListener;
    private SlidingUpPanelLayout slidingLayout;
    private LinearLayout dzone;
    private ImageView arrow;

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

        mapView = (MapView) view.findViewById(R.id.map);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slidingLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);
        dzone = (LinearLayout) view.findViewById(R.id.dragzone);
        slidingLayout.setDragView(dzone);



        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                arrow = (ImageView) view.findViewById(R.id.arrow);
                arrow.setRotation(180*slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());

        LatLng ll = new LatLng(-0.9681658,-80.7127556);

        CameraPosition pos = CameraPosition.builder().target(ll).zoom(14).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos));

        this.googleMap = googleMap;
    }

    public void processRoute(ArrayList<RouteSegment> segments, Place origin, ArrayList<Place> waypoints){
        PolylineManager pmanager = new PolylineManager();

        String iconbase = "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=";
        String iconcolor= "|FE6256|000000";

        for(RouteSegment rs : segments){

            List<LatLng> aux = pmanager.decode_multiple(rs.getSegments());
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(aux);
            lineOptions.width(10);
            lineOptions.color(Color.BLUE);

            googleMap.addPolyline(lineOptions);


        }

        waypoints.add(0, origin);
        int i = 0;
        for(Place p : waypoints){

            String url = iconbase + (i+1) + iconcolor;

            Glide.with(getActivity())
                    .asBitmap()
                    .load(url)
                    .fitCenter()
                    .override(100, 100)
                    .into(new Target<Bitmap>() {

                        @Override
                        public void onLoadStarted(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                            googleMap.addMarker(new MarkerOptions()
                                    .position(p.getCoordinates())
                                    .title(p.getName())
                                    .icon(BitmapDescriptorFactory.fromBitmap(resource)));
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            googleMap.addMarker(new MarkerOptions()
                                    .position(p.getCoordinates())
                                    .title(p.getName()));
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }

                        @Override
                        public void getSize(@NonNull SizeReadyCallback cb) {
                            cb.onSizeReady(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                        }

                        @Override
                        public void removeCallback(@NonNull SizeReadyCallback cb) {

                        }

                        @Override
                        public void setRequest(@Nullable Request request) {

                        }

                        @Nullable
                        @Override
                        public Request getRequest() {
                            return null;
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onStop() {

                        }

                        @Override
                        public void onDestroy() {

                        }
                    });
            i++;
        }

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
