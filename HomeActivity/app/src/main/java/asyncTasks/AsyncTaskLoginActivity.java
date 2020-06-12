package asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.parkingapp.homeactivity.LogInActivity;
import com.parkingapp.homeactivity.SigninActivity;

import org.json.JSONException;
import org.json.JSONObject;

import Server.VolleyCallback;
import mist.Variabili;

public class AsyncTaskLoginActivity extends AsyncTask<String, String, Object> {

  private Context context=null;
  private ProgressBar progressBar=null;
  private CheckBox checkBox=null;
  private TextView messaggioErrore=null;

  public  AsyncTaskLoginActivity(Context context, ProgressBar pb, CheckBox cb, TextView errore)
  {
      this.context=context;
      this.progressBar=pb;
      this.checkBox=cb;
      this.messaggioErrore=errore;
  }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(final String...strings) {

      final String username= strings[0];
      final String password = strings[1];

      Server.Server.makeGet("/v1/things/"+username+"_"+password, new VolleyCallback() {
          @Override
          public void onSuccess(JSONObject result) throws JSONException {

              Log.i("GET ASYNC LOGIN:", result.toString());

              LogInActivity.ritornoDaAsyncTask(true);

              Variabili.salvaUsernamePassword(context, strings);
              Variabili.salvaRicordaUtente(context, checkBox.isChecked());

          }
          @Override
          public void onError(VolleyError error) throws Exception {

              Log.e("CALLBACK MAKE GET", "ERRORE NELLA CALL BACK DELLA MAKE GET, In Login Activity");
              if(error instanceof ServerError || error instanceof AuthFailureError)
              {
                  messaggioErrore.setText("Username/Password errati");
                  messaggioErrore.setVisibility(View.VISIBLE);
              }

              else if(error instanceof TimeoutError || error instanceof NoConnectionError)
              {
                  messaggioErrore.setText("Connessione ad internet assente");
                  messaggioErrore.setVisibility(View.VISIBLE);
              }
              else if(error instanceof NetworkError)
              {
                  messaggioErrore.setText("Errore di connessione, riprova");
                  messaggioErrore.setVisibility(View.VISIBLE);
              }

              else if (error instanceof ParseError) {
              }
          }
      }, context);

        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
