package cl.healpy.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DrugstoreDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "drugstore";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "drugstore.db";
    private SQLiteDatabase db;

    private static final String KEY_URL= "url";
    private static final String KEY_NAME = "name";
    private static final String KEY_DATE = "date";
    private static final String KEY_FK_COMMUNE = "fk_commune";
    private static final String KEY_COMMUNE = "commune";
    private static final String KEY_FK_REGION = "fk_region";
    private static final String KEY_REGION = "region";
    private static final String KEY_OPERATING_DAY = "operating_day";
    private static final String KEY_OPENING_TIME = "opening_time";
    private static final String KEY_CLOSING_TIME = "closing_time";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_LOCAL_ID = "local_id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_TOWN_NAME = "town_name";
    private static final String KEY_ALL_NIGHT = "all_night";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ TABLE_NAME +" (" +
                    "_id INTEGER PRIMARY KEY, " +
                    KEY_NAME + " TEXT," +
                    KEY_URL +" TEXT," +
                    KEY_DATE +" TEXT," +
                    KEY_FK_COMMUNE +" TEXT," +
                    KEY_COMMUNE +" TEXT," +
                    KEY_FK_REGION +" INTEGER," +
                    KEY_REGION +" TEXT," +
                    KEY_OPERATING_DAY +" TEXT," +
                    KEY_OPENING_TIME +" TEXT," +
                    KEY_CLOSING_TIME +" TEXT," +
                    KEY_ADDRESS +" TEXT," +
                    KEY_LOCAL_ID +" TEXT," +
                    KEY_LATITUDE +" TEXT," +
                    KEY_LONGITUDE +" TEXT," +
                    KEY_PHONE +" TEXT," +
                    KEY_TOWN_NAME +" TEXT," +
                    KEY_ALL_NIGHT + " INTEGER" +
                    ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public DrugstoreDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public long createDrugstore(String name, String url, String date, String fk_commune, String commune,
                                int fk_region, String region, String operating_day, String opening_time,
                                String closing_time, String address, String local_id, String latitude,
                                String longitude, String phone, String town_name, Boolean all_night){

        ContentValues values = new ContentValues();

        int is_all_night = (all_night)? 1 : 0;

        values.put(KEY_NAME, name);
        values.put(KEY_URL, url);
        values.put(KEY_DATE, date);
        values.put(KEY_FK_COMMUNE, fk_commune);
        values.put(KEY_COMMUNE, commune);
        values.put(KEY_FK_REGION, fk_region);
        values.put(KEY_REGION, region);
        values.put(KEY_OPERATING_DAY, operating_day);
        values.put(KEY_OPENING_TIME, opening_time);
        values.put(KEY_CLOSING_TIME, closing_time);
        values.put(KEY_ADDRESS, address);
        values.put(KEY_LOCAL_ID, local_id);
        values.put(KEY_LATITUDE, latitude);
        values.put(KEY_LONGITUDE, longitude);
        values.put(KEY_PHONE, phone);
        values.put(KEY_TOWN_NAME, town_name);
        values.put(KEY_ALL_NIGHT, is_all_night);

        return db.insert(TABLE_NAME, null, values);
    }

    public boolean deleteAllDrugstore(){
        int doneDelete;
        doneDelete = db.delete(TABLE_NAME, null, null);
        return doneDelete > 0;
    }

    public Cursor getAllDrugstore(){
        String order_by = KEY_NAME + " ASC, " + KEY_ALL_NIGHT;
        return db.query(TABLE_NAME, null, null, null, null, null, order_by);
    }

    public int getDrugstoreCount(){
        Cursor cursor = getAllDrugstore();
        return cursor.getCount();
    }
}
