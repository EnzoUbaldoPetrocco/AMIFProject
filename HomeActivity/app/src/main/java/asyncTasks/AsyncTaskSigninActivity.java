package asyncTasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AsyncTaskSigninActivity extends AsyncTask {

    private ProgressBar progressBar=null;



    public AsyncTaskSigninActivity(ProgressBar pb){
        this.progressBar=pb;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressBar.setVisibility(View.VISIBLE);

    }

    @Override
    protected Object doInBackground(String...strings) throws IOException {

        String username=strings[0];
        String password=strings[1];

          switch(Server.Server.postSignin(username,password))
          {
              case
          }



        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        progressBar.setVisibility(View.INVISIBLE);

    }
}
