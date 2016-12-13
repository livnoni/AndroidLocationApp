package com.example.yehuda.androidlocationapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double Latitude;
    private double Longitude;
    private float zoomLevel = (float)16.0; //This goes up to 21

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        Log.i("check","----------------------");
        Log.i("check", "before: " + "Latitude=" + Latitude +"Longitude: "+Longitude);
        Log.i("check","----------------------");

        Latitude = intent.getDoubleExtra("Latitude",77);
        Longitude = intent.getDoubleExtra("Longitude",88);

        Log.i("check","----------------------");
        Log.i("check", "after: " + "Latitude=" + Latitude +"Longitude: "+Longitude);
        Log.i("check","----------------------");

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        LatLng NewLocation = new LatLng(Latitude, Longitude);
        mMap.addMarker(new MarkerOptions().position(NewLocation).title("Marker in "+Latitude+" / "+ Longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NewLocation, zoomLevel));
    }
}



//
//import android.support.v4.app.FragmentActivity;
//import android.os.Bundle;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//




//public class MapsActivity extends FragmentActivity
//{
//
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        setUpMapIfNeeded();
//
//    }
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        setUpMapIfNeeded();
//    }
//    public void setUpMapIfNeeded()
//    {
//        if(mMap==null)
//        {
//        }
//        else
//        {
//            setUpMap();
//        }
//        //mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
//    }
//    public void setUpMap()
//    {
//        //Ariel adress = 32.104300, 35.174771
//        mMap.addMarker(new MarkerOptions().position(new LatLng(32.104300, 35.174771)).title("This is yehuda first marker"));
//
//    }
//}
