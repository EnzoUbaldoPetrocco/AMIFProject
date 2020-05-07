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

//sarebbe da implementare uno stop, in maniera tale che il sensori si fermi per pochi ms per eseguire la media e il match
//dopodichè riparta.
//questo perché la media basa il suo algoritmo sulla dimensione della lista, se la lista cresce in continuazione
//la media non termina mai il ciclo
//su internet dice che si può usare il metodo: unregisterListener, provo a implementarlo, tuttavia non sono sicuro
    //sull'utilizzo di questo metodo: ho paura che mi cancelli anche i dati

    protected void onStop() {
        sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
    }



//ho deciso di scrivere la media per blocchi di N valori
    //con questo procedimento significa che ogni volta che eseguo la media posso reinizializzare la lista

   public Float[] Media(ArrayList<Float[]> accelerometerValues){

 Float media[]= new Float[2];

    int x=0, y=0, z=0;
    for(int i=0; i<accelerometerValues.size();i++){
        Float Buffer[] = new Float[2];
        Buffer=accelerometerValues.get(i);
        for(int j=0;j<2;j++){
            media[j]+=Buffer[j];
            }
        }
       for(int k=0;k<2;k++){
           media[k]=media[k]/accelerometerValues.size();
    }
    return media;
}


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


