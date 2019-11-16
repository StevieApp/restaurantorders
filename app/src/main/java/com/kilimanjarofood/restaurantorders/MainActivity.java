package com.kilimanjarofood.restaurantorders;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    String CHANNEL_ID = "Channel_Id";
    int i = 0;
    Bitmap bee;
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
    ArrayList<Order> getAllOrders = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    HashSet<String> nou = new HashSet();
    int NOTIF_ID = 1;
    double pInfiniteDouble = Double.POSITIVE_INFINITY;
    long duration = (long) pInfiniteDouble;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkConnectivity();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = getSharedPreferences("my prefa", Context.MODE_PRIVATE);
        editor = pref.edit();
        createNotificationChannel();
        startForeground(duration);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notif", "notif", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(manager).createNotificationChannel(channel);
        }
    }

    private void startForeground(long dur) {
        final Timer timer = new Timer();
        getAllOrders.clear();
        //Set the schedule function
        try {
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    startForeground(duration);

                    String url = "https://kilimanjarofood.co.ke/api/v1/dispatch/orders";
                    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

                    JsonObjectRequest objectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            url,
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject jsonObject) {
                                    Log.e("response from api", jsonObject.toString());
                                    System.out.println(jsonObject.toString());
                                    Log.d("response from api", "onResponse: \n"
                                            + jsonObject.toString());
                                    try {
                                        JSONObject j = jsonObject.getJSONObject("data");
                                        Log.d("response from api", j.toString());
                                        JSONArray rr = j.getJSONArray("orders");
                                        Log.d("response from api", rr.toString());
                                        Gson json = new Gson();
                                        Type types = new TypeToken<ArrayList<Order>>() {
                                        }.getType();
                                        ArrayList<Order> orders = json.fromJson(rr.toString(), types);
                                        getAllOrders.clear();
                                        getAllOrders.addAll(orders);

                                        for (int g = 0; g < getAllOrders.size(); g++) {
                                            nou.add(String.valueOf(getAllOrders.get(g).getId()));
                                        }

                                        if (pref.getString("size", null) == null) {
                                            editor.putString("size", String.valueOf(0));
                                            editor.putString("newsize", String.valueOf(0));
                                            editor.commit();
                                        }

                                        if (Integer.parseInt(pref.getString("size", null)) > 0
                                                && nou.size() != 0) {
                                            editor.putString("newsize", String.valueOf(nou.size()));
                                            editor.commit();
                                            if (Integer.parseInt(pref.getString("size", null))
                                                    < nou.size()) {
                                                Intent intent = new Intent(MainActivity.this,
                                                        Viewing.class);

                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                PendingIntent pendingIntent = PendingIntent
                                                        .getActivity(MainActivity.this,
                                                                0, intent, 0);

                                                NotificationCompat.Builder builder = new NotificationCompat
                                                        .Builder(MainActivity.this, CHANNEL_ID)
                                                        .setSmallIcon(R.drawable.cu)
                                                        .setColor(getResources().getColor(R.color.hound))
                                                        .setContentTitle("New Order(s): " + (nou.size()
                                                                - Integer.parseInt(pref
                                                                .getString("size", null))))
                                                        .setStyle(new NotificationCompat.BigPictureStyle()
                                                                .bigPicture(bee).bigLargeIcon(bee).setBigContentTitle("Kilimanjaro Food")
                                                                .setSummaryText("New Order Received!"))
                                                        .setContentText("Needs attention!!!")
                                                        .setStyle(new NotificationCompat.BigTextStyle()
                                                                .bigText("Needs attention!!!"))
                                                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                                                        .setContentIntent(pendingIntent)
                                                        .setLargeIcon(bee)
                                                        .setAutoCancel(true);

                                                Notification note = builder.build();
                                                note.defaults |= Notification.DEFAULT_VIBRATE;
                                                note.defaults |= Notification.DEFAULT_SOUND;

                                                NotificationManagerCompat notificationManager =
                                                        NotificationManagerCompat.from(MainActivity.this);
                                                // notificationId is a unique int for each notification that you must define
                                                notificationManager.notify(NOTIF_ID, note);
                                            }
                                        } else if (nou.size() != 0) {
                                            editor.putString("size", String.valueOf(nou.size()));
                                            editor.commit();
                                        } else {
                                            Calendar cal = Calendar.getInstance();
                                            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                                            //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pendingIntent);
                                        }

                                    } catch (JSONException e) {
                                        Log.d("response from api", "paaaapiiii");
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.e("error from api", volleyError.toString());
                                    System.out.println(volleyError.toString());
                                    Log.d("error from api", "onErrorResponse: \n"
                                            + volleyError.toString());
                                }
                            }
                    );
                    requestQueue.add(objectRequest);
                }
            }, 30000, (long) pInfiniteDouble);
        } catch (Exception e) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.delivery_charge);
            String description = getString(R.string.deliverycharge);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            Objects.requireNonNull(notificationManager).createNotificationChannel(channel);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkConnectivity();
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    @Override
    public void onPause() {
        // if network is being moniterd then we will unregister the network callback
        if (monitoringConnectivity) {
            final ConnectivityManager connectivityManager
                    = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            Objects.requireNonNull(connectivityManager).unregisterNetworkCallback(connectivityCallback);
            monitoringConnectivity = false;
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Method to check network connectivity in Main Activity
    private void checkConnectivity() {
        // here we are getting the connectivity service from connectivity manager
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        // Getting network Info
        // give Network Access Permission in Manifest
        final NetworkInfo activeNetworkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();

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
