package com.kilimanjarofood.restaurantorders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class Fireb extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        notifier(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void notifier(String title, String message) {
        Intent intent = new Intent(this,
                Viewing.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent
                .getActivity(this,
                        0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                "notif")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.chicken)
                .setAutoCancel(true)
                .setContentText(message)
                .setLights(Color.MAGENTA, 6, 6)
                .setContentIntent(pendingIntent);
        Notification note = builder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManagerCompat how = NotificationManagerCompat.from(this);
        how.notify(767, note);
    }
}
