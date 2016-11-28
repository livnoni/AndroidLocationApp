package com.example.yehuda.androidlocationapp;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity
{

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

    }
    @Override
    public void onResume()
    {
        super.onResume();
        setUpMapIfNeeded();
    }
    public void setUpMapIfNeeded()
    {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Marker"));
    }
}
