package com.example.restaurantorders;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    int i = 0;
    // to check if we are connected to Network
    boolean isConnected = true;
    String TAG = "Networking ME:";
    int g = 0;
    // to check if we are monitoring Network
    private boolean monitoringConnectivity = false;
    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;
            if (i == 0) {
                Thread me = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            Intent intent = new Intent(getApplicationContext(), Viewing.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                me.start();
                Toast.makeText(getApplicationContext(), "Connected",
                        Toast.LENGTH_SHORT).show();
            }
            i++;
            Log.d(TAG, "INTERNET CONNECTED");
        }

        @Override
        public void onLost(Network network) {
            Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
            isConnected = false;
            Log.d(TAG, "INTERNET LOST");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkConnectivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    @Override
    public void onPause() {
        // if network is being moniterd then we will unregister the network callback
        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            connectivityManager.unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;
        }
        super.onPause();
    }

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        // isConnected is a boolean variable
        // here we check if network is connected or is getting connected
        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();

        if (!isConnected) {
            // SHOW ANY ACTION YOU WANT TO SHOW
            // WHEN WE ARE NOT CONNECTED TO INTERNET/NETWORK
            Log.d(TAG, " NO NETWORK!");
            Toast.makeText(this, "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
// if Network is not connected we will register a network callback to  monitor network
            connectivityManager.registerNetworkCallback(
                    new NetworkRequest.Builder()
                            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                            .build(), connectivityCallback);
            monitoringConnectivity = true;
        } else {
            Log.d(TAG, " NOW NETWORK!");
            if (g == 0) {
                Thread me = new Thread() {
                    @Override
                    public void run() {
                        try {
                            sleep(2000);
                            Intent intent = new Intent(getApplicationContext(), Viewing.class);
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                me.start();
                Toast.makeText(getApplicationContext(), "Connected",
                        Toast.LENGTH_SHORT).show();
            }
            g++;
        }
    }

}
