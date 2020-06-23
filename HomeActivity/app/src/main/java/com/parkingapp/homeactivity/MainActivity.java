package com.parkingapp.homeactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.timeseries.TimeSeries;
import com.util.DistanceFunction;
import com.util.DistanceFunctionFactory;

import asyncTasks.AsyncTaskMainActivity;
import mist.Variabili;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.MANAGE_OWN_CALLS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    final String TAG="MainActivity";
    Button bttMain=null;
    Context context=this;
    boolean FLAG=true;
    AsyncTaskMainActivity asyncTaskMainActivity=null;
    //Variabile solo per un test, da cancellare dopo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttMain=findViewById(R.id.bttMain);

        //Controllo che i permessi siano abilitati all'avvio dell'app
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE}, 1);


       //al premere del bottone se ricordami è false si passa alla schermata di login
        //altrimenti si va direttamente alla home
       bttMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               //Se i permessi non ci sono li richiedo ogni volta che prova a far iniziare l'app
               if(requestPermission(ACCESS_FINE_LOCATION)&&requestPermission(WRITE_EXTERNAL_STORAGE)) {
                   //La scelta per capire se richiedere di identificarsi o no sta sella checkbox, salvata alla voce STATO
                   SharedPreferences sharedPreferences = getSharedPreferences("RICORDAMI", Context.MODE_PRIVATE);
                   boolean stato = sharedPreferences.getBoolean("STATO", false);
                   if (!stato) {
                       Intent i = new Intent(getString(R.string.MAIN_TO_LOGSIGN));
                       startActivity(i);
                   } else {

                       //Aggiorno i  parametri andando a prendeere dal server
                       if(Variabili.aggiornaPosizione(context)) {
                           Intent i = new Intent(getString(R.string.MAIN_TO_HOME));
                           startActivity(i);
                       }
                   }
               }


           }
       });
    }

    //Chiedo all'utente di attivare tutti i permessi per l'applicazione
    private boolean requestPermission(final String permission)
    {
        final boolean[] permesso = {true};//Valore per capire se il permesso è abilitato, array perchè se no non lo prende dopo

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
            permesso[0]=false;

            new AlertDialog.Builder(this)
                    .setTitle("Permessi obbligatori")
                    .setMessage("I permessi sono necessari per il corretto funzionamento dell'applicazione")
                    .setPositiveButton("Concedi permessi", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{ACCESS_FINE_LOCATION, WRITE_EXTERNAL_STORAGE}, 1);
                            permesso[0]=true;
                        }
                    })
                    .setNegativeButton("Nega", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            permesso[0] =false;
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else
        {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 1);
        }
        return permesso[0];
    }

}
