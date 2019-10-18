package com.example.restaurantorders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (CheckNet()) {
            Thread me = new Thread() {
                @Override
                public void run() {
                    try {
                        sleep(2000);
                        Intent intent = new Intent(getApplicationContext(), Viewing.class);
                        startActivity(intent);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
            me.start();
        } else {
            Toast.makeText(this, "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public boolean CheckNet() {
        boolean connected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
            //OrderManipulation.getAllOrders(this);
            OrderManipulation.getSingleOrder(this, 70);
        } else {
            connected = false;
        }
        return connected;
    }
}
