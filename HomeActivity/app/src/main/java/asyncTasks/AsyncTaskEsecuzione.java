package asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.parkingapp.homeactivity.Esecuzione;

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

        Accelerometro accelerometro= new Accelerometro();
        Posizione posizione = new Posizione(context);

        //0:nome intero, 1 città, 2 via
        String posizione_via_città[]=posizione.nomeViaECittà();
        String nome_intero=posizione_via_città[0];
        String città_attuale= posizione_via_città[1];
        String via=posizione_via_città[2];

      /*  while (esecuzione) {

            while(posizione.èFermo()) {

                if (città_attuale == posizione.nomeViaECittà()[1]) {
                    try {
                        wait(180000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                while (!fine) {
                    fine = accelerometro.esegui();
                }
                Variabili.salvaParcheggio(context, nome_intero);
                Variabili.salvaCoordinate(context, posizione.coordinate);
            }
        } */

     int k=2;
     while(k!=1)
        {
            k++;
        }

        return null;
    }

    //NON FUNZIONA TANTO VALE FARGLI FARE QUESTE COSE IN ESECUZIONE
    @Override
    protected void onCancelled() {
        super.onCancelled();


    }



    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }


}
