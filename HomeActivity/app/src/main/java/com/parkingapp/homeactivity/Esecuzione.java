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
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Posizione.Posizione;
import Server.VolleyCallback;

import Server.CreazioneJson;
import Server.Server;
import asyncTasks.AsyncTaskEsecuzione;
import mist.Variabili;

public class Esecuzione extends AppCompatActivity {

    Button bttAnnulla=null;
    Button bttSalvaParcheggio=null;
    TextView tvErrore=null;
    Context context=this;

    public static boolean scelta=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esecuzione);

       final Intent i= getIntent();

        bttAnnulla=findViewById(R.id.bttAnnullaEsecuzione);
        bttSalvaParcheggio= findViewById(R.id.bttSalvaParcheggio);
        tvErrore=findViewById(R.id.tvEsecuzione);


       final AsyncTaskEsecuzione asyncTaskEsecuzione= new AsyncTaskEsecuzione(this, bttAnnulla, bttSalvaParcheggio);
        asyncTaskEsecuzione.execute();


        bttAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Variabili.annullaOSalvaParcheggio(context, false);
                scelta=false;
                asyncTaskEsecuzione.cancel(true);
                Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                startActivity(i);
            }
        });



        bttSalvaParcheggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvErrore.setVisibility(View.INVISIBLE);//Inizializzo sempre il mio log di errore a invisible

              //  Variabili.annullaOSalvaParcheggio(context, true);
                scelta=true;
                 Posizione posizione=new Posizione(context);
                asyncTaskEsecuzione.cancel(true);

                //Recupero username e password per capire di chi è l'account
                SharedPreferences sharedPreferences=getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);
                String username=sharedPreferences.getString("USERNAME", "");
                String password=sharedPreferences.getString("PASSWORD", "");

                sharedPreferences=getSharedPreferences("COORDINATE", Context.MODE_PRIVATE);

                String[] coordinate_salvate_s = {sharedPreferences.getString("LATITUDINE", "1000000"), sharedPreferences.getString("LONGITUDINE", "1000000")};

                double[] coordinate_d={Double.valueOf(coordinate_salvate_s[0]), Double.valueOf(coordinate_salvate_s[1])};

                String coordinatesInString= "[ " + Double.valueOf(coordinate_salvate_s[0]) + ", " + Double.valueOf(coordinate_salvate_s[1]) + " ]";
              /*  String[] location={"type"};
                String[] fieldLocation= {"Point"};
                Map<String, String> locationField= CreazioneJson.createJson(location,fieldLocation);*/

              String location_string="{\"type\": \"Point\",\"coordinates\": "+coordinatesInString+"}";

                Map<String, Object> values= new HashMap<>();
                values.put("values", 1588147128);

                JSONObject valuesJson= new JSONObject(values);

                String[] samples=new String[]{valuesJson.toString()};


                String[] nomiJson={"thing", "feature", "device"};
                String[] campiJson = {username+"_"+password, "parking", "parking-app"};

                Map<String, String> bodyJson= CreazioneJson.createJson(nomiJson, campiJson);

                bodyJson.put("location", location_string);
                bodyJson.put("samples", Arrays.toString(samples));





               /* Map<String, String> post= new HashMap<>();
                post.put("thing",username+"_"+password);
                post.put("feature","parking");
                post.put("device","parking-app");
                post.put("location",locationField[0]);
                post.put("location",locationField[1]);
                */


                Server.makePost("/v1/measurements  ", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {

                        //Aggiungere parte in cui dalle coordinate prendiamo il nome e la città di provenienza, così da salvarlo in locale
                        //e farlo apparire in "Parcheggio"

                        Variabili.aggiornaPosizione(context);

                        Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                        startActivity(i);
                    }

                    @Override
                    public void onError(VolleyError error) throws Exception {

                        Log.e("BottoneSalva Coordinate", error.toString());
                        if(error instanceof ServerError || error instanceof AuthFailureError)
                        {
                            tvErrore.setText("Si è verificato un errore, riprova");
                            tvErrore.setVisibility(View.VISIBLE);
                        }

                        else if(error instanceof TimeoutError || error instanceof NoConnectionError)
                        {
                            tvErrore.setText("Connessione a internet assente");
                            tvErrore.setVisibility(View.VISIBLE);
                        }
                        else if(error instanceof NetworkError)
                        {
                            tvErrore.setText("Errore di connessione, riprova");
                            tvErrore.setVisibility(View.VISIBLE);
                        }

                        else if (error instanceof ParseError) {
                            Log.e("ParseError", "Errore server, ESECUZIONE");
                        }
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
