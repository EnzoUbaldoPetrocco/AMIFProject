package Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CreazioneJson {


    //Versione alternativa per la creazione di oggetti Json
    //Nel pimo array vanno scritti i nomi dei campi da creare per il

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

    public static Map<String, String> createJson(String[] nomi, String...strings)
    {
        Map<String, String> post= new HashMap<>();
        for (int i=0; i<strings.length; i++) {
            post.put(nomi[i], strings[i]);
        }
        return  post;
    }

    public static JSONObject createJSONObject(String[] nomi, Object...objects) throws JSONException {
        JSONObject post= new JSONObject();
        for (int i=0; i<objects.length; i++) {
            post.put(nomi[i], objects[i]);
        }
        return  post;
    }

    class NestedMap<String, V> {

        private final HashMap<String, NestedMap> child;
        private V value;

        public NestedMap() {
            child = new HashMap<>();
            value = null;
        }

        public boolean hasChild(String k) {
            return this.child.containsKey(k);
        }

        public NestedMap<String, V> getChild(String k) {
            return this.child.get(k);
        }

        public void makeChild(String k) {
            this.child.put(k, new NestedMap());
        }

        public V getValue() {
            return value;
        }

        public void setValue(V v) {
            value = v;
        }
    }


}

