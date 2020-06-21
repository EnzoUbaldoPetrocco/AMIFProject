package mist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Server.VolleyCallback;

import Server.Server;


public class Variabili {


   //Metodo privato per il salvataggio dei dati in maniera permanente
    public static void salvaUsernamePassword(Context context, String...strings)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", strings[0]);
        editor.putString("PASSWORD", strings[1]);
        editor.apply();
    }

    //Salvo lo stato della chechBox "Ricordami"
    public static void salvaRicordaUtente(Context context, boolean stato)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RICORDAMI", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("STATO", stato);
        editor.apply();
    }

    public static void salvaPromemoriaNotifica(Context context, String orario)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PROMEMORIA_NOTIFICA", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("STATO", orario);
        editor.apply();
    }


    public static void salvaDestinazione(Context context, String città)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DESTINAZIONE_VIAGGIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DESTINAZIONE_VIAGGIO", città);
        editor.apply();
    }

    public static void salvaMappa(Context context, String città)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MAPPE_SCARICATE", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(città, città);
        editor.apply();
    }

    public static void salvaParcheggio(Context context, String città_via)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PARCHEGGIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PARCHEGGIO", città_via);
        editor.apply();
    }

  /*  public static void annullaOSalvaParcheggio(Context context, boolean stato)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SCELTA", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SCELTA", stato);
        editor.apply();
    } */

    public static void salvaCoordinate(Context context, double[] coordinate)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("COORDINATE", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LATITUDINE", String.valueOf(coordinate[0]));
        editor.putString("LONGITUDINE", String.valueOf(coordinate[1]));
        editor.apply();
    }

    public static void salvaOrarioParcheggio(Context context, String orario)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ORARIO_PARCHEGGIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ORARIO_PARCHEGGIO", orario);
        editor.apply();
    }


    //METODI PER AGGIORNARE I DATI IN MEMORIA IN BASE AI VALORI DEL SERVER

    public static boolean aggiornaPosizione(final Context context)
    {
        String urlp1="/v1/measurements?filter={\"thing\":\"";
        String urlp2="\"}&limit=10&page=1";

         SharedPreferences sharedPreferences =context.getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);
        String username=sharedPreferences.getString("USERNAME", "")+"_"+sharedPreferences.getString("PASSWORD", "");

         Server.makeGet(urlp1 + username + urlp2, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                JSONArray docs =result.getJSONArray("docs");

                if(docs.length()!=0) { //Controllo per verificare che ci sia un parcheggio salvato, se docs è vuoto non c'è parcheggio per quell'utente

                    JSONObject docs_0 = docs.getJSONObject(0);
                    JSONObject location = docs_0.getJSONObject("location");
                    JSONArray coordinates = location.getJSONArray("coordinates");

                    final double[] coordinate = {coordinates.getDouble(0), coordinates.getDouble(1)};

                    SharedPreferences sharedPreferences1 = context.getSharedPreferences("COORDINATE", Context.MODE_PRIVATE);
                    //Metto a default valori impossibili, così forzo ad aggiornarsi col server se non abbiamo parcheggi salvati
                    String[] coordinate_salvate_s = {sharedPreferences1.getString("LATITUDINE", "1000000"), sharedPreferences1.getString("LONGITUDINE", "1000000")};

                    assert coordinate_salvate_s[0] != null;
                    assert coordinate_salvate_s[1] != null;
                    double[] coordinate_salvate = {Double.valueOf(coordinate_salvate_s[0]), Double.valueOf(coordinate_salvate_s[1])};

                    if (coordinate_salvate[0] != coordinate[0] && coordinate_salvate[1]!=coordinate[1]) {
                        Server.callReverseGeocoding(context, coordinate, new VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject response) throws JSONException {
                                JSONArray results = response.getJSONArray("results");
                                JSONObject formatted_address = results.getJSONObject(1);
                                String città_via = formatted_address.getString("formatted_address");
                                Log.i("NOME CITTA_VIA", città_via);

                                Variabili.salvaParcheggio(context, città_via);
                                Variabili.salvaCoordinate(context, coordinate);
                            }

                            @Override
                            public void onError(VolleyError error) throws Exception {

                                Log.e("REVERSE GEOCODING", "Errore nel reverse geocoding dell'aggiornamento Posizione in Variabili");
                            }
                        });
                    }
                }
                else
                {
                    Variabili.salvaParcheggio(context, "Nessun parcheggio salvato");
                }
            }

            //Se succede un errore carichiamo il dato dalla memoria e accettiamo di non aggiornarlo
            @Override
            public void onError(VolleyError error) throws Exception {
                if(error instanceof ServerError || error instanceof AuthFailureError)
                {
                    Log.e("CARICAMENTO POSIZIONE", "utente non trovato");
                }

                else if(error instanceof TimeoutError || error instanceof NetworkError)
                {
                    Log.e("CARICAMENTO POSIZIONE", "errore internet");
                }

            }
        }, context);

         return true;
    }

}
