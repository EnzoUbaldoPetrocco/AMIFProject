package Posizione;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parkingapp.homeactivity.Esecuzione;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Server.Server;
import Server.VolleyCallback;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Posizione {

    //Indirizzo per il reverse geocoding (latlng=40.714224,-73.961452&key=)

    public double[] coordinate = new double[2];

    public Context context;

    public Posizione(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public boolean prendiPosizione() {
        requestPermission();//Richiedo permesso localizzazione all'utente
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        if(ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return true;
        }


            client.getLastLocation().addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null) {
                        Posizione.this.coordinate[0] = location.getLatitude();
                        Posizione.this.coordinate[1] = location.getLongitude();
                    }
                }
            });

        return  true;
    }


  /*  public String nomeVia()
    {
        final String[] via={null};
        prendiPosizione();//Aggiorno le coordinate

        Server.callReverseGeocoding(context, this.coordinate, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                JSONArray results = response.getJSONArray("results");
                JSONArray address_components = results.getJSONArray(0);
                JSONObject route = address_components.getJSONObject(0);
                via[0] = route.getString("long_name");
            }

            @Override
            public void onError(VolleyError error) throws Exception {

            }
        });


        return via[0];

    } */

  //Il metodo è in grado di restituire una stringa in formato  "formatted_address": "Via Magenta, 42, 16043 Chiavari GE, Italy"
    public String[] nomeViaECittà()
    {
        final String[] città_via = {null, null, null};
        prendiPosizione();

        Server.callReverseGeocoding(context, this.coordinate, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                //Prendo nome intero
                JSONArray results = response.getJSONArray("results");
                JSONObject formatted_address = results.getJSONObject(1);
                //JSONObject route = address_components.getJSONObject(5);
                città_via[0] = formatted_address.getString("formatted_address");
                Log.i("NOME CITTA_VIA", città_via[0]);

                //Prendo nome Città
                results = response.getJSONArray("results");
                JSONArray address_components = results.getJSONArray(0);
                JSONObject città = address_components.getJSONObject(2);
                città_via[1]=città.getString("long_name");
                Log.i("NOME CITTA", città_via[1]);

                //Prendo nome Via
                results = response.getJSONArray("results");
                address_components = results.getJSONArray(0);
                JSONObject via = address_components.getJSONObject(1);
                città_via[2]=città.getString("long_name");
                Log.i("NOME VIA", città_via[2]);

            }

            @Override
            public void onError(VolleyError errore) throws Exception {

                Log.e("Città chiamata API", errore.toString());
            }
        });

        return città_via;
    }

    public boolean èFermo()
    {
        double[] confronto=this.coordinate;
        prendiPosizione();
        return confronto[0] == this.coordinate[0] && confronto[1] == this.coordinate[1];
    }

    private  void requestPermission()
    {
        ActivityCompat.requestPermissions((Activity)context, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
