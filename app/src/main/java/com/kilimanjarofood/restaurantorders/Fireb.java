package com.kilimanjarofood.restaurantorders;

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
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notif")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.chicken)
                .setAutoCancel(true)
                .setContentText(message);
        NotificationManagerCompat how = NotificationManagerCompat.from(this);
        how.notify(767, builder.build());
    }
}
