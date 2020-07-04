package Posizione;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.ActivityCompat;


import com.androidnetworking.error.ANError;
import com.google.android.gms.location.FusedLocationProviderClient;
import android.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parkingapp.homeactivity.Esecuzione;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import Server.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Server.Server;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class Posizione {

    //Indirizzo per il reverse geocoding (latlng=40.714224,-73.961452&key=)

    public double[] coordinate = new double[2];
    public Context context;
    public LocationManager locationManager=null;
    public String[] nomeCittà_via={null, null, null};


    //Generico modo per aggiornare le coordinate
   private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            coordinate[0] = location.getLatitude();
            coordinate[1] = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public Posizione(Context context) {
        this.context = context;
    }


    public void prendiPosizione() {
        requestPermission();//Richiedo permesso localizzazione all'utente

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        assert locationManager != null;
        if (ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        //Le coordinate effettive che vengono restituite sono le ultime note, così che se un aggiornamento fallisce abbiamo comunque valori attendibili
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        assert location != null;
        this.coordinate[0] = location.getLatitude();
        this.coordinate[1] = location.getLongitude();


    }


    //Il metodo è in grado di restituire una stringa in formato  "formatted_address": "Via Magenta, 42, 16043 Chiavari GE, Italy"
    public void nomeViaECittà() {
       // final String[] città_via = {null, null, null};
        prendiPosizione();

        Server.reverseGeocoding( this.context, this.coordinate, new Callback() {

            @Override
            public void onSuccess(JSONObject response) throws JSONException {

                //Prendo nome intero
                JSONArray results = response.getJSONArray("results");
                JSONObject formatted_address = results.getJSONObject(1);
                nomeCittà_via[0] = formatted_address.getString("formatted_address");
                Log.i("NOME CITTA_VIA", nomeCittà_via[0]);

                //Prendo nome Città
                JSONObject oggetto_riposta = results.getJSONObject(0);
                JSONArray address_components=oggetto_riposta.getJSONArray("address_components");
                JSONObject città = address_components.getJSONObject(2);
                nomeCittà_via[1] = città.getString("long_name");
                Log.i("NOME CITTA", nomeCittà_via[1]);

                //Prendo nome Via
                JSONObject via = address_components.getJSONObject(1);
                nomeCittà_via[2] = via.getString("long_name");
                Log.i("NOME VIA", nomeCittà_via[2]);

            }

            @Override
            public void onError(ANError errore) throws Exception {

                Log.e("Città chiamata API", errore.toString());
            }
        });

      //  return città_via;
    }


    public boolean èFermo() {
        double[] confronto = this.coordinate;
        prendiPosizione();
        return confronto[0] == this.coordinate[0] && confronto[1] == this.coordinate[1];
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions((Activity) context, new String[]{ACCESS_FINE_LOCATION}, 1);
    }


    public void aggiornaGPS(long tempo, float distanza) { //Mi faccio dare tempo e distanza così da poter modificare dinamicamente
        // la precisione all'interno del codice

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, tempo, distanza, locationListener);
    }


    public void fermaAggiornamentoGPS()
    {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        locationManager.removeUpdates(locationListener);
    }


}
