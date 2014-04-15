package ro.adrian.tourist.provider.queries;

import android.provider.BaseColumns;

import ro.adrian.tourist.provider.ClujTouristContract;
import ro.adrian.tourist.provider.ClujTouristDatabase;

/**
 * Created by Adrian Olar on 15/04/2014.
 * Licence Thesis Project
 */
public interface QueryPlace {

    String[] PROJECTION_SIMPLE = {
            ClujTouristDatabase.Tables.PLACE + "." + BaseColumns._ID,
            ClujTouristDatabase.Tables.PLACE + "." + ClujTouristContract.Place.LATITUDE,
            ClujTouristDatabase.Tables.PLACE + "." + ClujTouristContract.Place.LONGITUDE,
            ClujTouristDatabase.Tables.PLACE + "." + ClujTouristContract.Place.NAME,
    };

    int _ID = 0;
    int LATITUDE = _ID + 1;
    int LONGITUDE = LATITUDE + 1;
    int NAME = LONGITUDE + 1;
}
