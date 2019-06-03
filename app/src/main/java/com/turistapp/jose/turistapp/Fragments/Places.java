package com.turistapp.jose.turistapp.Fragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.maps.model.LatLng;
import com.google.api.Distribution;
import com.polyak.iconswitch.IconSwitch;
import com.turistapp.jose.turistapp.Activities.PlaceInfoActivity;
import com.turistapp.jose.turistapp.Adapters.PlacesListAdapter;
import com.turistapp.jose.turistapp.MainActivity;
import com.turistapp.jose.turistapp.MapsUtils.MapsUrlBuilder;
import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.R;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.polyak.iconswitch.IconSwitch.Checked.LEFT;
import static com.polyak.iconswitch.IconSwitch.Checked.RIGHT;
import static com.turistapp.jose.turistapp.MapsUtils.MapsUrlBuilder.DRIVING_MODE;
import static com.turistapp.jose.turistapp.MapsUtils.MapsUrlBuilder.WALKING_MODE;


public class Places extends Fragment {

    private static final String TAG = "Routes Fragment";

    private static final int FINE_LOCATION_REQUEST_RESULT = 1;

    private OnFragmentInteractionListener mListener;

    private PlacesListAdapter adapter;

    private LocationManager locationManager;

    private ArrayList<Place> _recommended;

    //widgets
    private LottieAnimationView animview;
    private Button genbtn;
    private TextView loadingtxt;
    private RelativeLayout loadinglayout;
    private RelativeLayout controlslayout;
    private SwitchCompat gpsswitch;
    private IconSwitch modeswitch;
    private ListView listView;

    private ImageView clock;
    private TextView timetxt;

    View view;



    public Places() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_places, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        _recommended = new ArrayList<Place>();

        listView = (ListView) view.findViewById(R.id.places_list);
        adapter = new PlacesListAdapter(getActivity(), _recommended);
        listView.setAdapter(adapter);

        animview = (LottieAnimationView) view.findViewById(R.id.pinjumpanimation);
        loadingtxt = (TextView) view.findViewById(R.id.loadinginfo);
        loadinglayout = (RelativeLayout) view.findViewById(R.id.loadinglayout);
        controlslayout = (RelativeLayout) view.findViewById(R.id.controlslayout);
        clock = (ImageView) view.findViewById(R.id.time);
        timetxt = (TextView) view.findViewById(R.id.timetext);
        gpsswitch = (SwitchCompat) view.findViewById(R.id.gpsswitch);
        modeswitch = (IconSwitch) view.findViewById(R.id.modeswitch);

        genbtn = (Button) view.findViewById(R.id.gen_btn);
        genbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(adapter.getSelected().size() < 2) {
                    Toast.makeText(getActivity(), "Debe seleccionar almenos dos lugares", Toast.LENGTH_SHORT).show();
                } else {
                    loadinglayout.setVisibility(View.VISIBLE);
                    controlslayout.setVisibility(View.GONE);

                    generateroute();
                }



            }
        });

        clock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String timeset = String.format("%02d", selectedHour)+":"+String.format("%02d", selectedMinute);
                        timetxt.setText(timeset);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.show();

            }
        });

        gpsswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_REQUEST_RESULT);
                    }

                    adapter.toggleOrigin(0);

                } else {
                    adapter.toggleOrigin(1);
                }

            }
        });

    }

    public void switchcontrols (int anim, int controls){
        if(anim == 1){
            loadinglayout.setVisibility(View.VISIBLE);
        } else {
            loadinglayout.setVisibility(View.GONE);
        }

        if(controls == 1){
            controlslayout.setVisibility(View.VISIBLE);
        } else {
            controlslayout.setVisibility(View.GONE);
        }
    }

    /*private List<Place> fakelist() {
        List<Place> list = new ArrayList<>();

        Place p1 = new Place("Museo Cancebi", new LatLng(-0.9476959,-80.7217458), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2F2016-12-20.jpg?alt=media&token=ee11f173-11d4-47b3-8587-55c8bc07df66", 9, 17);
        Place p2 = new Place("Malecon del murcielago", new LatLng(-0.9406871,-80.7322122), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2Fatardecer-entrando-al.jpg?alt=media&token=5de4c3e9-07e1-4eac-986d-c5e32c479abd", 0, 0);
        Place p3 = new Place("Playa Murcielago", new LatLng(-0.9399168,-80.7342732), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2FPlaya%20del%20Murci%C3%A9lago%20_1.jpg?alt=media&token=b8aed93c-c660-40fc-be40-940563dfd333", 0, 0);
        Place p4 = new Place("Monumento al Pescador", new LatLng(-0.9524816,-80.719479), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2Fman4.jpg?alt=media&token=c40fe927-317b-427e-83c6-1b852c055e34", 0, 0);
        Place p5 = new Place("El Paseo Shopping", new LatLng(-0.9664772,-80.7075729), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2FEl_paseo_shopping.jpg?alt=media&token=e7abf878-f1ec-47b7-897b-2c5e1385ef41", 10, 23);

        list.add(p1);
        list.add(p2);
        list.add(p3);
        list.add(p4);
        list.add(p5);

        return list;
    }*/

    public void setAdapter(ArrayList<Place> p){
        _recommended.clear();
        _recommended.addAll(p);
        adapter.notifyDataSetChanged();
    }


    private void generateroute() {



        loadingtxt.setText("Procesando Solicitud...");

        listView.setEnabled(false);

        ArrayList<LatLng> coordinates = new ArrayList<>();

        for(Place p : adapter.getSelected()) {
            coordinates.add(p.getCoordinates());
        }

        LatLng orig;

        if(gpsswitch.isChecked()){//se crea un latlong con la ubicacion actual del usuario y se agrega coordinates
            Location gps_lock = null;

            /*if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gps_lock = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }*/
            gps_lock = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            orig = new LatLng(gps_lock.getLatitude(), gps_lock.getLongitude());

            coordinates.add(orig);

        } else {
            orig = adapter.getOrigin().getCoordinates();
        }

        coordinates.remove(orig);


        String mode = "";
        switch (modeswitch.getChecked()) {
            case LEFT:
                mode = DRIVING_MODE;
                break;
            case RIGHT:
                mode = WALKING_MODE;
                break;
        }

        long timereq = 0;

        if(!timetxt.getText().toString().equals("--:--")){

            int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);

            if(hourOfDay < Integer.parseInt(timetxt.getText().toString().substring(0,2))){
                int dif = Integer.parseInt(timetxt.getText().toString().substring(0,2)) - hourOfDay;

                timereq = System.currentTimeMillis()/1000L + (dif * 3600);
            }
        }

        String reqURL = new MapsUrlBuilder(coordinates, timereq , mode, orig).build();

        //Log.i(TAG, reqURL); //URL generando correctamente


        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(reqURL).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //Fuck that
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //loadingtxt.setText("Optimizando ruta...");

                ArrayList<Place> waypoints = new ArrayList<>(adapter.getSelected());
                waypoints.remove(adapter.getOrigin());

                ((MainActivity)getActivity()).routesCallback(response.body(), adapter.getOrigin(), waypoints);

            }
        });

        ArrayList<Place> auxplace = adapter.getSelected();
        List<Integer> isel = new ArrayList<>();
        for(Place p : auxplace){
            isel.add(p.getId());
        }

        String values = ((MainActivity)getActivity()).datap + " " + isel.toString();

        request = new Request.Builder().url("https://us-central1-trip-planner-pucp.cloudfunctions.net/writeTrainingData/"+values).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //...
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //...
            }
        });



    }

    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_REQUEST_RESULT: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    gpsswitch.setChecked(false);
                }
                return;
            }

        }
    }
}
