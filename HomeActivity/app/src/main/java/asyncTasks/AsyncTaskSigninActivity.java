package asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.parkingapp.homeactivity.HomeActivity;
import com.parkingapp.homeactivity.R;
import com.parkingapp.homeactivity.SigninActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

import Server.VolleyCallback;

import Server.Server;
import Server.CreazioneJson;
import mist.Variabili;


public class AsyncTaskSigninActivity extends AsyncTask<String, Integer, Integer> {

    private Context context=null;
    private ProgressBar progressBar=null;
    private TextView messaggioErrore=null;



    public AsyncTaskSigninActivity(ProgressBar pb,TextView errore,Context context){
        this.context=new WeakReference<>(context).get();
        this.progressBar=pb;
        this.messaggioErrore=errore;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(View.VISIBLE);

    }


    @Override
    protected Integer doInBackground(final String...strings) {

        String url= "/v1/things/";
        String username=strings[0];
        String password = strings[1];

        String nomiJson[]={"_id"};
       Map<String, String> oggettoJson= CreazioneJson.createJson(nomiJson, username);

         Server.makePost(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                Log.i("POST ASYNC SINGNIN:", result.toString());

                SigninActivity.ritornoDaAsyncTask(true);

                Variabili.salvaUsernamePassword(context, strings);
            }

            @Override//Non restituisce numeri, ma la gestice così
            public void onError(VolleyError error) throws Exception
            {
                Log.e("CALLBACK MAKE POST", "ERRORE NELLA CALL BACK DELLA MAKE POST, In Signin Activity");
                if(error instanceof ServerError || error instanceof AuthFailureError)
                {
                    messaggioErrore.setText(" Username già in uso, prova con un altro");
                    messaggioErrore.setVisibility(View.VISIBLE);
                }

                else if(error instanceof TimeoutError || error instanceof NoConnectionError)
                {
                    messaggioErrore.setText(" Connessione ad internet assente, verifica la tua  connessione dati");
                    messaggioErrore.setVisibility(View.VISIBLE);
                }
                else if(error instanceof NetworkError)
                {
                    messaggioErrore.setText(" Errore di connessione, riprova");
                    messaggioErrore.setVisibility(View.VISIBLE);
                }

              else if (error instanceof ParseError) {
                 Log.e("ParseError", "Errore server asyncTask signin activity");
             }

            }
        }, this.context, oggettoJson);

        return  0;
    }



    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);

        progressBar.setVisibility(View.INVISIBLE);

    }
}
