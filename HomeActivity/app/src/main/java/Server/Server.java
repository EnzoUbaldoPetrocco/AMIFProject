package Server;

import android.app.DownloadManager;
import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.parkingapp.homeactivity.HomeActivity;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server {

    //Codice per definire se la post è andata a buon fine o no (500= generico errore server)
    private static String codice_risposta="500";

    public static String postToken(Context context)
    {
        String str= CreazioneJson.signinJson("parking-username", "parking-password");

        String token= null;
        try {
            token = post(str, "http://students.atmosphere.tools/v1/login", context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }




    public static String post(String data, String url, Context context) throws IOException {

        final String TAG="SERVER_POST_SIGNIN"; //Tag per controlli di verifica
        final String datiJson=data;




        RequestQueue requestQueue= Volley.newRequestQueue(context); //Mi faccio passare il contesto della SigninActivity, non so se è giusto
        StringRequest stringRequest= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                    Log.i(TAG, "Post riuscita:  " + response);
                    codice_risposta = "200";

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "Post non andata a buon fine:  "+error);
                codice_risposta="409"; //Spero che i codici siano giusti, comunque da rivedere per sicurezza

            }
        })
        {
            @Override //Parte copiata, da discutere assieme
            public String getBodyContentType() {return "application/json; charset=utf-8";}

            @Override
            public byte[] getBody() throws AuthFailureError{
                try {
                    return datiJson== null ? null: datiJson.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException uee){
                    return null;
                }
            }
        };

        requestQueue.add(stringRequest);

        return codice_risposta;

    }


}
