package com.mafi.prendivaloriaccelerometro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    Button bttAvvia=null;
    Button bttStop = null;


    private ArrayList<Float[]> accelerometerValues = null; //Contiene i campioni accelerometrici

    //QUESTI SONO I 3 OGGETTI CHE CI SERVONO PER INTERFACCIARCI CON L'ACCELLEROMETRO
    private SensorManager sensorManager=null; //Tutti i sensori
    private Sensor accelerometer=null;
    private SensorEventListener sel= null;

    private static String TAG = "Main Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttAvvia=findViewById(R.id.bttAvvia);
        bttStop= findViewById(R.id.bttStop);


        accelerometerValues=new ArrayList<Float[]>();
        sel=this;

        //Accedo ai sensori disponibili sul telefono
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null)
            accelerometer=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        bttAvvia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sensorManager.registerListener(sel, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            }
        });

        bttStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sensorManager.unregisterListener(sel, accelerometer);

                String storeDir= Environment.getExternalStorageDirectory()+ "/Dati";
                File f= new File(storeDir);
                if(!f.exists()){
                    f.mkdir();
                    if(!f.mkdir()){
                        Log.e(TAG, "Cannot create download directory");
                    }
                }

                String   fullName= storeDir + "/Dati.txt";
                try {
                    FileOutputStream fw= new FileOutputStream(fullName, true);
                    while (!accelerometerValues.isEmpty())
                    {
                        Float x[] = accelerometerValues.get(0);
                        fw.write((x[0].toString()).getBytes());
                        fw.write(",   ".getBytes());
                        fw.write((x[1].toString()).getBytes());
                        fw.write(",   ".getBytes());
                        fw.write((x[2].toString()).getBytes());
                        fw.write("   \n".getBytes());

                        accelerometerValues.remove(0);
                    }

                    fw.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });



    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x, y, z;
        x= event.values[0];
        y= event.values[1];
        z= event.values[2];

        accelerometerValues.add(new Float[] {x, y, z});
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

}
