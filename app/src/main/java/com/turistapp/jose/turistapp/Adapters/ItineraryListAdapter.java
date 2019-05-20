package com.turistapp.jose.turistapp.Adapters;

import android.content.Context;

import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.mikhaellopez.circularimageview.CircularImageView;

import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.Model.RouteSegment;
import com.turistapp.jose.turistapp.R;

import java.util.ArrayList;
import java.util.List;

public class ItineraryListAdapter extends ArrayAdapter<Pair<RouteSegment, String>> {

    private Context mcontext;
    public ItineraryListAdapter(Context context, ArrayList<Pair<RouteSegment, String>> objects) {
        super(context, 0, objects);

        mcontext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;


        if (convertView == null) {
            convertView = LayoutInflater.from(mcontext).inflate(R.layout.places_item, parent, false);

            holder = new ViewHolder();
            holder.location = (TextView) convertView.findViewById(R.id.placename);
            holder.distance = (TextView) convertView.findViewById(R.id.distanceto);
            holder.duration = (TextView) convertView.findViewById(R.id.timeto);
            holder.summary = (LinearLayout) convertView.findViewById(R.id.summary);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }

        Pair<RouteSegment, String> cur = getItem(position);

        if(cur != null){
            holder.location.setText(cur.second);

            if(cur.first.getSegments() == null){
                holder.summary.setVisibility(View.GONE);
            } else {

                holder.duration.setText(cur.first.getTime()+"");


                holder.distance.setText(cur.first.getDistance()+"");
            }

            holder.duration.setText(cur.first.getTime()+"");

            holder.distance.setText(cur.first.getDistance()+"");
        }


        return convertView;

    }

    private class ViewHolder {
       TextView duration;
       TextView distance;
       TextView location;

       View summary;
    }

}
