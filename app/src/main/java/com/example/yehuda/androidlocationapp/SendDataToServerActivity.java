package com.example.yehuda.androidlocationapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SendDataToServerActivity extends AppCompatActivity {
    double Altitude,Latitude,Longitude;
    String model;
    TextView altitude,latitude,longitude,result;
    Button buttonSendData,ButtonGetData;
    Switch toggleButtonAutomaticallySendData;

    com.android.volley.RequestQueue requestQueue;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private Thread thread;

    //for my local phpmyadmin:
    // String insertUrl = "http://10.0.0.2/tutorial/insertStudent.php";
    // String showUrl = "http://10.0.0.2/tutorial/showStudents.php";
    String insertUrl = "http://yehudalocation.000webhostapp.com/location/insertLocation.php";
    String showUrl = "http://yehudalocation.000webhostapp.com/location/showLocations.php";
    //go to http://yehudalocation.000webhostapp.com/location/ and see all the php files

    private static final int minTime = 1000; //min time in milliseconds to show new gps single
    private static final int minDistance = 0; //min distance (in meters), to show new gps single

     Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            Log.d("sendToServer() ", "-------------------");
            sendToServer();
        }
    };
    boolean send=false;

    Runnable runnable = new Runnable() {
        @Override
        public void run()
        {
            //while(true)
            {
                synchronized (this)
                {
                    while(send)
                    {
                        try {
                            wait(5000);
                            handler.sendEmptyMessage(0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
    };
    Thread t = new Thread(runnable);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_data_to_server);

        altitude = (TextView) findViewById(R.id.altitude);
        latitude = (TextView) findViewById(R.id.latitude);
        longitude = (TextView) findViewById(R.id.longitude);
        result = (TextView) findViewById(R.id.text_field_from_server);
        result.setMovementMethod(new ScrollingMovementMethod()); //alow to scoll down if the text is long...

        buttonSendData = (Button) findViewById(R.id.button_send_data);
        ButtonGetData = (Button) findViewById(R.id.button_data_from_server);
        toggleButtonAutomaticallySendData = (Switch) findViewById(R.id.button_automatically_send_data);
        toggleButtonAutomaticallySendData.setBackgroundColor(Color.GREEN);

        model = Build.MODEL;
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        turnOnGps();
        onClickButtonSendDataToServer();
        onClickButtonShowDataFromServer();
        onClickButtonAutomaticallySendData();

    }




    public void turnOnGps()
    {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
                Altitude = location.getAltitude();

                altitude.setText("altitude: "+Altitude);
                latitude.setText("Lltitude: "+Latitude);
                longitude.setText("altitude: "+Longitude);

               //send data to server
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                //GO to gps SETTING:
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        configure_button();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }
    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        locationManager.requestLocationUpdates("gps", minTime, minDistance, locationListener);
    }

    public void onClickButtonSendDataToServer()
    {
        buttonSendData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                sendToServer();
                Toast.makeText(getApplicationContext(),"Location Sent !", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void sendToServer()
    {
        StringRequest request = new StringRequest(Request.Method.POST, insertUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {

                    }
                }, new Response.ErrorListener()
        {

            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("latitude", String.valueOf(Latitude));
                parameters.put("longitude", String.valueOf(Longitude));
                parameters.put("altitude", String.valueOf(Altitude));
                parameters.put("model", String.valueOf(model));
                parameters.put("date", String.valueOf(DateFormat.getDateTimeInstance().format(new Date())));

                return parameters;
            }
        };
        Log.i("the model is", model);

        requestQueue.add(request);
    }
    public void onClickButtonShowDataFromServer()
    {
        ButtonGetData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        showUrl,new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        result.setText("");
                        try {
                            JSONArray locations = response.getJSONArray("Locations");
                            for(int i=0;i<locations.length();i++)
                            {
                                JSONObject student  = locations.getJSONObject(i);

                                String latitudejson = student.getString("latitude");
                                String longitudejson = student.getString("longitude");
                                String modeljson = student.getString("model");

                                result.append(modeljson+" : "+latitudejson+"/"+longitudejson+"\n");
                            }
                            result.append("==========================\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                requestQueue.add(jsonObjectRequest);
            }
        });

    }
    public void onClickButtonAutomaticallySendData()
    {
        toggleButtonAutomaticallySendData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton cb, boolean on)
            {

                if (on)
                {
                    //Do something when Switch button is on/checked
                    toggleButtonAutomaticallySendData.setText("STOP SENDING DATA !");
                    toggleButtonAutomaticallySendData.setBackgroundColor(Color.RED);
                    send=true;
                    Thread t = new Thread(runnable);
                    t.start();

                } else
                {
                    //Do something when Switch is off/unchecked
                    toggleButtonAutomaticallySendData.setText("AUTOMATICALLY SEND DATA");
                    toggleButtonAutomaticallySendData.setBackgroundColor(Color.GREEN);
                    send =false;
                    t.interrupt();


                }
            }
        });


    }



}
