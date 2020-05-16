package asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parkingapp.homeactivity.HomeActivity;
import com.parkingapp.homeactivity.R;
import com.parkingapp.homeactivity.SigninActivity;

import java.io.IOException;

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


        String token= Server.postToken(context);

        Toast.makeText(context,"token:" + token, Toast.LENGTH_LONG).show();

        String username=strings[0];
        String password=strings[1];
         String codice_risposta="400";

        //Creo oggetto Json da inviare
        String data= CreazioneJson.signinJson(username, password);

        try {
             codice_risposta=Server.post(data, "http://students.atmosphere.tools/v1/login", this.context);
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
