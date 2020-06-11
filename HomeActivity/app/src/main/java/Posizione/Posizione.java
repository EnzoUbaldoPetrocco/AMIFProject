package Posizione;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

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

import Server.VolleyCallback;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Posizione {

    //Indirizzo per il reverse geocoding (latlng=40.714224,-73.961452&key=)
    private String indirizzo_p1 = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private String indirizzo_p2 = "&key=AIzaSyA09MsWgTgGZvMh8KDjyNtxm8ovRYoq1Dg";

    private URL url;
    public double[] coordinate = new double[2];

    public Context context;

    public Posizione(Context context) {
        this.context = context;
    }

    @SuppressLint("MissingPermission")
    public void prendiPosizione() {
        requestPermission();//Richiedo permesso localizzazione all'utente
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);

        if(ActivityCompat.checkSelfPermission(context, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
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
    }


    public String nomeVia()
    {
        String via=null;
        prendiPosizione();//Aggiorno le coordinate
        try{
            String coordinate_s=coordinate[0]+","+coordinate[1];
            String url_s=indirizzo_p1+coordinate_s+indirizzo_p2;
            url= new URL(url_s);

            HttpURLConnection connection= (HttpURLConnection) url.openConnection(); //Cast a url
            JSONObject response = (JSONObject)connection.getContent();
            JSONArray results = response.getJSONArray("results");
            JSONArray address_components = results.getJSONArray(0);
            JSONObject route = address_components.getJSONObject(0);
            via = route.getString("long_name");
        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return via;

    }

    public String nomeCittà()
    {
        final String[] città = {null};
        prendiPosizione();
        /*try{
            String coordinate_s=this.coordinate[0]+","+this.coordinate[1];
            String url_s=indirizzo_p1+coordinate_s+indirizzo_p2;
            url= new URL(url_s);

            HttpURLConnection connection= (HttpURLConnection) url.openConnection(); //Cast a url
            JSONObject response = (JSONObject)connection.getContent();
            JSONArray results = response.getJSONArray("results");
            JSONArray address_components = results.getJSONArray(0);
            JSONObject route = address_components.getJSONObject(5);
            città = route.getString("long_name");

        }

        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

        Server.Server.callReverseGeocoding(context, this.coordinate, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                JSONArray results = response.getJSONArray("results");
                JSONArray address_components = results.getJSONArray(0);
                JSONObject route = address_components.getJSONObject(5);
                città[0] = route.getString("long_name");
            }

            @Override
            public void onError(String errore) throws Exception {

                Log.e("Posizione chiamata API", errore);
            }
        });

        return città[0];
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
