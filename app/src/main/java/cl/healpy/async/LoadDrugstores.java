package cl.healpy.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cl.healpy.config.ConfigHealpy;
import cl.healpy.config.DataHealpy;
import cl.healpy.R;
import cl.healpy.models.DrugstoreDBHelper;


public class LoadDrugstores extends AsyncTask<String, Void, String> {

    private Context mContext;
    private String token = new String();
    private DataHealpy data_healpy = new DataHealpy();
    private ConfigHealpy configHealpy = new ConfigHealpy();

    public LoadDrugstores(Context context){
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

        DrugstoreDBHelper drugstore_db = new DrugstoreDBHelper(mContext);

        if (result != null){

            try{
                drugstore_db.deleteAllDrugstore();
                JSONArray drugstore_json = new JSONArray(configHealpy.prettyfyJSON(result));

                for (int i = 0; i < drugstore_json.length(); i++){
                    JSONObject drugstore = drugstore_json.getJSONObject(i);

                    String url = drugstore.getString("url");
                    String name = drugstore.getString("name");
                    String date = drugstore.getString("date");
                    String fk_commune = drugstore.getString("fk_commune");
                    String commune = drugstore.getString("commune");
                    int fk_region = drugstore.getInt("fk_region");
                    String region = drugstore.getString("region");
                    String operating_day = drugstore.getString("operating_day");
                    String opening_time = drugstore.getString("opening_time");
                    String closing_time = drugstore.getString("closing_time");
                    String address = drugstore.getString("address");
                    String local_id = drugstore.getString("local_id");
                    String latitude = drugstore.getString("latitud");
                    String longitude = drugstore.getString("longitude");
                    String phone = drugstore.getString("phone");
                    String town_name = drugstore.getString("town_name");
                    Boolean all_night = drugstore.getBoolean("all_night");

                    drugstore_db.createDrugstore(name, url, date, fk_commune, commune,
                            fk_region, region, operating_day, opening_time, closing_time,
                            address, local_id, latitude, longitude, phone, town_name, all_night);
                }

            }catch (JSONException e) {
                Log.i("process exception", e.toString());
            }

        } else {
            Toast.makeText(mContext, mContext.getString(R.string.conection_error), Toast.LENGTH_SHORT).show();
        }

    }
}
