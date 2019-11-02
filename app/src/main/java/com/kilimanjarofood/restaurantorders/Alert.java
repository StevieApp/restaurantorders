package com.kilimanjarofood.restaurantorders;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class Alert extends Service {

    ArrayList<Order> getAllOrders = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    HashSet<String> nou = new HashSet();
    String CHANNEL_ID = "Channel_Id";
    int NOTIF_ID = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        pref = getSharedPreferences("my prefa", Context.MODE_PRIVATE);
        editor = pref.edit();

        startForeground();

        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {

        final Timer timer = new Timer();
        getAllOrders.clear();
        //Set the schedule function
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //stopService();
                startService();
                String url = "https://demo.kilimanjarofood.co.ke/api/v1/dispatch/orders";
                RequestQueue requestQueue = Volley.newRequestQueue(Alert.this);

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
                                        editor.commit();
                                    }
                                    if (Integer.parseInt(pref.getString("size", null)) > 0
                                            && nou.size() != 0) {
                                        editor.putString("newsize", String.valueOf(nou.size()));
                                        editor.commit();
                                        if (Integer.parseInt(pref.getString("size", null))
                                                < nou.size()) {
                                            Intent intent = new Intent(Alert.this,
                                                    Viewing.class);

                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            PendingIntent pendingIntent = PendingIntent
                                                    .getActivity(Alert.this,
                                                            0, intent, 0);

                                            NotificationCompat.Builder builder = new NotificationCompat
                                                    .Builder(Alert.this, CHANNEL_ID)
                                                    .setSmallIcon(R.drawable.chicken)
                                                    .setColor(getResources().getColor(R.color.hound))
                                                    .setContentTitle("New Order(s): " + (nou.size()
                                                            - Integer.parseInt(pref
                                                            .getString("size", null))))

                                                    .setContentText("Needs attention!!!")
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText("Needs attention!!!"))
                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                    .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                                            + "://" + Alert.this.getBaseContext()
                                                            .getPackageName() + "/" + R.raw.alarm))
                                                    .setVibrate(new long[]{5000, 5000})
                                                    .setContentIntent(pendingIntent)
                                                    .setAutoCancel(true);

                                            NotificationManagerCompat notificationManager =
                                                    NotificationManagerCompat.from(Alert.this);
                                            // notificationId is a unique int for each notification that you must define
                                            notificationManager.notify(NOTIF_ID, builder.build());
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
        }, 5000, 999999999);
    }

    //NotificationChannel creation
    /*private void createNotificationChannel() {
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
            notificationManager.createNotificationChannel(channel);
        }
    }*/

    public void startService() {
        startService(new Intent(this, Alert.class));
    }

    public void stopService() {
        stopService(new Intent(this, Alert.class));
    }
}
