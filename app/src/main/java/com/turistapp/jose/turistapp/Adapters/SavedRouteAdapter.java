package com.turistapp.jose.turistapp.Adapters;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.Model.RouteInstance;
import com.turistapp.jose.turistapp.Model.RouteSegment;
import com.turistapp.jose.turistapp.R;

import java.util.ArrayList;
import java.util.List;

public class SavedRouteAdapter extends ArrayAdapter<RouteInstance> {

    private Context mcontext;

    public SavedRouteAdapter(Context context, List<RouteInstance> objects) {
        super(context, R.layout.saved_route_item, objects);

        mcontext = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.saved_route_item, parent, false);

            holder = new ViewHolder();



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        RouteInstance cur = getItem(position);

        TextView name = (TextView) convertView.findViewById(R.id.namesr);
        name.setText("Ruta Guardada " + (position+1));



        if(cur != null){


        }


        return convertView;

    }

    private class ViewHolder {
        TextView name;
    }
}
