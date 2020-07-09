package asyncTasks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.FontsContract;
import android.util.Log;
import android.widget.Button;


import androidx.annotation.RequiresApi;

import com.parkingapp.homeactivity.Esecuzione;
import com.parkingapp.homeactivity.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import Accelerometro.Accelerometro;
import Posizione.Posizione;
import Server.Server;
import mist.Variabili;


public class AsyncTaskEsecuzione extends AsyncTask{

    Context context;
    Button annulla;
    Button registraParcheggio;
     Posizione posizione;
    String città_destinazione=null;

    //Mi faccio passare anche un oggetto posizione così è sempre lo stesso e posso decidere come modificare la precisione del GPS
    public AsyncTaskEsecuzione(Context context, Button annulla, Button registraParcheggio, Posizione posizione)
    {
        this.context=context;
        this.annulla=annulla;
        this.registraParcheggio=registraParcheggio;
        this.posizione=posizione;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        //Il GPS si aggiorna ogni 10 minuti e dopo almeno 400 metri di distanza
        posizione.aggiornaGPS(600000, 400);

        posizione.prendiPosizione();

        SharedPreferences sharedPreferences = context.getSharedPreferences("DESTINAZIONE_VIAGGIO", Context.MODE_PRIVATE);
        città_destinazione = sharedPreferences.getString("DESTINAZIONE_VIAGGIO", "");
    }


    @Override
    protected Object doInBackground(Object[] objects) {

        boolean esecuzione_città=true;
        boolean esecuzione_fermo=true;
        boolean esecuzione_sensore=true;



        Accelerometro accelerometro= new Accelerometro(context);

        //0:nome intero, 1 città, 2 via
        String[] posizione_via_città={null, null, null};
        String[] città_attuale={null};


        //Utilizzo un timer per tenere il tempo che devo aspetare prima di nuovi controlli
        Timer myTimer= new Timer();
        TimerTask timerTask=new TimerTask() {
            @Override
            public void run() {
                //Non faccio nulla
            }
        };

        

      //Primo step: controllo in un loop infinito di trovarmi nella città giusta
      while (esecuzione_città)
      {
          try {
              posizione_via_città=posizione.nomeViaECittà();
          } catch (InterruptedException e) {
              e.printStackTrace();
          }

          città_attuale[0] = posizione_via_città[1];
          assert città_attuale[0] != null;
          if(città_attuale[0].equals(città_destinazione))
          {
              //Aggiorno il GPS ogni trenta secondi e dopo almeno un metro di distanza
              posizione.fermaAggiornamentoGPS();
              posizione.aggiornaGPS(60000, 7);

              //Secondo step: verifichiamo che la macchina sia ferma
              while (esecuzione_fermo)
              {
                  if(posizione.èFermo())
                  {
                      //Ultimo step: verifico che il sensore accelerometrico mi dica se mi sono alzato
                      while (esecuzione_sensore)
                      {
                          if(accelerometro.esegui()&&posizione.èFermo())
                          {
                              try {
                                  posizione.nomeViaECittà();
                              } catch (InterruptedException e) {
                                  e.printStackTrace();
                              }

                              posizione_via_città=posizione.nomeCittà_via;

                              //Già che ho anche il nome della città faccio un'ultima verifica per vedere se mi trovo nella città giusta
                              //per avere un sistema più robusto
                              if(posizione_via_città[1].equals(città_destinazione)) {

                                  //Ora che ho superato tutti i controlli salvo il parchegio
                                  double[] coordinate = posizione.coordinate;
                                  Variabili.salvaCoordinate(context, coordinate);

                                  Variabili.salvaParcheggio(context, posizione_via_città[0]);

                                  //IMPLEMENTARE LA PARTE IN CUI RECUPERO L'ORARIO DEL LAVAGGIO

                                  //Concludo mettendo a false tutti i loop
                                  esecuzione_città=false;
                                  esecuzione_fermo=false;
                                  esecuzione_sensore=false;
                              }
                              else
                              {
                                  //Se non sono nella città giusta torno al primo step
                                  esecuzione_città=true;
                                  posizione.aggiornaGPS(600000, 400);
                                  break;
                              }
                          }
                          //Faccio nuovamente la verifica perché se fosse ripartito torno al ciclo precedente
                          else if(!posizione.èFermo())
                          {
                              break;
                          }
                      }
                  }

                 else if(esecuzione_fermo)
                  {
                      myTimer.scheduleAtFixedRate(timerTask, 30000, 500);
                  }

              }
          }

          //Se non siamo nella città giusta faccio attendere il sistema per una decina di minuti
        else if(esecuzione_città)//Il controllo è obbligatorio perché se ho finito non devo aspettare a caso
          {
              myTimer.scheduleAtFixedRate(timerTask, 10000, 1000);
              Log.i("TIMER esecuzione_città", "Timer concluso");
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

    }



    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        posizione.fermaAggiornamentoGPS();

    }

}
