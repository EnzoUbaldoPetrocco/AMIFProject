package com.parkingapp.homeactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import gestione_file.GestioneFile;

public class MainActivity extends AppCompatActivity {

    final String TAG="MainActivity";
    Button bttMain=null;

    boolean ricordami=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttMain=findViewById(R.id.bttMain);



        //codice per vedere se la cartella RICORDAMI esiste già
        //altrimenti creo la cartella
        //Il controllo è effettuuato suli dati nel file, il file sarà creato sempre

        File f=new File(GestioneFile.createDir());
        if(f.exists())
        {
            try {
                Scanner scanner=new Scanner(f);
                String data=null;
                while(scanner.hasNextLine()) {
                data=scanner.nextLine();
                }
                if(data!=null){
                    ricordami=true;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
        else {
            Log.e(TAG, "Cannot create download directory");
        }

        //se TAG esiste già la cartella, intento per Activityhome
        //se no-->passo activity per login e sign in attraverso intento per login e sign in



       //al premere del bottone se ricordami è false si passa alla schermata di login
        //altrimenti si va direttamente alla home
       bttMain.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               if(ricordami)
                {
                Intent i= new Intent(getString(R.string.MAIN_TO_HOME));
                startActivity(i);
                }
               else
                   {
                   Intent i= new Intent(getString(R.string.MAIN_TO_LOGSIGN));
                   startActivity(i);
                    }
           }
       });
    }

    //crea directory


}
