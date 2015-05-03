package cl.healpy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.heinrichreimersoftware.materialdrawer.DrawerActivity;

import java.util.ArrayList;
import java.util.List;

import cl.healpy.adapters.DrugstoreAdapater;
import cl.healpy.async.LoadCommunes;
import cl.healpy.async.LoadDrugstores;
import cl.healpy.config.ConfigHealpy;
import cl.healpy.config.DataHealpy;
import cl.healpy.config.SpinnerObject;
import cl.healpy.models.CommuneDBHelper;
import cl.healpy.models.DrugstoreDBHelper;


public class ComunaActivity extends DrawerActivity {

    private ConfigHealpy config_healpy = new ConfigHealpy(this);
    private DataHealpy data_healpy = new DataHealpy();
    private Spinner spinner_regions;
    private CommuneDBHelper communeDBHelper;
    private DrugstoreDBHelper drugstoreDBHelper;
    private Spinner spinner_commune;
    private ListView drugstore_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comuna);

        communeDBHelper = new CommuneDBHelper(getApplicationContext());
        drugstoreDBHelper = new DrugstoreDBHelper(getApplicationContext());
        drugstore_list = (ListView) findViewById(R.id.listView_drugstore);


        config_healpy.setToolbar(getString(R.string.comuna));
        config_healpy.setDrawer();

        if (communeDBHelper.getCommuneCount() <= 0){
            getAllCommuneAPI();
        }

        loadRegions();
        loadCommunes();
        spinnerRegionSelected();
        drugstoreShare();
    }

    private void drugstoreShare(){
        drugstore_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) drugstore_list.getItemAtPosition(position);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                String opening_time = cursor.getString(cursor.getColumnIndexOrThrow("opening_time"));
                String closing_time = cursor.getString(cursor.getColumnIndexOrThrow("closing_time"));
                String town_name = cursor.getString(cursor.getColumnIndexOrThrow("town_name"));

                String send_text =
                        getString(R.string.drugstore).concat(": ").concat(name) + "\n" +
                                getString(R.string.address).concat(": ").concat(address) + "\n" +
                                getString(R.string.phone).concat(": ").concat(phone) + "\n" +
                                getString(R.string.schedule).concat(" ").concat(opening_time).concat(getString(R.string.to)).concat(" ").concat(closing_time) + "\n" +
                                town_name +
                                "";

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, send_text);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
    }

    private void spinnerRegionSelected(){
        spinner_regions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadCommunes();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void getAllCommuneAPI(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.load_communes));
        progressDialog.setCancelable(false);
        progressDialog.show();

        new LoadCommunes(getApplicationContext()){
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                loadCommunes();
            }
        }.execute(data_healpy.getURL_COMMUNE());
    }

    private void loadRegions(){
        spinner_regions = (Spinner) findViewById(R.id.spinner_regions);
        List<String> list = new ArrayList<String>();
        String[] regions = data_healpy.getRegions();

        for(int indice = 0; indice < regions.length ; indice++){
            list.add(regions[indice]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

        spinner_regions.setAdapter(dataAdapter);
    }

    public void loadCommunes(){

        long fk_region = spinner_regions.getSelectedItemId();
        String fk_region_string = String.valueOf(fk_region + 1);
        spinner_commune = (Spinner) findViewById(R.id.spinner_commune);

        List <SpinnerObject> communes_array = communeDBHelper.getCommuneByRegionArray(fk_region_string);
        ArrayAdapter<SpinnerObject> dataAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_item, communes_array);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner_commune.setAdapter(dataAdapter);
    }

    public void getDrugstores(View view){
        String fk_region = String.valueOf(spinner_regions.getSelectedItemId() + 1);
        String fk_commune = ((SpinnerObject) spinner_commune.getSelectedItem()).getId();

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.load_drugstores));
        progressDialog.setCancelable(true);
        progressDialog.show();

        new LoadDrugstores(getApplicationContext()){
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                showDrugstoreList();
            }
        }.execute(data_healpy.getDrugstoreByRegionAndCommune(fk_region, fk_commune));
    }

    private void showDrugstoreList(){
        final Cursor drugstores_cursor = drugstoreDBHelper.getAllDrugstore();
        drugstore_list = (ListView) findViewById(R.id.listView_drugstore);

        DrugstoreAdapater drugstoreAdapater = new DrugstoreAdapater(getApplicationContext(), drugstores_cursor);
        drugstore_list.setAdapter(drugstoreAdapater);
        if (drugstoreDBHelper.getDrugstoreCount() <= 0) { simpleDialog(); }
    }

    private void simpleDialog(){
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.not_found_drugstore))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.info:
                simpleDialogInfo();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void simpleDialogInfo(){
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_info_grey600_36dp)
                .setTitle(getString(R.string.info))
                .setMessage(getString(R.string.info_complete))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
}
