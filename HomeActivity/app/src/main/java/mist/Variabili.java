package mist;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceScreen;

public class Variabili {

    public String nomeUtente=null;
    public String password=null;

   public static boolean ricordami =false;



   //Metodo privato per il salvataggio dei dati in maniera permanente
    public static void salvaUsernamePassword(Context context, String...strings)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USERNAME_PASSWORD", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("USERNAME", strings[0]);
        editor.putString("PASSWORD", strings[1]);
        editor.apply();
    }

    //Salvo lo stato della chechBox "Ricordami"
    public static void salvaRicordaUtente(Context context, boolean stato)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("RICORDAMI", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("STATO", stato);
        editor.apply();
    }

    public static void salvaPromemoriaNotifica(Context context, String orario)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PROMEMORIA_NOTIFICA", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("STATO", orario);
        editor.apply();
    }

    public static void salvaToken(Context context, String token)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TOKEN", token);
        editor.apply();
    }

    public static void salvaDestinazione(Context context, String città)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("DESTINAZIONE_VIAGGIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("DESTINAZIONE_VIAGGIO", città);
        editor.apply();
    }

    public static void salvaMappa(Context context, String città)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MAPPE_SCARICATE", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MAPPE_SCARICATE", città);
        editor.apply();
    }



}
