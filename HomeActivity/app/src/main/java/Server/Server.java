package Server;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;


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

    //La classe dovrebbe supporta il ws per le notifiche push che ci avvisano di
    //impedimenti, parcheggio che sta per scadere e via dicendo
    //é quindi fondamentale che la classe funzioni, altrimenti l'app non ha senso

    public class WSApi extends WebSocketListener {


        private static final int NORMAL_CLOSURE_STATUS = 1000;

        public String tokenToConvert;

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            /* webSocket.send("Hello, it's SSaurel !");
            webSocket.send("What's up ?");
            webSocket.send(ByteString.decodeHex("deadbeef"));

             */
            webSocket.close(NORMAL_CLOSURE_STATUS, "Goodbye !");
        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {

            //Estrapolo la città
            //MI arriva un JSON, quindi se divido con : avrò sempre i campi e il precedente valore nello stessa stringa.
            //A me serve l'ultimo valore dell'ultimo campo. quindi son fortunato
            //da questo devo estrapolare l'informazione sul luogo, orario città ecc
            //Il filtraggio per città dovrà essere fatto prima
            //Il filtraggio per zona verrà fatto dopo
        String[] subStringsToCollect = text.split(":");
        String stringToBeSubstringed = subStringsToCollect[subStringsToCollect.length - 1];


        String messageReceived = stringToBeSubstringed.substring(stringToBeSubstringed.indexOf(","),stringToBeSubstringed.indexOf("}") -1);
        messageReceived = messageReceived.substring(messageReceived.indexOf(","),messageReceived.indexOf("]") -1);

        Context context=null;
        context = context.getApplicationContext();
        //Recupero la città dove ho parcheggiato
            SharedPreferences sharedPreferences=  context.getSharedPreferences("PARCHEGGIO", MODE_PRIVATE);
        String viaAttuale= sharedPreferences.getString("PARCHEGGIO", "" );

        if(messageReceived.compareTo(viaAttuale)==0){
            //invia notifica push
        }



        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

        }

        public String createUrl() {
            return "wss://" +  "http://students.atmosphere.tools"  +  "/v1/streams?thing=city&token=" + tokenToConvert ;
        }

    } //end class WSApi Listener

    public void startWS(Context context) throws JSONException {
        Token( new Callback() {

            @Override
            public void onSuccess(JSONObject result) throws JSONException, IOException, InterruptedException {

                //La chiama all'API si fa tramite il token convertito in stringa
                //si costruisce l'url tramite cui si fa l'accesso usando il token ricevuto
                String risposta=result.toString();


                //Verifica che sia passato per di qua
                Log.i("WS onSuccess", "siamo riusciti a farci dare il token");

                //Ora comincia l'apertura del ws
                OkHttpClient client = new OkHttpClient();
                WSApi listener = new WSApi();
                listener.tokenToConvert=risposta.substring(10, risposta.indexOf("}")-1);
                Request request = new Request.Builder().url(listener.createUrl()).build();
                WebSocket ws = client.newWebSocket(request, listener);


            }

            @Override
            public void onError(ANError error) throws Exception {
                Log.e("WS on Error", error.toString());
                Log.e("Ws on Error" , "non siamo riusciti a farci dare il token");
            }
        } ,context);
    }


}



