package com.kilimanjarofood.restaurantorders;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
                getAllOrders.addAll(OrderManipulation.getAllOrders(Alert.this));
                if (!(getAllOrders.size() == 0)) {
                    for (int g = 0; g < getAllOrders.size(); g++) {
                        nou.add(String.valueOf(getAllOrders.get(g).getId()));
                    }
                }
            }
        }, 5000, 999999999);
        if (!(nou.size() == 0)) {
            editor.putString("size", String.valueOf(nou.size()));
            editor.commit();

        } else if (Integer.parseInt(pref.getString("size", null)) > 0
                && !(nou.size() == 0)) {
            editor.putString("newsize", String.valueOf(nou.size()));
            if (Integer.parseInt(pref.getString("size", null)) < nou.size()) {
                Intent intent = new Intent(this, Viewing.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(Alert.this, 0, intent, 0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(Alert.this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.chicken)
                        .setColor(getResources().getColor(R.color.hound))
                        .setContentTitle("New Order(s): " + (nou.size()
                                - Integer.parseInt(pref.getString("size", null))))
                        .setContentText("Needs attending!!!")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText("Needs attending!!!"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                                + "://" + this.getBaseContext().getPackageName() + "/" + R.raw.alarm))
                        .setVibrate(new long[]{5000, 5000})
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(NOTIF_ID, builder.build());
            }

        } else {
            Calendar cal = Calendar.getInstance();
            AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            //alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pendingIntent);
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
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void startService() {
        startService(new Intent(this, Alert.class));
    }

    public void stopService() {
        stopService(new Intent(this, Alert.class));
    }
}
