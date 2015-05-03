package cl.healpy.async;


import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.healpy.config.DataHealpy;
import cl.healpy.models.CommuneDBHelper;
import cl.healpy.R;
import cl.healpy.config.ConfigHealpy;

public class LoadCommunes extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String token = new String();
    private DataHealpy data_healpy = new DataHealpy();
    private ConfigHealpy configHealpy = new ConfigHealpy();

    public LoadCommunes(Context context){
        super();
        this.mContext = context;
        this.token = data_healpy.getTOKEN();
    }

    @Override
    protected String doInBackground(String... urls) {
        String response;
        try {

            String url = urls[0];
            response = HttpRequest.get(url)
                    .header("Authorization", token)
                    .accept("application/json").body();

        } catch (HttpRequest.HttpRequestException exception) {
            response = null;
        }
        return response;
    }

    protected void onProgressUpdate(Integer... progress) {}

    protected void onPostExecute(String result) {

        CommuneDBHelper commune_db = new CommuneDBHelper(mContext);

        if (result != null){

            try{
                commune_db.deleteAllCommune();
                JSONArray commune_json = new JSONArray(configHealpy.prettyfyJSON(result));

                for (int i = 0; i < commune_json.length(); i++){
                    JSONObject commune = commune_json.getJSONObject(i);

                    String name = commune.getString("name");
                    String url = commune.getString("url");
                    String fk_commune = commune.getString("fk_commune");
                    String fk_region = commune.getString("fk_region");
                    String region = commune.getString("region");

                    commune_db.createCommune(name, url, fk_commune, fk_region, region);
                }

            }catch (JSONException e) {}

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.conection_error), Toast.LENGTH_SHORT).show();
        }

    }
}
