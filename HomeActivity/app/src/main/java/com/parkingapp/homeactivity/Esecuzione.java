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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

        Variabili.salvaDestinazione(context, "Chiavari");
        final Posizione posizione = new Posizione(context);
        final AsyncTaskEsecuzione asyncTaskEsecuzione = new AsyncTaskEsecuzione(context, bttAnnulla, bttSalvaParcheggio, posizione);
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
                double[] coordinate = posizione.coordinate;
                Variabili.salvaCoordinate(context, coordinate);
                asyncTaskEsecuzione.cancel(true);


                String[] coordinateInStringhe = new String[2];
                coordinateInStringhe[0] = String.format("%f", posizione.coordinate[0]);
                coordinateInStringhe[1] = String.format("%f", posizione.coordinate[1]);
                Log.i("esecuzioneSalva", posizione.coordinate[0] + "spazio" + posizione.coordinate[1]);


                //Recupero username e password per capire di chi è l'account
                SharedPreferences sharedPreferences = getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("USERNAME", "");
                String password = sharedPreferences.getString("PASSWORD", "");

                //sharedPreferences=getSharedPreferences("COORDINATE", Context.MODE_PRIVATE);

                // String[] coordinate_salvate_s = {sharedPreferences.getString("LATITUDINE", "1000000"), sharedPreferences.getString("LONGITUDINE", "1000000")};

                //  double[] coordinate_d={Double.valueOf(coordinate_salvate_s[0]), Double.valueOf(coordinate_salvate_s[1])};

                // String coordinatesInString= "[ " + Double.valueOf(coordinate[0]) + ", " + Double.valueOf(coordinate[1]) + " ]";
              /*  String[] location={"type"};
                String[] fieldLocation= {"Point"};
                Map<String, String> locationField= CreazioneJson.createJson(location,fieldLocation);*/

                // String location_string="{\"type\": \"Point\",\"coordinates\": "+coordinatesInString+"}";


                String[] nomi_jsonObject = {"thing", "feature", "device", "location", "samples"};
                String[] nomi_location = {"type", "coordinates"};
                String[] nomi_samples = {"values"};

                JSONObject location = new JSONObject();
                JSONObject samples = new JSONObject();
                JSONArray coordinatesArray = new JSONArray();
                JSONArray samplesArray = new JSONArray();
                JSONObject jsonPost = new JSONObject();

                Map<String, String> postJson = new HashMap<>();

                try {
                    coordinatesArray.put(coordinate[0]);
                    coordinatesArray.put(coordinate[1]);
                    location = CreazioneJson.createJSONObject(nomi_location, "Point", coordinatesArray);
                    samples = CreazioneJson.createJSONObject(nomi_samples, 1588147128);
                    samplesArray.put(samples);
                    //String locationString = location.toString();
                    //String samplesArrayString= samplesArray.toString();


                    //provo a creare qui il JSONObject da passare alla classe httpconnectionnovolley che poi aggiungerà il token nelle
                    //autorizzazioni e infine aggiungerà al corpo questo JSON
                    JSONObject locationObj = new JSONObject();
                    locationObj.put("type", "Point");
                    locationObj.put("coordinates", coordinatesArray);
                   /* postMes = new JSONObject();
                    postMes.put("thing", username+"_"+password);
                    postMes.put("feature", "parking");
                    postMes.put("device", "parking-app");
                    postMes.put("location", locationObj);
                    postMes.put("samples", samplesArray);

 */

                    postJson.put("thing", username + "_" + password);
                    postJson.put("feature", "parking");
                    postJson.put("device", "parking-app");
                    postJson.put("location", locationObj.toString());
                    postJson.put("samples", samplesArray.toString());


                    //  jsonPost = CreazioneJson.createJSONObject(nomi_jsonObject, username+"_"+password, "parking", "parking-app", location, samplesArray);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                //  jsonPost.put("samples", values.toArray());


                // JSONObject valuesJson= new JSONObject(values);

                //  String[] samples=new String[]{valuesJson.toString()};


               /* String[] nomiJson={"thing", "feature", "device"};
                String[] campiJson = {username+"_"+password, "parking", "parking-app"};

                Map<String, String> bodyJson= CreazioneJson.createJson(nomiJson, campiJson);

                bodyJson.put("location", location_string);
                bodyJson.put("samples", Arrays.toString(samples));

                */

                Toast.makeText(context, coordinate[0] + ", " + coordinate[1], Toast.LENGTH_LONG).show();


               /* Map<String, String> post= new HashMap<>();
                post.put("thing",username+"_"+password);
                post.put("feature","parking");
                post.put("device","parking-app");
                post.put("location",locationField[0]);
                post.put("location",locationField[1]);
                */
             /* HttpConnectionNoVolley  hcnv = new HttpConnectionNoVolley();
                try {
                    hcnv.makePostMesur(context, postMes );
                    Log.i ("provatofareconnession", "no volley");
                } catch (IOException e) {
                    e.printStackTrace();
                }


                */

                Server.makePost("/v1/measurements", new VolleyCallback() {
                    @Override
                    public void onSuccess(JSONObject result) throws JSONException {

                        //Aggiungere parte in cui dalle coordinate prendiamo il nome e la città di provenienza, così da salvarlo in locale
                        //e farlo apparire in "Parcheggio"


                        Variabili.aggiornaPosizione(context);

                        posizione.fermaAggiornamentoGPS();
                        Variabili.salvaParcheggio(context, posizione.nomeViaECittà()[0]);
                        Intent i = new Intent(getString(R.string.MAIN_TO_HOME));
                        startActivity(i);

                    }

                    ;

                    @Override
                    public void onError(VolleyError error) throws Exception {

                        Log.e("BottoneSalva Coordinate", error.toString());
                        if (error instanceof ServerError || error instanceof AuthFailureError) {
                            tvErrore.setText("Si è verificato un errore, riprova");
                            tvErrore.setVisibility(View.VISIBLE);
                        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            tvErrore.setText("Connessione a internet assente");
                            tvErrore.setVisibility(View.VISIBLE);
                        } else if (error instanceof NetworkError) {
                            tvErrore.setText("Errore di connessione, riprova");
                            tvErrore.setVisibility(View.VISIBLE);
                        } else if (error instanceof ParseError) {
                            Log.e("ParseError", "Errore server, ESECUZIONE");
                        }
                    }
                }, context, postJson);


            }
        });



    }
}
