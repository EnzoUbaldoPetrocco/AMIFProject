package com.parkingapp.homeactivity;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
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

public class MainActivity extends AppCompatActivity {

    final String TAG="MainActivity";
    Button bttMain=null;
    boolean FLAG=true;
    AsyncTaskMainActivity asyncTaskMainActivity=null;
    //Variabile solo per un test, da cancellare dopo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttMain=findViewById(R.id.bttMain);



        //codice per vedere se la cartella RICORDAMI esiste già
        //altrimenti creo la cartella
        //Il controllo è effettuuato suli dati nel file, il file sarà creato sempre


        //se TAG esiste già la cartella, intento per Activityhome
        //se no-->passo activity per login e sign in attraverso intento per login e sign in






       //al premere del bottone se ricordami è false si passa alla schermata di login
        //altrimenti si va direttamente alla home
       bttMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           //    asyncTaskMainActivity= new AsyncTaskMainActivity();
           //    asyncTaskMainActivity.execute();

               // if (Variabili.ricordami==true) {

           /*      Intent i = new Intent(getString(R.string.MAIN_TO_HOME));
               startActivity(i);

               // }
            else
                    {
                */

           //La scelta per capire se richiedere di identificarsi o no sta sella checkbox, salvata alla voce STATO
               SharedPreferences sharedPreferences = getSharedPreferences("CHECKBOX", Context.MODE_PRIVATE);
               boolean stato = sharedPreferences.getBoolean("STATO", false);
               if (!stato)
               {
                   Intent i= new Intent(getString(R.string.MAIN_TO_LOGSIGN));
                   startActivity(i);
               }

               else
               {
                   Intent i = new Intent(getString(R.string.MAIN_TO_HOME));
                   startActivity(i);
               }


           }
       });
    }



}
