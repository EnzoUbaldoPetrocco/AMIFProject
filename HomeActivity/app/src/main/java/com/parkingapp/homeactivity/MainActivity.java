package com.parkingapp.homeactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final String TAG="MainActivity";
    ImageView ImmagineIniziale=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
        ImmagineIniziale=findViewById(R.id.ivSfondoIniziale);


        //Aspetto 4 secondi prima di andare alla schermata con i comandi
        try
        {
            TimeUnit.SECONDS.sleep(4);
        }
        catch(InterruptedException ex)
        {
            Log.e(TAG, "Errore caricamento immmagine");
        }

        startActivity(i); //Passaggio dalla schermata all'activity con comandi
    }
}
