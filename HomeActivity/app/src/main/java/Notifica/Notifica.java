package Notifica;

import android.app.AlarmManager;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.parkingapp.homeactivity.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

public class Notifica extends BroadcastReceiver {

    public static final String CHANNEL_ID ="ID notifica";
    public static String NOTIFICATION_ID = "notification-id";

  public   String titolo;
   public String messaggio;
     Context context;

    public Notifica()
    { }


//Notifica che appare per l'esecuzione di un processo in background
    public  void createNotificationChannel(Context context, int importance)
    {

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Parking Advisor",
                    importance
            );

          NotificationManager  notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    //Metodo statico per la generazione di notifiche
    public void creaNotifica(Context context, String messaggio, String titolo)
    {
        this.messaggio=messaggio;
        this.titolo=titolo;
        this.context=context;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context1, Intent intent) {

        createNotificationChannel(context1, NotificationManager.IMPORTANCE_DEFAULT);

        Notification.Builder builder = new Notification.Builder(context1, CHANNEL_ID)
                .setContentTitle(intent.getStringExtra("titolo"))
                .setContentText(intent.getStringExtra("messaggio"))
                .setSmallIcon(R.drawable.logo_app)
                .setStyle(new Notification.BigTextStyle())
                .setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManager notificationManager = (NotificationManager)context1.getSystemService(Context.NOTIFICATION_SERVICE);
      //  Random rand = new Random(); //Genero un id a caso per la notifica
        int id=intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, builder.build());
    }

  /*  public void process(String titolo, String messaggio, String ora_convertire,Context context)
    {

        createNotificationChannel(context, NotificationManager.IMPORTANCE_DEFAULT);

        Intent intent = new Intent(context, Notifica.class);
        intent.putExtra("titolo", titolo);
        intent.putExtra("messaggio", messaggio);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        long tempo_adesso = System.currentTimeMillis();

        // String string = "20-7-2020 21&00&00";
        DateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.ITALIAN);
        Date date = new Date();
        try {
            date = format.parse(ora_convertire);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("PROMEMORIA_NOTIFICA", Context.MODE_PRIVATE);
        long millisecondi_sottrarre=sharedPreferences.getLong("TEMPO MILLI", 3600000);

        Log.i("tempo millisecondi", String.valueOf(date.getTime()-millisecondi_sottrarre-tempo_adesso));

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                date.getTime()-millisecondi_sottrarre-tempo_adesso,
                pendingIntent);
    }

   */
}


