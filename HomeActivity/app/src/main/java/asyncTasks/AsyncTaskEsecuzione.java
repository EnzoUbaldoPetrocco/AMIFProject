package asyncTasks;

import android.os.AsyncTask;

import Accelerometro.Accelerometro;

public class AsyncTaskEsecuzione extends AsyncTask {

    private boolean valoreAccellerometro = false;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        Accelerometro accelerometro= new Accelerometro();

        while(!valoreAccellerometro)
        {
            valoreAccellerometro = accelerometro.esegui();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
