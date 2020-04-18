package com.parkingapp.homeactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Time;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    final String TAG="MainActivity";
    Button bttMain;
    boolean ricordami=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //codice per vedere se la cartella RICORDAMI esiste già
        //altrimenti creo la cartella

        File f=new File(createDir());
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

        //se TAG esiste già la cartella, intento per Activityhome
        //se no-->passo activity per login e sign in attraverso intento per login e sign in

       bttMain=findViewById(R.id.bttMain);


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
                    }
           }
       });
    }

    //crea directory
    private String createDir(){
        String storeDir= Environment.getExternalStorageState()+ "/RICORDAMI.txt";
        File f= new File(storeDir);
        if(!f.exists()){
            f.mkdir();
            if(!f.mkdir()){
                Log.e(TAG, "Cannot create download directory");
                return  null;
            }
            else return storeDir;
        }
        else return storeDir;
    }

}
