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
import Server.VolleyCallback;

import Server.Server;
import Server.CreazioneJson;


public class AsyncTaskSigninActivity extends AsyncTask<String, Integer, Integer> {

    private Context context=null;
    private ProgressBar progressBar=null;





    public AsyncTaskSigninActivity(ProgressBar pb,Context context){
        this.context=context;
        this.progressBar=pb;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(View.VISIBLE);

    }

    @Override //Non so come ritornare il codice di risposta
    protected Integer doInBackground(String...strings) {

        String url= "http://students.atmosphere.tools/v1/login";

        //!!!!!!NON HAI ANCORA INVIATO NESSUN OGGETTO JSON!!!

        Server.makeRequest(url, new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException {


            }

            @Override
            public void onError(String result) throws Exception {}
        }, context);

        return  0;
    }



    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);

        progressBar.setVisibility(View.INVISIBLE);


    }
}
