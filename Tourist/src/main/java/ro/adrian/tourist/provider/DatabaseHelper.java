package ro.adrian.tourist.provider;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ro.adrian.tourist.model.places.PlaceForMap;
import ro.adrian.tourist.provider.queries.QueryPlace;
import ro.adrian.tourist.utils.Constants;

/**
 * Created by Adrian Olar on 15/04/2014.
 * Licence Thesis Project
 */
public class DatabaseHelper {
    private static DatabaseHelper INSTANCE;


    private DatabaseHelper() {
    }

    public static DatabaseHelper getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DatabaseHelper();
        }
        return INSTANCE;
    }

    private void applyBatch(ArrayList<ContentProviderOperation> batch) {
        ContentResolver resolver = Constants.getContext().getContentResolver();
        try {
            resolver.applyBatch(ClujTouristContract.CONTENT_AUTHORITY, batch);
        } catch (RemoteException e) {
            Log.e("UpdateFavorite", "Exception", e);
        } catch (OperationApplicationException e) {
            Log.e("UpdateFavorite", "Exception", e);
        }
    }

    public boolean trySavePlaces(Collection<PlaceForMap> places) {
        List<PlaceForMap> placesToUpdate = new ArrayList<PlaceForMap>();
        ContentResolver resolver = Constants.getContext().getContentResolver();
        boolean success = false;

        for (PlaceForMap place : places) {
            String placeName = place.getName();
            String[] args = new String[]{placeName};
            String selection = ClujTouristDatabase.Tables.PLACE + "." + ClujTouristContract.Place.NAME + " LIKE '%?%'";
            Cursor c = resolver.query(ClujTouristContract.Place.CONTENT_URI, QueryPlace.PROJECTION_SIMPLE, selection, args, ClujTouristContract.Place.DEFAULT_SORT);
            if (c != null) {
                if (c.getCount() == 0) {
                    placesToUpdate.add(place);
                }
                c.close();
            } else {
                placesToUpdate.add(place);
            }
        }

        if (!placesToUpdate.isEmpty()) {
            Log.e("DBHelper", "updatePlaces " + placesToUpdate.size());
            success = true;
            ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

            for (PlaceForMap place : placesToUpdate) {
                String placeName = place.getName();
                ContentProviderOperation.Builder insertBuilder = ContentProviderOperation.newInsert(ClujTouristContract.Place.CONTENT_URI);
                insertBuilder.withValue(ClujTouristContract.Place.NAME, placeName);
                insertBuilder.withValue(ClujTouristContract.Place.LATITUDE, place.getLatitude());
                insertBuilder.withValue(ClujTouristContract.Place.LONGITUDE, place.getLongitude());

                batch.add(insertBuilder.build());
            }
            try {
                resolver.applyBatch(ClujTouristContract.CONTENT_AUTHORITY, batch);
            } catch (RemoteException e) {
                Log.e("UpdatePlace", "Exception", e);
                success = false;
            } catch (OperationApplicationException e) {
                Log.e("UpdatePlaces", "Exception", e);
                success = false;
            }
        }
        return success;
    }
}
