package com.turistapp.jose.turistapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.turistapp.jose.turistapp.Adapters.PlacesListAdapter;
import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.R;

import java.util.ArrayList;
import java.util.List;


public class Places extends Fragment {

    private OnFragmentInteractionListener mListener;

    private PlacesListAdapter adapter;

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

        ListView listView = (ListView) view.findViewById(R.id.places_list);
        adapter = new PlacesListAdapter(getActivity(), fakelist());
        listView.setAdapter(adapter);


    }

    private List<Place> fakelist() {
        List<Place> list = new ArrayList<>();

        Place p1 = new Place("Museo Cancebi", 123,123, "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2F2016-12-20.jpg?alt=media&token=ee11f173-11d4-47b3-8587-55c8bc07df66");
        Place p2 = new Place("Malecon del murcielago", 123, 123, "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2Fatardecer-entrando-al.jpg?alt=media&token=5de4c3e9-07e1-4eac-986d-c5e32c479abd");
        Place p3 = new Place("Playa Murcielago", 123, 123, "https://firebasestorage.googleapis.com/v0/b/trip-planner-pucp.appspot.com/o/Places%2FPlaya%20del%20Murci%C3%A9lago%20_1.jpg?alt=media&token=b8aed93c-c660-40fc-be40-940563dfd333");

        list.add(p1);
        list.add(p2);
        list.add(p3);

        return list;
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
