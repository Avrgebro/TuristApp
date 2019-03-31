package com.turistapp.jose.turistapp.Fragments;

import android.Manifest;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    //widgets
    private LottieAnimationView animview;
    private Button genbtn;
    private TextView loadingtxt;
    private RelativeLayout loadinglayout;
    private RelativeLayout controlslayout;
    private SwitchCompat gpsswitch;
    private IconSwitch modeswitch;

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

        ListView listView = (ListView) view.findViewById(R.id.places_list);
        adapter = new PlacesListAdapter(getActivity(), fakelist());
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
                loadinglayout.setVisibility(View.VISIBLE);
                controlslayout.setVisibility(View.GONE);

                generateroute();


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

    private List<Place> fakelist() {
        List<Place> list = new ArrayList<>();

        Place p1 = new Place("Museo Cancebi", new LatLng(-0.9500534,-80.7202813), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2F2016-12-20.jpg?alt=media&token=ee11f173-11d4-47b3-8587-55c8bc07df66");
        Place p2 = new Place("Malecon del murcielago", new LatLng(-0.9437843,-80.7306949), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2Fatardecer-entrando-al.jpg?alt=media&token=5de4c3e9-07e1-4eac-986d-c5e32c479abd");
        Place p3 = new Place("Playa Murcielago", new LatLng(-0.9414055,-80.7368706), "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2FPlaya%20del%20Murci%C3%A9lago%20_1.jpg?alt=media&token=b8aed93c-c660-40fc-be40-940563dfd333");

        list.add(p1);
        list.add(p2);
        list.add(p3);

        return list;
    }


    private void generateroute() {

        ArrayList<LatLng> coordinates = new ArrayList<>();

        for(Place p : adapter.getSelected()) {
            coordinates.add(p.getCoordinates());
        }

        LatLng orig;

        if(gpsswitch.isChecked()){//se crea un latlong con la ubicacion actual del usuario y se agega coordinates
            Location gps_lock = null;

            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                gps_lock = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            orig = new LatLng(gps_lock.getLatitude(), gps_lock.getLongitude());

            coordinates.add(orig);

        } else {
            orig = adapter.getOrigin().getCoordinates();
        }

        int oIndex = coordinates.indexOf(orig);


        String mode = "";
        switch (modeswitch.getChecked()) {
            case LEFT:
                mode = DRIVING_MODE;
                break;
            case RIGHT:
                mode = WALKING_MODE;
                break;
        }

        String reqURL = new MapsUrlBuilder(coordinates, 0 , mode, oIndex).build();

        Log.i(TAG, reqURL);




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
