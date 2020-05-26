package asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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


public class AsyncTaskSigninActivity extends AsyncTask<String, Integer, Integer> {

    private Context context=null;
    private ProgressBar progressBar=null;



    public AsyncTaskSigninActivity(ProgressBar pb,Context context){
        this.context=new WeakReference<>(context).get();
        this.progressBar=pb;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(View.VISIBLE);

    }


    @Override //Non so come ritornare il codice di risposta
    protected Integer doInBackground(String...strings) {

        String url= "/v1/things/";
        String username=strings[0];
        String password = strings[1];

        String nomiJson[]={"_id"};
       Map<String, String> oggettoJson= CreazioneJson.createJson(nomiJson, username);

        //ABBIAMO DEI PROBLEMI A PASSARE IL CONTESTO
         int codiceRitorno = Server.makePost(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {

                Log.i("POST ASYNC SINGNIN:", result.toString());
            }

            @Override
            public void onError(String result) throws Exception {}
        }, this.context, oggettoJson);
        /* Server.postToken(new VolleyCallback() {
                              @Override
                              public void onSuccess(JSONObject result) throws JSONException {
                                  Log.i("FUNZIONA", "La post token è andata a buon fine");
                                  Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();
                              }

                              @Override
                              public void onError(String result) throws Exception {
                                  Log.e("POST-TOKEN", "Post token fallita");
                                  Toast.makeText(context, result.toString(), Toast.LENGTH_LONG).show();

                              }
                          }, context
         );*/


        return  0;
    }



    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);

        progressBar.setVisibility(View.INVISIBLE);


    }
}
