package Server;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpConnectionNoVolley {
    public void makePostMesur (Context context, final JSONObject JsonPost) throws IOException {

        Server.postToken(new VolleyCallback() {
            @Override
            public void onSuccess(JSONObject result) throws JSONException, IOException {
                URL url = new URL("http://students.atmosphere.tools/v1/measurements");

                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("POST");

                con.setRequestProperty("Content-Type", "application/json; utf-8");

                con.setDoOutput(true);

                String risposta = result.toString();
                String token=risposta.substring(10, risposta.indexOf("}")-1);

                con.setRequestProperty("Authorization", token);

                String stringToPost = JsonPost.toString();


                try (OutputStream os = con.getOutputStream()) {
                    byte[] input = stringToPost.getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    Log.i("RispAPIBertaNoVolley", response.toString());
                }


            }

            @Override
            public void onError(VolleyError error) throws Exception {
                Log.e("ErrorHttpNoVolley" , error.toString());
            }
        }, context);

    }
}
