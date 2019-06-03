package com.turistapp.jose.turistapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.turistapp.jose.turistapp.Model.Place;
import com.turistapp.jose.turistapp.Model.RouteInstance;
import com.turistapp.jose.turistapp.R;

public class PlaceInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_info);

        Bundle bundle = getIntent().getExtras();
        String ps = bundle.getString("place");

        Gson gson = new Gson();

        Place l = gson.fromJson(ps, Place.class);

        ImageView image = (ImageView) findViewById(R.id.infoimage);

        RequestOptions reqOpt = RequestOptions
                .fitCenterTransform()
                .transform(new RoundedCorners(5))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(image.getWidth(),image.getHeight());

        Glide.with(this).load(l.getImgurl()).thumbnail(/*sizeMultiplier=*/ 0.25f)
                .apply(reqOpt).centerCrop().into(image);

        TextView text = (TextView) findViewById(R.id.infotext);
        text.setText(l.getDescripcion());

        TextView hours = (TextView) findViewById(R.id.infohours);

        Place.Horario h = l.getHorario();

        hours.setText("De " + h.apertura + ":00 a " + h.cierre + ":00");
    }
}
