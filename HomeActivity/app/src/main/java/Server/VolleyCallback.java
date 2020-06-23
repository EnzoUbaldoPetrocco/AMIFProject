package Server;


import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface VolleyCallback {
    void onSuccess(JSONObject result) throws JSONException, IOException;
    void onError(VolleyError error) throws Exception;
}

