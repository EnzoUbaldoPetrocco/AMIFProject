package Notifica;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.parkingapp.homeactivity.R;

public class Notifica extends Application {

    public static final String CHANNEL_ID ="ServiceEsecuzione";


//Notifica che appare per l'esecuzione di un processo in background
    public static void createNotificationChannel(Context context)
    {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Park Tracking",
                    NotificationManager.IMPORTANCE_LOW
            );

            NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //Metodo statico per la generazione di notifiche
    public static void creaNotifica(Context context, String messaggio, String titolo)
    {
        Intent notificationIntent = new Intent(context, context.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(titolo)
                .setContentText(messaggio)
                .setSmallIcon(R.drawable.logo_app)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}


