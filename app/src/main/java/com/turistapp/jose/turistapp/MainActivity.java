package com.turistapp.jose.turistapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.turistapp.jose.turistapp.Fragments.Chatbot;
import com.turistapp.jose.turistapp.Fragments.Places;
import com.turistapp.jose.turistapp.Fragments.Route;

public class MainActivity extends AppCompatActivity
implements Chatbot.OnFragmentInteractionListener, Route.OnFragmentInteractionListener{

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment frag;
            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    frag = new Chatbot();
                    inflateFragment(frag);
                    return true;

                case R.id.navigation_places:
                    frag = new Places();
                    inflateFragment(frag);
                    return true;

                case R.id.navigation_routes:
                    frag = new Route();
                    inflateFragment(frag);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        Fragment fragment = new Chatbot();
        inflateFragment(fragment);
    }

    @Override
    public void onFragmentInteraction(Uri uri){

    }

    private void inflateFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mainfragment, fragment);
            ft.commit();
        }
    }

}
