package com.parkingapp.homeactivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import Server.VolleyCallback;

import Server.CreazioneJson;
import Server.Server;
import asyncTasks.AsyncTaskEsecuzione;
import mist.Variabili;

public class Esecuzione extends AppCompatActivity {

    Button bttAnnulla=null;
    Button bttSalvaParcheggio=null;
    Context context=this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esecuzione);

       final Intent i= getIntent();

        bttAnnulla=findViewById(R.id.bttAnnullaEsecuzione);
        bttSalvaParcheggio= findViewById(R.id.bttSalvaParcheggio);



       final AsyncTaskEsecuzione asyncTaskEsecuzione= new AsyncTaskEsecuzione(this, bttAnnulla, bttSalvaParcheggio);
        asyncTaskEsecuzione.execute();


        bttAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Variabili.annullaOSalvaParcheggio(context, false);
                asyncTaskEsecuzione.cancel(true);
                Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                startActivity(i);
            }
        });



        bttSalvaParcheggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Variabili.annullaOSalvaParcheggio(context, true);
                asyncTaskEsecuzione.cancel(true);

                SharedPreferences sharedPreferences=getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);
                String username=sharedPreferences.getString("USERNAME", "");

                sharedPreferences=getSharedPreferences("COORDINATE", Context.MODE_PRIVATE);
                String latitudine = sharedPreferences.getString("LATITUDINE", "");
                String longitudine = sharedPreferences.getString("LONGITUDINE", "");

                String coordinatesInString= "[ " + latitudine + "," + longitudine + " ]";
                String[] location={"type", "coordinates"};
                String[] fieldLocation= {"Point", coordinatesInString};
                Map<String, String> locationField= CreazioneJson.createJson(location,fieldLocation);

                String[] nomiJson={"thing", "feature", "device", "location", "samples"};
                String[] campiJson = {username, "parking", "parking-app", String.valueOf(locationField), " [ { \"values\": 1588147128 } ] "};
               Map<String, String> bodyJson= CreazioneJson.createJson(nomiJson, campiJson);

                Server.makePost("/v1/measurements  ", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {

                        Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                        startActivity(i);
                    }

                    @Override
                    public void onError(String errore) throws Exception {

                        Log.e("BottoneSalva Coordinate", errore);
                    }
                }, context, bodyJson);


            }
        });

     /*   private void startForeground() {
            Intent notificationIntent = new Intent(this, Esecuzione.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);

            startForeground(NOTIF_ID, new NotificationCompat.Builder(this,
                    NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                    .setOngoing(true)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Service is running background")
                    .setContentIntent(pendingIntent)
                    .build());
        }*/


    }
}
