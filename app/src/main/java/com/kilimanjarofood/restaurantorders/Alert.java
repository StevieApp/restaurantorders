package com.kilimanjarofood.restaurantorders;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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
        
    }
}
