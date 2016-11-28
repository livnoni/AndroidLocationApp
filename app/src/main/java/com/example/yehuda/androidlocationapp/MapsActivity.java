package com.example.yehuda.androidlocationapp;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        //ariel = 32.104954, 35.207730
        LatLng Ariel = new LatLng(32.104954, 35.207730);
        mMap.addMarker(new MarkerOptions().position(Ariel).title("Marker in Ariel"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Ariel));
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
