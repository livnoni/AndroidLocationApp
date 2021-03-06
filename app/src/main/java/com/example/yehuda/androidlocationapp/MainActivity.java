package com.example.yehuda.androidlocationapp;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
    private Button GpsButton;
    private Button ShowOnMap;
    private Button SendData;
    private Button SendDataToServer;
    private Button buttonQrCodeScanner;
    private Button buttonBLECodeScanner;
    private TextView CoordinationView;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private double Latitude = 32.104954; //ariel Latitude position, for default location
    private double Longitude = 35.207730; //ariel Longitude position, for default location
    private double Altitude;
    private double speed;
    private double Accuracy;

    //private ZXingScannerView msScannerView;

    private static final int minTime = 5000; //min time in milliseconds to show new gps single
    private static final int minDistance = 0; //min distance (in meters), to show new gps single

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GpsButton = (Button) findViewById(R.id.button_show_gps_location);
        GpsButton.setBackgroundColor(Color.RED);

        buttonQrCodeScanner = (Button) findViewById(R.id.button_QrCodeScanner);
        buttonBLECodeScanner = (Button) findViewById(R.id.button_BLECodeScanner);

        CoordinationView = (TextView) findViewById(R.id.GpsCoordination);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                GpsButton.setBackgroundColor(Color.GREEN);

                Latitude = location.getLatitude();
                Longitude = location.getLongitude();
                Altitude = location.getAltitude();
                speed = location.getSpeed();
                Accuracy = location.getAccuracy();

                CoordinationView.setText("Location: " + Latitude + " / " + Longitude + "\n" +
                        "Altitude:" + Altitude + "\n" +
                        "Speed: " + speed + " \n" +
                        "Accuracy: " + Accuracy);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                CoordinationView.setText("on status changed");

            }

            @Override
            public void onProviderEnabled(String s) {
                CoordinationView.setText("on provider enabled");

            }

            @Override
            public void onProviderDisabled(String s) {
                //GO to gps SETTING:
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };

        configure_button();
        onClickButtonMapListener();
        onClickButtonSendDataListener();
        onClickButtonSendDataToServer();
        onClickbuttonQrCodeScanner();
        onClickbuttonBLECodeScanner();
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        GpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", minTime, minDistance, locationListener);
            }
        });
    }

    public void onClickButtonMapListener() {
        ShowOnMap = (Button) findViewById(R.id.button_move_to_map_activity);
        ShowOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.example.yehuda.androidlocationapp.MapsActivity");
                intent.putExtra("Latitude", Latitude);
                intent.putExtra("Longitude", Longitude);
                startActivity(intent);
            }
        });
    }

    public void onClickButtonSendDataListener() {
        SendData = (Button) findViewById(R.id.button_share_location);
        SendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"yehuda.livnoni@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Location is");
        emailIntent.putExtra(Intent.EXTRA_TEXT, Latitude + " / " + Longitude);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("finished sending", "done");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickButtonSendDataToServer()
    {
        SendDataToServer = (Button) findViewById(R.id.button_send_data_to_server);
        SendDataToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent("com.example.yehuda.androidlocationapp.SendDataToServerActivity");
                startActivity(intent);
            }
        });
    }
    public void onClickbuttonQrCodeScanner()
    {
        buttonQrCodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.i("QR:","button pressed.");
                Intent intent = new Intent("com.example.yehuda.androidlocationapp.QrReaderActivity");
                startActivity(intent);
            }
        });
    }
    public void onClickbuttonBLECodeScanner()
    {
        buttonBLECodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Log.i("QR:","button pressed.");
                Intent intent = new Intent("com.example.yehuda.androidlocationapp.BLEreaderActivity");
                startActivity(intent);
            }
        });
    }

}


