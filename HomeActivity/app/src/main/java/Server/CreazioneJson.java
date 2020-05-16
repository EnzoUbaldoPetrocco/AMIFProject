package Server;

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
}

