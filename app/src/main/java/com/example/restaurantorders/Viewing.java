package com.example.restaurantorders;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Viewing extends AppCompatActivity {

    Fragment selectedFragment = null;
    // to check if we are connected to Network
    boolean isConnected = true;
    String TAG = "Networking ME:";
    private ConnectivityManager.NetworkCallback connectivityCallback
            = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            isConnected = true;

            BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigator);
            bottom_nav.getSelectedItemId();
            Fragment frgg;
            final FragmentTransaction ftt;
            if (bottom_nav.getSelectedItemId() == R.id.allorders) {
                ftt = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_area, new Orders());
                ftt.commit();
                Log.d(TAG, "INTERNET CONNECTEDedededdd");
            } else if (bottom_nav.getSelectedItemId() == R.id.dispatchedorders) {
                ftt = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_area, new DispatchedOrders());
                ftt.commit();
                Log.d(TAG, "INTERNET CONNECTEDedededdd");
            } else if (bottom_nav.getSelectedItemId() == R.id.pendingorders) {
                ftt = getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_area, new PendingOrders());
                ftt.commit();
                Log.d(TAG, "INTERNET CONNECTEDedededdd");
            }
            Log.d(TAG, "INTERNET CONNECTED");
        }

        @Override
        public void onLost(Network network) {
            isConnected = false;
            Toast.makeText(getApplicationContext(), "No Internet Connection",
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "INTERNET LOST");
        }
    };
    // to check if we are monitoring Network
    private boolean monitoringConnectivity = false;
    private BottomNavigationView.OnNavigationItemSelectedListener newListener = new
            BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.allorders:
                            selectedFragment = new Orders();
                            break;
                        case R.id.dispatchedorders:
                            selectedFragment = new DispatchedOrders();
                            break;
                        case R.id.pendingorders:
                            selectedFragment = new PendingOrders();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                            selectedFragment).commit();
                    checkConnectivity();
                    return true;
                }
            };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing);
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigator);
        bottom_nav.setOnNavigationItemSelectedListener(newListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                new Orders()).commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
        
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_navigator);
        bottom_nav.setOnNavigationItemSelectedListener(newListener);
        if (bottom_nav.getSelectedItemId() == R.id.allorders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                    new Orders()).commit();
        } else if (bottom_nav.getSelectedItemId() == R.id.dispatchedorders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                    new DispatchedOrders()).commit();
        } else if (bottom_nav.getSelectedItemId() == R.id.pendingorders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_area,
                    new PendingOrders()).commit();
        }
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

        if (getSupportFragmentManager().findFragmentById(R.id.fragment_area) != null) {
            getSupportFragmentManager().findFragmentById(R.id.fragment_area)
                    .setRetainInstance(true);
        }
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
        }
    }
}
