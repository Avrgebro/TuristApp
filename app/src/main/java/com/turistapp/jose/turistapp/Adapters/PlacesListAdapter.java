package com.turistapp.jose.turistapp.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.turistapp.jose.turistapp.Fragments.Places;
import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.R;

import java.util.List;

public class PlacesListAdapter extends ArrayAdapter<Place> {

    private final LayoutInflater mInflater;


    public PlacesListAdapter(Context context, List<Place> objects) {
        super(context, R.layout.places_item, objects);
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.places_item, parent, false);

            holder = new ViewHolder();
            holder.placeimage = (CircularImageView) convertView.findViewById(R.id.locationimage_pilayout);
            holder.addbtn = (TextView) convertView.findViewById(R.id.addbtn_pilayout);
            holder.delbtn = (TextView) convertView.findViewById(R.id.delbtn_pilayout);
            holder.placename = (TextView) convertView.findViewById(R.id.locationname_pilayout);
            holder.selected = (ImageView) convertView.findViewById(R.id.selected_pilayout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Place item = getItem(position);
        if (item != null) {

            holder.placename.setText(item.getName());

            RequestOptions reqOpt = RequestOptions
                    .fitCenterTransform()
                    .transform(new RoundedCorners(5))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .override(holder.placeimage.getWidth(),holder.placeimage.getHeight());

            Glide.with(getContext()).load(item.getImgurl()).thumbnail(/*sizeMultiplier=*/ 0.25f)
                    .apply(reqOpt).centerCrop().into(holder.placeimage);

            holder.addbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            holder.delbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //quitar del recorrido
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        CircularImageView placeimage;
        View addbtn;
        View delbtn;
        TextView placename;
        ImageView selected;
    }
}
