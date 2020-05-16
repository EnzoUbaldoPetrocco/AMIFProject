package asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import com.parkingapp.homeactivity.HomeActivity;
import com.parkingapp.homeactivity.SigninActivity;

import java.io.IOException;

import Server.Server;
import Server.CreazioneJson;


public class AsyncTaskSigninActivity extends AsyncTask<String, Integer, Integer> {

    private ProgressBar progressBar=null;
    private Context context=null;




    public AsyncTaskSigninActivity(ProgressBar pb, Context context){
        this.progressBar=pb;
        this.context=context;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.progressBar.setVisibility(View.VISIBLE);

    }

    @Override //Non so come ritornare il codice di risposta
    protected Integer doInBackground(String...strings) {

        String username=strings[0];
        String password=strings[1];
         Integer codice_risposta=500;

        //Creo oggetto Json da inviare
        String data= CreazioneJson.signinJson(username, password);

        try {
             codice_risposta=(Server.postSignin(data, this.context));
        } catch (IOException e) {
            e.printStackTrace();
        }

        SigninActivity.codice_risultato=codice_risposta;
        return null;
    }

    @Override
    protected void onPostExecute(Integer i) {
        super.onPostExecute(i);

        progressBar.setVisibility(View.INVISIBLE);

    }
}
