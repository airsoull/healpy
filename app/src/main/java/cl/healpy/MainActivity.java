package cl.healpy;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.heinrichreimersoftware.materialdrawer.DrawerActivity;

import cl.healpy.async.LoadDrugstores;
import cl.healpy.config.ConfigHealpy;
import cl.healpy.config.DataHealpy;
import cl.healpy.models.DrugstoreDBHelper;


public class MainActivity extends DrawerActivity implements OnMapReadyCallback {

    private ConfigHealpy config_healpy = new ConfigHealpy(this);
    private DataHealpy data_healpy = new DataHealpy();
    private DrugstoreDBHelper drugstoreDBHelper;
    private GoogleMap google_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drugstoreDBHelper = new DrugstoreDBHelper(getApplicationContext());

        config_healpy.setToolbar(getString(R.string.app_name));
        config_healpy.setDrawer();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getAllDrugstores();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        google_map = googleMap;

        googleMap.setMyLocationEnabled(true);
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location!=null){
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng myPosition = new LatLng(latitude, longitude);

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myPosition, 13));
            googleMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .position(myPosition));
        }
    }

    private void getAllDrugstores(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getString(R.string.load_drugstores));
        progressDialog.setCancelable(true);
        progressDialog.show();

        new LoadDrugstores(getApplicationContext()){
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                progressDialog.dismiss();
                showDrugstoreInMap();
            }
        }.execute(data_healpy.getURL_DRUGSTORE());
    }

    private void showDrugstoreInMap(){
        Cursor drugstores = drugstoreDBHelper.getAllDrugstore();

        if(drugstores.moveToFirst()){
            do {

                String latitude = drugstores
                        .getString(drugstores.getColumnIndexOrThrow("latitude"));
                String longitude = drugstores
                        .getString(drugstores.getColumnIndexOrThrow("longitude"));

                if((!latitude.isEmpty()) && (!longitude.isEmpty())){

                    String name = drugstores.getString(drugstores.getColumnIndexOrThrow("name"));
                    String address = drugstores.getString(drugstores.getColumnIndexOrThrow("address"));

                    double latitude_double = Double.parseDouble(latitude);
                    double longitude_double = Double.parseDouble(longitude);

                    LatLng drugstore_position = new LatLng(latitude_double, longitude_double);
                    google_map.addMarker(new MarkerOptions()
                            .title(name)
                            .snippet(address)
                            .icon(BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                            .position(drugstore_position));
                }


            }while(drugstores.moveToNext());
        }
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
                simpleDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void simpleDialog(){
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
