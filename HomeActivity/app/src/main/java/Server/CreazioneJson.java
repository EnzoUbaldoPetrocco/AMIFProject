package Server;

import org.json.JSONException;
import org.json.JSONObject;

public class CreazioneJson {

    private static String data=null;

    //Json per la post dell'account da creare in Signin

    public static String signinJson(String username, String password)
    {
        data= "{"+
                "\"username\"" + "\""+username+"\","+
                "\"password\"" + "\""+password+"\""+
                "}";
        return data;

    }

    //Versione alternativa per la creazione di oggetti Json
    //Nel pimo array vanno scritti i nomi dei campi da creare per il
    /*
    public static JSONObject convert2Json(String[] nomi, String...strings)
    {
        JSONObject jsonObject=new JSONObject();

        try {
            for (int i=0; i<strings.length; i++)
            {
                jsonObject.put(nomi[i], strings[i]);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return  jsonObject;
    }

     */



}

