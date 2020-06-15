package mist;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.util.LocaleData;
import android.preference.PreferenceScreen;


public class Variabili {


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
        editor.putString(città, città);
        editor.apply();
    }

    public static void salvaParcheggio(Context context, String città_via)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("PARCHEGGIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PARCHEGGIO", città_via);
        editor.apply();
    }

  /*  public static void annullaOSalvaParcheggio(Context context, boolean stato)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("SCELTA", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("SCELTA", stato);
        editor.apply();
    } */

    public static void salvaCoordinate(Context context, float[] coordinate)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("COORDINATE", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("LATITUDINE", coordinate[0]);
        editor.putFloat("LONGITUDINE", coordinate[1]);
        editor.apply();
    }

    public static void salvaOrarioParcheggio(Context context, String orario)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences("ORARIO_PARCHEGGIO", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ORARIO_PARCHEGGIO", orario);
        editor.apply();
    }


}
