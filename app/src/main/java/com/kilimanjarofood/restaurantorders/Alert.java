package com.kilimanjarofood.restaurantorders;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class Alert extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        Toast.makeText(this, "boom", Toast.LENGTH_SHORT).show();
        Log.d("loobing", "loobing");
        Timer timer = new Timer();
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                stopService();
                startService();
            }
        }, 5000, 600000000);
        Toast.makeText(this, "booming", Toast.LENGTH_SHORT).show();
    }

    public void startService() {
        startService(new Intent(this, Alert.class));
    }

    public void stopService() {
        stopService(new Intent(this, Alert.class));
    }
}
