package Server;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Server {

   private static String my_URL = "http://students.atmosphere.tools";

    public static void postToken(final VolleyCallback volleyCallback, Context context){
        String url= my_URL+"/v1/login";

        CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.POST,
                url, null, new Response.Listener() {

            @Override
            public void onResponse(Object responseObj) {
                String response = (String) responseObj;
                if (!response.equals(null)) {

                    Log.e("Your Array Response", response);
                } else {
                    Log.e("Your Array Response", "Data Null");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error is ", "" + error);
            }
        }) {

            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            //Pass Your Parameters here
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", "parking-username");
                params.put("password", "parking-password");
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(rq);

    }



    // Custom JSON Request Handler
    public static void makeRequest(final String url, final VolleyCallback callback, final Context context) {

        postToken(new VolleyCallback() {

            @Override
            public void onSuccess(final JSONObject result) throws JSONException {
                CustomJSONObjectRequest rq = new CustomJSONObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener() {
                            @Override
                            public void onResponse(Object responseObj) {
                                JSONObject response = (JSONObject) responseObj;
                                Log.v("Response", response.toString());
                                try {
                                    String ip = response.getString("ip");
                                    if (ip != "null") {
                                        callback.onSuccess(response);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        //Pass response to success callback

                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {}
                        }) {

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Authorization", result.toString());
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        return params;
                    }

                };

                // Request added to the RequestQueue
                VolleyController.getInstance(context).addToRequestQueue(rq);
            }

            @Override
            public void onError(String result) throws Exception {

            }
        }, context);



    }


}
