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


    @Override //Non so come ritornare il codice di risposta
    protected Integer doInBackground(final String...strings) {

        String url= "/v1/things/";
        String username=strings[0];
        String password = strings[1];

        String nomiJson[]={"_id"};
       Map<String, String> oggettoJson= CreazioneJson.createJson(nomiJson, username);

         Server.makePost(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                //Log.i("POST ASYNC SINGNIN:", result.toString());
                Log.i("BODY", result.toString());


                SigninActivity.ritornoDaAsyncTask(true);

                Variabili.salvaUsernamePassword(context, strings);
            }

            @Override
            public void onError(String result) throws Exception
            {
                Log.e("CALLBACK MAKE POST", "ERRORE NELLA CALL BACK DELLA MAKE POST, In Signin Activity");
                if(result=="400")
                {
                    messaggioErrore.setText("Potresti dover scegliere un altro username");
                    messaggioErrore.setVisibility(View.VISIBLE);
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
