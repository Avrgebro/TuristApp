package com.turistapp.jose.turistapp.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.turistapp.jose.turistapp.Activities.SRMActivity;
import com.turistapp.jose.turistapp.Adapters.SavedRouteAdapter;
import com.turistapp.jose.turistapp.Model.RouteInstance;
import com.turistapp.jose.turistapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SavedRoutes extends Fragment {

    private static final String TAG = "SavedRoutes Fragment";
    private static final String SP_NAME = "savedroutes";

    View view;

    public SavedRoutes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_saved_routes, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView srlist = (ListView) view.findViewById(R.id.savedroutelist);

        SharedPreferences sp = getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed;
        Gson gson = new Gson();

        String name = sp.getString("route_list", "[]");

        List<RouteInstance> l = gson.fromJson(name, new TypeToken<List<RouteInstance>>(){}.getType());

        SavedRouteAdapter adapter = new SavedRouteAdapter(getActivity(), l);

        srlist.setAdapter(adapter);


        srlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RouteInstance sel = adapter.getItem(position);

                Intent i = new Intent(getActivity(), SRMActivity.class);
                startActivity(i);
            }
        });

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }

}
