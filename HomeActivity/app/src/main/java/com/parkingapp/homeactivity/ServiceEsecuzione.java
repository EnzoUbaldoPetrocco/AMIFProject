package com.parkingapp.homeactivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.parkingapp.homeactivity.Esecuzione;
import com.parkingapp.homeactivity.R;

import java.util.Timer;
import java.util.TimerTask;

import Accelerometro.Accelerometro;
import Notifica.Notifica;
import Posizione.Posizione;
import mist.Variabili;

public class ServiceEsecuzione extends Service {

    Context context;
    Posizione posizione;
    String città_destinazione=null;

//Parametri che uso per l'esecuzione, in parte li inizializzo quando creo il servizio
    boolean esecuzione_città;
    boolean esecuzione_fermo;
    boolean esecuzione_sensore;

    Accelerometro accelerometro;

    //0:nome intero, 1 città, 2 via
    String[] posizione_via_città={null, null, null};
    String[] città_attuale={null};

    Timer myTimer;
    Vibrator vibrazione;

    private PowerManager.WakeLock wakeLock;


    @Override
    public void onCreate() {
        super.onCreate();

        this.context=getApplicationContext();


        //Notifica per segnalare l'esecuzione di un processo in background, riane avviata finché il processo è attivo
        PowerManager powerManager =(PowerManager) getSystemService(POWER_SERVICE);
        assert powerManager != null;
        wakeLock =powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "ParkingAdvisor:wakelock");
        wakeLock.acquire(10*60*1000L /*10 minutes*/);

        Notifica.createNotificationChannel(context, NotificationManager.IMPORTANCE_LOW);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(context, Notifica.CHANNEL_ID)
                    .setContentTitle("Parking Advisor")
                    .setContentText("Esecuzione...")
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.logo_app)
                    .build();

            startForeground(1, notification);

        }

        this.esecuzione_città=true;
        this.esecuzione_fermo=true;
        this.esecuzione_sensore=true;

        SharedPreferences sharedPreferences = context.getSharedPreferences("DESTINAZIONE_VIAGGIO", Context.MODE_PRIVATE);
        this.città_destinazione = sharedPreferences.getString("DESTINAZIONE_VIAGGIO", "");

        this.accelerometro= new Accelerometro(context);

        //Per far vibrare il cellulare quando registra il parcheggio
        this.vibrazione=(Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);

        //Utilizzo un timer per tenere il tempo che devo aspetare prima di nuovi controlli
        this.myTimer = new Timer();

    }



    @Override
    public void onStart(Intent intent, int startId) {

        //onTaskRemoved(intent);

        assert intent != null;
        this.posizione=(Posizione) intent.getParcelableExtra("posizione");

        assert posizione != null;

        posizione.context=this.context; //Inizializzo nuovamente il contesto perché è andato perduto passandolo per l'intento
        posizione.aggiornaGPS(600000, 400);

        posizione.prendiPosizione();


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
                //Aggiorno il GPS ogni minuto e dopo almeno 150 metri di distanza
                //(Soglia di 9 km/h sotto la quale avendo sempre le stesse coordinate, guardo il sensore accelertometrico)
                posizione.fermaAggiornamentoGPS();
                posizione.aggiornaGPS(60000, 150);

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

                                try {
                                    posizione_via_città=posizione.nomeViaECittà();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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

                                    //Faccio vibrare il cellulare per dare conferma che ho registrato il parcheggio
                                    assert vibrazione != null;
                                    vibrazione.vibrate(700);

                                    //Creo la notifica per avvisare l'avvenuto salvataggio del parcheggio
                                    Notifica.creaNotifica(context, "Parcheggio: "+posizione_via_città[2],"Parcheggio salvato");

                                    //Passo all'activity finale in cui mostro il parcheggio sulla mappa
                                    Intent i = new Intent(context.getString(R.string.FRAGMENT_PARCHEGGIO_TO_MOSTRA_SULLA_MAPPA));
                                    context.startActivity(i);
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
                        for (int i=0; i<60; i++) {


                            TimerTask timerTask=new TimerTask() {
                                @Override
                                public void run() {
                                    //Non faccio nulla

                                }
                            };

                            //Riduco il ritardo e gli faccio fare più giri così da tenere sempre sotto controllo se blocco l'asyncTask premendo il pulsante
                            myTimer.scheduleAtFixedRate(timerTask, 600, 1);
                        }
                    }

                }
            }


            //Se non siamo nella città giusta faccio attendere il sistema per una decina di minuti
            else if(esecuzione_città)//Il controllo è obbligatorio perché se ho finito non devo aspettare a caso
            {

                for (int i = 0; i < 1200; i++)
                {

                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {
                            //Non faccio nulla

                        }
                    };

                    //Riduco il ritardo e gli faccio fare più giri così da tenere sempre sotto controllo se blocco l'asyncTask premendo il pulsante
                    myTimer.scheduleAtFixedRate(timerTask, 600, 1);
                    Log.i("TIMER esecuzione_città", "Timer concluso");
                }
            }

        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        esecuzione_città=false;
        esecuzione_fermo=false;
        esecuzione_sensore=false;

        posizione.fermaAggiornamentoGPS();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    /*@Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartIntent=new Intent(context, context.getClass());
        restartIntent.setPackage(context.getPackageName());
        startService(rootIntent);
        super.onTaskRemoved(rootIntent);
    }

     */

}
