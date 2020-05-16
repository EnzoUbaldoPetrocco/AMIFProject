package asyncTasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.parkingapp.homeactivity.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import gestione_file.GestioneFile;

public class AsyncTaskMainActivity extends AsyncTask {


    boolean FLAG=true;
    final String TAG="AsyncTasckMainActivity";

    boolean ricordami=false;

    //AsyncTasck per vedere se c'è file ricordami, nel caso carica le informazioni da file per mettere a disposizione e avviare HomeActivity.
    //Se non c'è vai a login/singnin



    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] objects) {

        File f=new File(GestioneFile.createDir("/RICORDAMI"));
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







        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);




    }
}
