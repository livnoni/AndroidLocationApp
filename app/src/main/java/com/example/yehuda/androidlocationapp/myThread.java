package com.example.yehuda.androidlocationapp;

import android.util.Log;

/**
 * Created by yehud on 19/12/2016.
 */

public class myThread extends Thread
{
    int i=0;
    public void run() {
        // TODO Auto-generated method stub
        while(!isInterrupted())
        {
            while(true)
            {
                try {
                    Thread.sleep(1000);
                    int i=0;
                    Log.d( "I is ",""+i);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }
    }
}
