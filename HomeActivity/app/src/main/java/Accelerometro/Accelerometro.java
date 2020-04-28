package Accelerometro;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.ArrayList;

public class Accelerometro implements SensorEventListener {

    private ArrayList<Float[]> accelerometerValues = null;

    private SensorManager sensorManager=null; //Tutti i sensori
    private Sensor accelerometer=null;
    private SensorEventListener sel= null;






   /*public Float[] Media(ArrayList<Float[]> accelerometerValues){

 Float media[]= new Float[2];
    int x=0, y=0, z=0;
    for(int i=0; i<accelerometerValues.)
}*/


    @Override //Prendiamo i valori dall'accellerometro e li mettiamo nella lista
    public void onSensorChanged(SensorEvent event) {

        float x, y, z;
        x= event.values[0];
        y= event.values[1];
        z= event.values[2];

        accelerometerValues.add(new Float[] {x, y, z});

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {} //A noi non serve
}
