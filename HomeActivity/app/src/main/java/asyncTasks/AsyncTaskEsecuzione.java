package asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Button;

import Accelerometro.Accelerometro;
import Posizione.Posizione;
import mist.Variabili;

public class AsyncTaskEsecuzione extends AsyncTask{

    private boolean valoreAccellerometro = false;
    Context context;
    Button annulla;
    Button registraParcheggio;

    public AsyncTaskEsecuzione(Context context, Button annulla, Button registraParcheggio)
    {
        this.context=context;
        this.annulla=annulla;
        this.registraParcheggio=registraParcheggio;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Object doInBackground(Object[] objects) {

        boolean fine= false;
        boolean esecuzione=true;

        boolean città=false;

        SharedPreferences sharedPreferences = context.getSharedPreferences("DESTINAZIONE_VIAGGIO", Context.MODE_PRIVATE);
        String nome_città=sharedPreferences.getString("DESTINAZIONE_VIAGGIO", "");


        Accelerometro accelerometro= new Accelerometro();
        Posizione posizione = new Posizione(context);
        String città_attuale= posizione.nomeCittà();

        while (esecuzione) {

            while(posizione.èFermo()) {

                if (nome_città == posizione.nomeCittà()) {
                    try {
                        wait(180000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                while (!fine) {
                    fine = accelerometro.esegui();
                }
                String via = posizione.nomeVia();
                Variabili.salvaParcheggio(context, città_attuale, via);
                Variabili.salvaCoordinate(context, posizione.coordinate);
            }
        }

     int k=2;
     while(k!=1)
        {
            k++;
        }

        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();

        SharedPreferences sharedPreferences = context.getSharedPreferences("SCELTA", Context.MODE_PRIVATE);
        boolean scelta=sharedPreferences.getBoolean("SCELTA", false);

        if(scelta) {
            Posizione posizione = new Posizione(context);
            Variabili.salvaParcheggio(context, posizione.nomeCittà(), posizione.nomeVia());
            Variabili.salvaCoordinate(context, posizione.coordinate);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

}
