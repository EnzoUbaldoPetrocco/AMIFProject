package Posizione;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

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

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class Posizione {

    //Indirizzo per il reverse geocoding (latlng=40.714224,-73.961452&key=)
    private String indirizzo_p1 ="https://maps.googleapis.com/maps/api/geocode/json?latlng=";
    private  String indirizzo_p2="&key=AIzaSyA09MsWgTgGZvMh8KDjyNtxm8ovRYoq1Dg";

    private URL url;
    private String coordinate=null;

    public Context context;

    public Posizione (Context context)
    {
        this.context=context;
    }

    public void prendiPosizione()
    {
        requestPermission();//Richiedo permesso localizzazione all'utente
        FusedLocationProviderClient client= LocationServices.getFusedLocationProviderClient(context);

        client.getLastLocation().addOnSuccessListener((Activity)context,new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if (location != null) {
                    coordinate = location.toString();
                    coordinate=coordinate.substring(coordinate.indexOf("fused"), coordinate.indexOf("acc")); //Prendo solo la parte con le coordinate
                }
            }
        });
    }


    public String nomeVia()
    {
        String via=null;
        prendiPosizione();//Aggiorno le coordinate
        try{
            String url_s=indirizzo_p1+coordinate+indirizzo_p2;
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
        String città=null;
        prendiPosizione();
        try{
            String url_s=indirizzo_p1+coordinate+indirizzo_p2;
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
        }

        return città;
    }


    private  void requestPermission()
    {
        ActivityCompat.requestPermissions((Activity)context, new String[]{ACCESS_FINE_LOCATION}, 1);
    }
}
