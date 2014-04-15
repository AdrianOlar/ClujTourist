package ro.adrian.tourist.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Adrian Olar on 15/04/2014.
 * Licence Thesis Project
 */
public class ClujTouristDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "clujtourist.db";
    private static final int DATABASE_VERSION = 1;

    public ClujTouristDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.PLACE + "(" +
                        ClujTouristContract.Place._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ClujTouristContract.Place.LATITUDE + " DOUBLE DEFAULT 0, " +
                        ClujTouristContract.Place.LONGITUDE + " DOUBLE DEFAULT 0, " +
                        ClujTouristContract.Place.NAME + " TEXT, " +
                        "UNIQUE (" + ClujTouristContract.Place.NAME + "))"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Tables.PLACE);
    }

    public interface Tables {
        String PLACE = "place";
    }
}
