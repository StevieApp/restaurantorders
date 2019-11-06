package com.kilimanjarofood.restaurantorders;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Fireb extends FirebaseMessagingService {

    Bitmap bee;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        notifier(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    public void notifier(String title, String message) {

        bee = getBitmapfromUrl
                ("https://scontent-mba1-1.xx.fbcdn.net/v/t1.0-9/48084527_930364097157540_8317509901655998464_n.jpg?_nc_cat=103&_nc_eui2=AeFNIjGgWW3meweMpgATua-seY6MES--GjuwvuiVHguEIKBfj14h2joMxfDaKLNXlCU9GvNlSf49YHeUYwFrDT8j80TNAUJPANZHyeqNqmAWWA&_nc_oc=AQlx6ECG7eif364m-D7By2GlavD22DixrRwmFyWJaWGPxipehjDamVZ_H6Z_R78FPN0&_nc_ht=scontent-mba1-1.xx&oh=09944f0fe98c8e51843d7677385cd6c0&oe=5E5CFBD8");

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
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(bee).bigLargeIcon(bee).setBigContentTitle("Kilimanjaro Food")
                        .setSummaryText("New Order Received!"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                //.setLargeIcon(bee)
                .setContentText(message)
                .setLights(Color.MAGENTA, 6, 6)
                .setContentIntent(pendingIntent);
        Notification note = builder.build();
        note.defaults |= Notification.DEFAULT_VIBRATE;
        note.defaults |= Notification.DEFAULT_SOUND;
        NotificationManagerCompat how = NotificationManagerCompat.from(this);
        how.notify(767, note);
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

}
