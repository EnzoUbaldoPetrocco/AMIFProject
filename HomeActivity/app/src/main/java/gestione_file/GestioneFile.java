package gestione_file;

import android.os.Environment;
import android.util.Log;

import java.io.File;

public class GestioneFile {

    final static String TAG="Gestione file";

    public static String createDir(){
        String storeDir= Environment.getExternalStorageDirectory()+ "/RICORDAMI";
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
