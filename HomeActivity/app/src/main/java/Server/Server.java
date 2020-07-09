package Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class Server {



   private static String my_URL = "http://students.atmosphere.tools";

    private static void Token(final Callback callback, Context context) throws JSONException {
        String url= my_URL+"/v1/login";

        String nomiJson[]={"username", "password"};
        JSONObject costanteJson= CreazioneJson.createJSONObject(nomiJson, "parking-username", "parking-password");

        AndroidNetworking.initialize(context);

        AndroidNetworking.post(url)
                .addJSONObjectBody(costanteJson)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                            Log.i("TOKEN", response.toString());
                        } catch (JSONException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        try {
                            callback.onError(anError);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    public static void Post(final String url_add, final Callback callback, final Context context, final JSONObject postJson) throws JSONException {
        final String url=my_URL+url_add;

        Token(new Callback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException, IOException {

                String risposta=result.toString();
                String token=risposta.substring(10, risposta.indexOf("}")-1);

                AndroidNetworking.initialize(context);
                AndroidNetworking.post(url)
                        .addJSONObjectBody(postJson)
                        .addHeaders("Authorization", token)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    callback.onSuccess(response);
                                    Log.i("Risposta Post", response.toString());
                                } catch (JSONException | IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                                try {
                                    callback.onError(anError);
                                    Log.e("Errore Post", anError.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            @Override
            public void onError(ANError error) throws Exception {

                callback.onError(error);
                Log.e("POST", "Errore ritorno Token dalla post");
            }
        }, context);
    }


    public static void Get(final String url_add, final Callback callback, final Context context) throws JSONException {

        final String url=my_URL+url_add;

        Token(new Callback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException, IOException {

                String risposta=result.toString();
                String token=risposta.substring(10, risposta.indexOf("}")-1);

                AndroidNetworking.initialize(context);
                AndroidNetworking.get(url)
                        .addHeaders("Authorization", token)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    callback.onSuccess(response);
                                    Log.i("Risposta Get", response.toString());
                                } catch (JSONException | IOException | InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                                try {
                                    callback.onError(anError);
                                    Log.e("Errore Get", anError.toString());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            @Override
            public void onError(ANError error) throws Exception {

                callback.onError(error);
                Log.e("GET", "Errore ritorno Token dalla GET");
            }
        }, context);
    }


    public static void reverseGeocoding(Context context, final double[] coordinate, final Callback callback)
    {
        String indirizzo_p1 ="https://maps.googleapis.com/maps/api/geocode/json?latlng=";
        String indirizzo_p2="&key=AIzaSyA09MsWgTgGZvMh8KDjyNtxm8ovRYoq1Dg";
        String url=indirizzo_p1+coordinate[0]+","+coordinate[1]+indirizzo_p2;

        AndroidNetworking.initialize(context);
        AndroidNetworking.get(url)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            callback.onSuccess(response);
                            Log.i("Ritorno Geocoding", response.toString());
                        } catch (JSONException | IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        try {
                            callback.onError(anError);
                            Log.e("Errore Geocoding", anError.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}



