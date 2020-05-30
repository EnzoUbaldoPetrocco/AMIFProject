package mist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceScreen;

public class Variabili {

    public String nomeUtente=;
    public String password;

   public static boolean ricordami =false;

   private Context context = this.;

    SharedPreferences sharedPreferences = getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);




   //Metodo privato per il salvataggio dei dati in maniera permanente
    public static void salvaUsernamePassword(Context context, String...strings)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USERNAME_PASSWORD", Context.MODE_WORLD_READABLE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", strings[0]);
        editor.putString("PASSWORD", strings[1]);
        editor.apply();
    }

    //Salvo lo stato della chechBox "Ricordami"
    public static void salvaStatoCheckBox(Context context, boolean stato)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("CHECKBOX", Context.MODE_WORLD_READABLE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("STATO", stato);
        editor.apply();
    }

}
