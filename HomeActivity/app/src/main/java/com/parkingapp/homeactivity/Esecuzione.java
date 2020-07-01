package com.parkingapp.homeactivity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import com.androidnetworking.error.ANError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import Accelerometro.Accelerometro;
import Posizione.Posizione;
import Server.VolleyCallback;

import Server.CreazioneJson;
import Server.Server;
import asyncTasks.AsyncTaskEsecuzione;
import mist.Variabili;
import Server.Callback;
import Server.HttpConnectionNoVolley;

public class Esecuzione extends AppCompatActivity {

    Button bttAnnulla=null;
    Button bttSalvaParcheggio=null;
    TextView tvErrore=null;
    Context context=this;
    JSONObject postMes= null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.esecuzione);

        final Intent i = getIntent();

        bttAnnulla = findViewById(R.id.bttAnnullaEsecuzione);
        bttSalvaParcheggio = findViewById(R.id.bttSalvaParcheggio);
        tvErrore = findViewById(R.id.tvEsecuzione);

      //  this.context=getApplicationContext();
        Variabili.salvaDestinazione(this.context, "Chiavari");
        final Posizione posizione = new Posizione(this.context);
        final AsyncTaskEsecuzione asyncTaskEsecuzione = new AsyncTaskEsecuzione(this.context, bttAnnulla, bttSalvaParcheggio, posizione);
         asyncTaskEsecuzione.execute();



        bttAnnulla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Variabili.annullaOSalvaParcheggio(context, false);
                asyncTaskEsecuzione.cancel(true);
                //Smetto di aggiornare costantemente la mia posizione
                posizione.fermaAggiornamentoGPS();

                Intent i = new Intent(getString(R.string.MAIN_TO_HOME));
                startActivity(i);
            }
        });


        bttSalvaParcheggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tvErrore.setVisibility(View.INVISIBLE);//Inizializzo sempre il mio log di errore a invisible

                //Salvo i dati prima di cancellare l'async task
                posizione.prendiPosizione();
                final double[] coordinate = posizione.coordinate;
                asyncTaskEsecuzione.cancel(true);


                String[] coordinateInStringhe = new String[2];
                coordinateInStringhe[0] = String.format("%f", posizione.coordinate[0]);
                coordinateInStringhe[1] = String.format("%f", posizione.coordinate[1]);
                Log.i("esecuzioneSalva", posizione.coordinate[0] + "spazio" + posizione.coordinate[1]);


                //Recupero username e password per capire di chi è l'account
                SharedPreferences sharedPreferences = getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("USERNAME", "");
                String password = sharedPreferences.getString("PASSWORD", "");

                String[] nomiJson={"thing", "feature", "device", "location", "samples"};
                JSONObject location=new JSONObject();

                try {
                    location.put("type", "Point");
                    location.put("coordinates", coordinate[0]);
                    location.accumulate("coordinates", coordinate[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONArray samples= new JSONArray();
                try {
                    samples.put(CreazioneJson.createJSONObject(new String[]{"values"}, 1588147128));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JSONObject postJson=new JSONObject();
                try {
                    postJson=CreazioneJson.createJSONObject(nomiJson, username+"_"+password, "parking", "parking-app", location, samples);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(context, coordinate[0]+","+coordinate[1], Toast.LENGTH_LONG).show();

                try {
                    Server.Post("/v1/measurements ", new Callback() {
                        @Override
                        public void onSuccess(JSONObject result) throws JSONException, IOException {

                            posizione.fermaAggiornamentoGPS();
                            posizione.nomeViaECittà();
                            String[] nomeCittà_via=posizione.nomeCittà_via;
                            if(nomeCittà_via[0]!=null) {
                                Variabili.salvaParcheggio(context, nomeCittà_via[0]);
                                Variabili.salvaCoordinate(context, coordinate);

                                Intent i = new Intent(getString(R.string.FRAGMENT_PARCHEGGIO_TO_MOSTRA_SULLA_MAPPA));
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onError(ANError error) throws Exception {

                            if(error.getErrorCode()==400 || error.getErrorCode()==403)
                            {
                                tvErrore.setText("Si è verificato un errore, riprova");
                                Log.e("ESECUZIONE.errore", "Richiesta mal formulata");
                            }
                            else if(error.getErrorCode()==500)
                            {
                                tvErrore.setText("Salvataggio non riuscito, verifica la connessione");
                                Log.e("ESECUZIONE.errore", "Erroe di Server/connessione");
                            }
                        }
                    }, context, postJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }
}


