package com.example.yehuda.androidlocationapp;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class QrReaderActivity extends AppCompatActivity
{

    private Button button;
    private Button buttonCopClipBoardy;
    private Button ButtonSendData;
    private TextView textView;
    private String dataScanned = "no input yet...";
    double Latitude=0,Longitude=0;
    double lastLatitude,lastLongitude;

  //  String insertUrl = getString(R.string.insertUrl);
    String insertUrl = "http://yehudalocation.000webhostapp.com/location/insertLocation.php";

    com.android.volley.RequestQueue requestQueue;
    String model;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_reader);
        button = (Button) this.findViewById(R.id.button);
        textView = (TextView) this.findViewById(R.id.textView2);
        textView.setMovementMethod(new ScrollingMovementMethod());
        buttonCopClipBoardy = (Button) this.findViewById(R.id.button_cope_to_clipboard);
        final Activity activity = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        buttonCopClipBoardy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Scanned data", dataScanned);
                clipboard.setPrimaryClip(clip);

            }
        });
        model = Build.MODEL;
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        ButtonSendData = (Button) this.findViewById(R.id.Button_send_data_from_QR);
        onClickButtonSendDataToServer();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result!=null)
        {
            if (result.getContents() == null)
            {
                Log.d("MainActivity", "Cancelled scan");
                Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.d("MainActivity", "scanned");
                Toast.makeText(this, "scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                textView.setText("");
                textView.setText(result.getContents());
                dataScanned = result.getContents();
            }
        }
        else
        {
            //This is importent! otherwise the result will not be passed to the fragment.
            super.onActivityResult(requestCode,resultCode,data);


        }
    }
    public void onClickButtonSendDataToServer()
    {
        ButtonSendData.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                try {
                    String[] separated = dataScanned.split(" ");
                    Latitude = Double.parseDouble(separated[0]);
                    Longitude = Double.parseDouble(separated[1]);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    Latitude=0;
                    Longitude=0;
                }


                Log.i("Latitude=",Latitude+"");
                Log.i("Longitude=",Longitude+"");

                if(Latitude!=0 && Longitude!=0)
                {
                    sendToServer();
                    Toast.makeText(getApplicationContext(),"Location Sent !", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    new AlertDialog.Builder(QrReaderActivity.this)
                            .setTitle("Wrong Scanning Barcode")
                            .setMessage("Sorry, but you didn't scanned properly the barcode, please scan again.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                            {
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }

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
                parameters.put("altitude", String.valueOf(0));
                parameters.put("model", String.valueOf(model));
                parameters.put("date", String.valueOf(DateFormat.getDateTimeInstance().format(new Date())));

                return parameters;
            }
        };
        Log.i("the model is", model);

        requestQueue.add(request);
    }

}
