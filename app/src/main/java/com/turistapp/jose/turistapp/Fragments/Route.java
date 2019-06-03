package com.turistapp.jose.turistapp.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.common.primitives.Ints;
import com.google.gson.reflect.TypeToken;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.turistapp.jose.turistapp.Adapters.ItineraryListAdapter;
import com.turistapp.jose.turistapp.MapsUtils.PolylineManager;
import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.Model.RouteInstance;
import com.turistapp.jose.turistapp.Model.RouteSegment;
import com.turistapp.jose.turistapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;


public class Route extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Routes Fragment";
    private static final String SP_NAME = "savedroutes";

    private OnFragmentInteractionListener mListener;
    private SlidingUpPanelLayout slidingLayout;
    private LinearLayout dzone;
    private ImageView arrow;
    private Button save;
    private RouteInstance rinstance;

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

        save = (Button) view.findViewById(R.id.savebtn);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor ed;
                Gson gson = new Gson();
                List<RouteInstance> l = null;

                if(!sp.contains("route_list")){

                    l = new ArrayList<>();
                    l.add(rinstance);

                } else {

                    String name = sp.getString("route_list", "[]");

                    l = gson.fromJson(name, new TypeToken<List<RouteInstance>>(){}.getType());
                    l.add(rinstance);
                }

                ed = sp.edit();

                String routesJson = gson.toJson(l);

                ed.putString("route_list", routesJson);

                ed.apply();

                save.setEnabled(false);
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

    public void processRoute(ArrayList<RouteSegment> segments, Place origin, ArrayList<Place> waypoints, int[] waypoint_order){
        googleMap.clear();
        PolylineManager pmanager = new PolylineManager();

        String iconbase = "http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=";
        String iconcolor= "|FE6256|000000";

        List<PolylineOptions> lops = new ArrayList<>();

        for(RouteSegment rs : segments){

            List<LatLng> aux = pmanager.decode_multiple(rs.getSegments());
            PolylineOptions lineOptions = new PolylineOptions();
            lineOptions.addAll(aux);
            lineOptions.width(8);
            lineOptions.color(getContext().getResources().getColor(R.color.colorPrimaryDark));

            /*PolylineOptions lineOptionsinner = new PolylineOptions();
            lineOptionsinner.addAll(aux);
            lineOptionsinner.width(6);
            lineOptionsinner.color(getContext().getResources().getColor(R.color.colorPrimaryDark));*/

            List<PatternItem> pattern = Arrays.<PatternItem>asList(
                    new Dash(30), new Dot(), new Gap(10));

            lineOptions.pattern(pattern);

            googleMap.addPolyline(lineOptions);
            //googleMap.addPolyline(lineOptionsinner);

            lops.add(lineOptions);
            save.setEnabled(true);

        }

        rinstance = new RouteInstance("Ruta sin nombre", lops );

        ArrayList<Place> ordered = new ArrayList<>();
        for(int i=0; i < waypoint_order.length; i++){
            ordered.add(waypoints.get(Ints.indexOf(waypoint_order, i)));
        }

        waypoints = ordered;

        waypoints.add(0, origin);
        int i = 0;
        for(Place p : waypoints){

            String url = iconbase + (i+1) + iconcolor;

            Glide.with(getActivity())
                    .asBitmap()
                    .load(p.getImgurl())
                    .fitCenter()
                    .apply(RequestOptions.circleCropTransform())
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

        setItinerary(segments, waypoints, waypoint_order);
    }

    public void setItinerary(ArrayList<RouteSegment> segments, ArrayList<Place> waypoints, int[] waypoint_order){



        segments.add(new RouteSegment(null, 0, 0));

        ArrayList<Pair<RouteSegment, String>> aux = new ArrayList<>();

        for(int i = 0; i < waypoints.size(); i++){

            Pair<RouteSegment, String> p = new Pair<RouteSegment, String>(segments.get(i), waypoints.get(i).getName());

            aux.add(p);
        }

        ListView itinerary = (ListView) view.findViewById(R.id.itinerarylist);
        ItineraryListAdapter iadapter = new ItineraryListAdapter(getActivity(), aux);
        itinerary.setAdapter(iadapter);
    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}
