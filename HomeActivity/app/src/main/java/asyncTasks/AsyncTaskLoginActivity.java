package asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ProgressBar;

import mist.Variabili;

public class AsyncTaskLoginActivity extends AsyncTask<String, String, Object> {

  private Context context=null;
  private ProgressBar progressBar=null;
  private CheckBox checkBox=null;

  public  AsyncTaskLoginActivity(Context context, ProgressBar pb, CheckBox cb)
  {
      this.context=context;
      this.progressBar=pb;
      this.checkBox=cb;
  }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String...strings) {

      String username= strings[0];
      String password = strings[1];


      //Salvo i dati
        Variabili.salvaUsernamePassword(context, strings);



        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        progressBar.setVisibility(View.INVISIBLE);
    }

}
