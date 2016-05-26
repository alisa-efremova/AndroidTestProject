package com.alice.a7blankproject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;

public class MyService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();


    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //TextView txtResult = (TextView) findViewById(R.id.testText);
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
        //return super.onStartCommand(intent, flags, startId);
        handler = new Handler();
        Thread myThread = new Thread(
                runnable = new Runnable() {
            public void run() {
                Toast.makeText(context, "Service is still running", Toast.LENGTH_LONG).show();
                handler.postDelayed(runnable, 10000);

            }
        }
        );

        //handler.postDelayed(runnable, 2000);

        return START_NOT_STICKY;
    }

    public void getContext()
    {

    }
}