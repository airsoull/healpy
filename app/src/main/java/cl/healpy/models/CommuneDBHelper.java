package cl.healpy.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import cl.healpy.config.SpinnerObject;


public class CommuneDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "commune";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "commune.db";
    private SQLiteDatabase db;

    private static final String KEY_URL = "url";
    private static final String KEY_NAME = "name";
    private static final String KEY_FK_COMMUNE = "fk_commune";
    private static final String KEY_FK_REGION = "fk_region";
    private static final String KEY_REGION = "region";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ TABLE_NAME +" (" +
                    "_id INTEGER PRIMARY KEY, " +
                    KEY_URL +" TEXT," +
                    KEY_NAME +" TEXT," +
                    KEY_FK_COMMUNE +" TEXT," +
                    KEY_FK_REGION +" TEXT," +
                    KEY_REGION +" TEXT" +
                    ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public CommuneDBHelper(Context context) {
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

    public long createCommune(String name, String url, String fk_commune, String fk_region, String region){

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_URL, url);
        values.put(KEY_FK_COMMUNE, fk_commune);
        values.put(KEY_FK_REGION, fk_region);
        values.put(KEY_REGION, region);

        return db.insert(TABLE_NAME, null, values);
    }

    public Cursor getAllCommune(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getCommuneByRegion(String fk_region){
        String[] args = new String[] {fk_region};
        return db.query(TABLE_NAME, null, KEY_FK_REGION + "=?", args, null, null, KEY_NAME);
    }

    public boolean deleteAllCommune(){
        int doneDelete;
        doneDelete = db.delete(TABLE_NAME, null, null);
        return doneDelete > 0;
    }

    public int getCommuneCount(){
        Cursor cursor = getAllCommune();
        return cursor.getCount();
    }

    public List <SpinnerObject> getCommuneByRegionArray(String fk_region){
        List <SpinnerObject> communes = new ArrayList <SpinnerObject>();
        Cursor communes_cursor = getCommuneByRegion(fk_region);

        if ( communes_cursor.moveToFirst () ) {
            do {
                String fk_commune = communes_cursor.getString(communes_cursor.getColumnIndex("fk_commune"));
                String name = communes_cursor.getString(communes_cursor.getColumnIndex("name"));
                communes.add(new SpinnerObject(fk_commune, name));
            } while (communes_cursor.moveToNext());
        }

        return communes;
    }
}
