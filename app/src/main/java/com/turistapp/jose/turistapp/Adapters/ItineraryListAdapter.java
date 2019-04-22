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


    public ItineraryListAdapter(Context context, ArrayList<Pair<RouteSegment, String>> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.itinerary_item, parent, false);
        }

        Pair<RouteSegment, String> cur = getItem(position);

        TextView location = (TextView) listItem.findViewById(R.id.placename);
        location.setText(cur.second);

        /*if(cur.first.getSegments() == null){
            LinearLayout summary = (LinearLayout) listItem.findViewById(R.id.summary);
            summary.setVisibility(View.GONE);
        } else {
            TextView duration = (TextView) listItem.findViewById(R.id.timeto);
            duration.setText(cur.first.getTime()+"");

            TextView distance = (TextView) listItem.findViewById(R.id.distanceto);
            distance.setText(cur.first.getDistance()+"");
        }*/

        TextView duration = (TextView) listItem.findViewById(R.id.timeto);
        duration.setText(cur.first.getTime()+"");

        TextView distance = (TextView) listItem.findViewById(R.id.distanceto);
        distance.setText(cur.first.getDistance()+"");


        return listItem;

    }

}
