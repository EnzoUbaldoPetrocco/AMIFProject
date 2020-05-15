package Server;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Server {



    public static int postSignin(String username, String password) throws IOException {

        URL url=new URL("http://students.atmosphere.tools/v1/login");
        HttpURLConnection urlConnection= (HttpURLConnection) url.openConnection();



    }
}
