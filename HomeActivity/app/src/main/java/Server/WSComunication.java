package Server;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.androidnetworking.error.ANError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import Notifica.Notifica;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class WSComunication {

    //La classe dovrebbe supporta il ws per le notifiche push che ci avvisano di
    //impedimenti, parcheggio che sta per scadere e via dicendo
    //é quindi fondamentale che la classe funzioni, altrimenti l'app non ha senso

    Context context;

    public class WSApi extends WebSocketListener {


        private static final int NORMAL_CLOSURE_STATUS = 1000;


        @Override
        public void onOpen(WebSocket webSocket, Response response) {

            Log.i("WS aperto", "onOpen: "+response);
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
            String stringToBeSubstringed = subStringsToCollect[subStringsToCollect.length - 2];


            String messageReceived = stringToBeSubstringed.substring(stringToBeSubstringed.indexOf(","),stringToBeSubstringed.indexOf("}") -1);

            String[] messageSplitted = messageReceived.split("\"");
            messageReceived=messageSplitted[1].replace("\\", "");

            Log.i("messaggio ricevuto", messageReceived);

            if(filtroVicinanza(messageReceived, context)){
                //invia notifica push

            /*
           String oraInizio= messageReceived.substring(1, 6);
           String oraFine = messageReceived.substring(7, 12);
           String[] campi=messageReceived.split(";");
           String giorno = campi[1];
           String testo=campi[2];
           String via= campi[3];



          */

                String[] campi=messageReceived.split(";");
              //  String ora_convertire=campi[0].substring(1, 13)+":"+campi[0].substring(15, 16)+":"+campi[0].substring(18, 19);
                String ora_convertire = campi[0].replace("&", ":");

                for (int i=0; i<campi.length; i++) {
                    Log.i("Campo", campi[i]);
                }
                Log.i("Ora da convertire", ora_convertire);

                Notifica notifica = new Notifica();
                notifica.process("Impedimento", campi[1], ora_convertire ,context);

            }


         Log.i("Messaggio ricevuto", text);

        }
        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {

            Log.i("onMessage", "qui");
        }
        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            Log.i("onClosing", "qui");
            webSocket.close(NORMAL_CLOSURE_STATUS, null);

        }
        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {

            Log.e("Fallimento WS", "Web socket non funziona");
        }

        public String createUrl(String tokenToConvert) {
            String url="ws://students.atmosphere.tools/v1/streams?thing=city&token=" + tokenToConvert ;
           // String url ="ws://echo.websocket.org";
            Log.i("URL WS", url);
            return url;

        }

    } //end class WSApi Listener

    public void startWS(Context context) throws JSONException {

        this.context=context;

        Server.Token( new Callback() {
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
                String tokenToConvert=risposta.substring(10, risposta.indexOf("}")-1);
                Request request = new Request.Builder().url(listener.createUrl(tokenToConvert)).build();
                WebSocket ws = client.newWebSocket(request, listener);

                client.dispatcher().executorService().shutdown();

                Log.i("Websocket aperto", "websocket aperto");

            }

            @Override
            public void onError(ANError error) throws Exception {
                Log.e("WS on Error", error.toString());
                Log.e("Ws on Error" , "non siamo riusciti a farci dare il token");
            }
        } ,context);
    }

    private boolean filtroVicinanza(String messaggioRicevuto, Context context)
    {
        //Recupero la città dove ho parcheggiato
        SharedPreferences sharedPreferences=  context.getSharedPreferences("PARCHEGGIO", MODE_PRIVATE);
        String viaAttuale= sharedPreferences.getString("VIA PARCHEGGIO", null );

        String[] campi=messaggioRicevuto.split(";");
      //  String coordinate = campi[4];


        return true;
    }

}