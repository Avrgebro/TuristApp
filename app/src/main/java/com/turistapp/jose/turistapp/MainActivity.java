package com.turistapp.jose.turistapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    final Fragment chatbotFragment = new Chatbot();
    final Fragment placesFragment = new Places();
    final Fragment routeFragment = new Route();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = chatbotFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_chat:
                    fm.beginTransaction().hide(active).show(chatbotFragment).commit();
                    active = chatbotFragment;
                    return true;

                case R.id.navigation_places:
                    fm.beginTransaction().hide(active).show(placesFragment).commit();
                    active = placesFragment;
                    return true;

                case R.id.navigation_routes:
                    fm.beginTransaction().hide(active).show(routeFragment).commit();
                    active = routeFragment;
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

        fm.beginTransaction().add(R.id.mainfragment, routeFragment, "3").hide(routeFragment).commit();
        fm.beginTransaction().add(R.id.mainfragment, placesFragment, "2").hide(placesFragment).commit();
        fm.beginTransaction().add(R.id.mainfragment, chatbotFragment, "1").commit();
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
