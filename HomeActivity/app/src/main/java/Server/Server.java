package Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import mist.Variabili;

public class Server {



   private static String my_URL = "http://students.atmosphere.tools";

    public static void postToken(final VolleyCallback volleyCallback, final Context context){
        String url= my_URL+"/v1/login";

        String nomiJson[]={"username", "password"};
        Map<String, String> costanteJson= CreazioneJson.createJson(nomiJson, "parking-username", "parking-password");

        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST,
                url, costanteJson, new Response.Listener() {

            @Override
            public void onResponse(Object responseObj) {
                String risposta = responseObj.toString();
                if (!risposta.equals(null)) {

                    Log.i("Array di risposta: ", risposta);
                } else {
                    Log.e("Array di risposta: ", "Data Null");
                }
                try {

                    volleyCallback.onSuccess((JSONObject)responseObj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError errore) {
                Log.e("ERRORE-POST-TOKEN ", "" + errore.toString());
                try {
                    volleyCallback.onError(errore.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {

            //E' per prendere l'header
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> parametri = new HashMap<String, String>();
                return parametri;
            }

            //Passi i tui parametri qui
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parametri = new HashMap<String, String>();
                parametri.put("username", "parking-username");
                parametri.put("password", "parking-password");

                return parametri;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(rq);


    }


    //Metodo per la post nella signin, serve quando si registra un nuovo utente per farlo registrare sul database
    public static void makePost(final String url_add, final VolleyCallback callback, final Context context, final Map<String, String> postJson) {

     final   String url=my_URL+url_add; //Creo l'URL con quello base più la nuova parte in base ala post che devo fare


        //Chiamp la postToken per otternere l'header di autorizzazione per la mia Post
        postToken(new VolleyCallback() {


            @Override //Quando la postToken è andata a buon fine torna l'header
            public void onSuccess(final JSONObject result) throws JSONException {

                //Ragiono in maniera speculare alla postToken
                CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST,
                        url, postJson,
                        new Response.Listener() {

                            @Override //Quello che mi aspetto una volta ottenuto l'header: lo aggiungo come autorizzazione e faccio la post
                            public void onResponse(Object responseObj) {
                                JSONObject risposta = (JSONObject) responseObj;
                                Log.i("Risposta", risposta.toString());
                                try {
                                        callback.onSuccess(risposta);
                                    }
                                 catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e("ERRORE MakePost", "Errore nella callBack");
                                }
                            }
                        },

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError errore) {
                                VolleyLog.d("ERRORE", "errore nella post: "+errore.toString());
                                try {
                                    callback.onError(errore.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }) {

                    @Override //Header della mia richiesta di Post
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                       /* SharedPreferences sharedPreferences = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
                        String token = sharedPreferences.getString("TOKEN", ""); */

                       //Rimuovo parti inutili dal token
                        String risposta=result.toString();
                        String token=risposta.substring(10, risposta.indexOf("}")-1);
                        //Variabili.salvaToken(context, token); //Salvo il token

                        Log.i("TOKEN", token);

                        headers.put("Authorization", token);

                        return headers;
                    }

                    @Override //Corpo di quello che sto postando
                    protected Map<String, String> getParams() {
                        Map<String, String> params = postJson;
                        return params;
                    }

                };

                // Request added to the RequestQueue
                VolleyController.getInstance(context).addToRequestQueue(rq);
                /*RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(rq);*/
            }

            @Override //Quello che succede se l'header non torna come deve dalla postToken
            public void onError(String result) throws Exception {
                Log.e("ERRORE makePost", "Errore nel ritorno dalla callback della postToken");

            }
        }, context);
    }

    //Metodo per le chiamate dell'API di Google Maps per il reverse geocoding
    //Problemi di conversione double a String
    public static void callReverseGeocoding(Context context, final double[] coordinate, final VolleyCallback volleyCallback)
    {
        String indirizzo_p1 ="https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        String indirizzo_p2="&key=AIzaSyA09MsWgTgGZvMh8KDjyNtxm8ovRYoq1Dg";
        String url=indirizzo_p1+coordinate[0]+","+coordinate[1]+indirizzo_p2;

        CustomJSONObjectRequest rq= new CustomJSONObjectRequest(Request.Method.GET,
                url, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                try {
                    volleyCallback.onSuccess((JSONObject)response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("CHIAMATA MAPS API", error.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(rq);
    }
}
