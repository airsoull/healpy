package cl.healpy.models;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RegionDBHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME = "region";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "region.db";
    private SQLiteDatabase db;

    private static final String KEY_REGION = "region";
    private static final String KEY_FK_REGION = "fk_region";
    private static final String KEY_URL = "url";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE "+ TABLE_NAME +" (" +
                    "_id INTEGER PRIMARY KEY, " +
                    KEY_REGION +" TEXT," +
                    KEY_FK_REGION +" INTEGER," +
                    KEY_URL +" TEXT" +
                    ");";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;


    public RegionDBHelper(Context context) {
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

    public long createRegion(String region, int fk_region, String url){

        ContentValues values = new ContentValues();

        values.put(KEY_REGION, region);
        values.put(KEY_FK_REGION, fk_region);
        values.put(KEY_URL, url);

        return db.insert(TABLE_NAME, null, values);
    }

    public boolean deleteAllRegion(){
        int doneDelete;
        doneDelete = db.delete(TABLE_NAME, null, null);
        return doneDelete > 0;
    }

    public Cursor getAllRegions(){
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public int getRegionsCount(){
        Cursor cursor = getAllRegions();
        return cursor.getCount();
    }
}
