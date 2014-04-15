package ro.adrian.tourist.provider;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by Adrian Olar on 15/04/2014.
 * Licence Thesis Project
 */
public class ClujTouristProvider extends ContentProvider {
    private static final UriMatcher mUriMatcher = buildUriMatcher();
    private static final int PLACE = 100;
    private static final int PLACE_ID = 101;

    private ClujTouristDatabase mDatabaseHelper;

    /**
     * Build and return a {@link android.content.UriMatcher} that catches all {@Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ClujTouristContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, ClujTouristContract.PATH_PLACE, PLACE);
        matcher.addURI(authority, ClujTouristContract.PATH_PLACE + "/*", PLACE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mDatabaseHelper = new ClujTouristDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase database = mDatabaseHelper.getReadableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        return builder.where(selection, selectionArgs).query(database, projection,
                sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PLACE:
                return ClujTouristContract.Place.CONTENT_TYPE;

            case PLACE_ID:
                return ClujTouristContract.Place.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case PLACE:
                insertOrUpdateById(db, uri, ClujTouristDatabase.Tables.PLACE, contentValues, ClujTouristContract.Place.NAME);
                getContext().getContentResolver().notifyChange(uri, null, false);
                return ClujTouristContract.Place.buildPlaceUri(contentValues.getAsString(ClujTouristContract.Place.NAME));

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (uri == ClujTouristContract.BASE_CONTENT_URI) {
            // Handle whole database deletes (e.g. when signing out)
            deleteDatabase();
            getContext().getContentResolver().notifyChange(uri, null, false);

            return 1;
        }

        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).delete(db);

        getContext().getContentResolver().notifyChange(uri, null, false);

        return retVal;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        int retVal = builder.where(selection, selectionArgs).update(db, values);

        getContext().getContentResolver().notifyChange(uri, null, false);

        return retVal;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations) throws OperationApplicationException {
        final SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();

        // Begin the transaction
        db.beginTransaction();

        // Apply the operations
        try {
            final int numOperations = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[numOperations];

            for (int i = 0; i < numOperations; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }

            db.setTransactionSuccessful();

            return results;
        } finally {
            db.endTransaction();
        }
    }


    /**
     * Deletes the current database instance.
     */
    private void deleteDatabase() {
        // Close current handler
        mDatabaseHelper.close();

        // Delete database
        Context context = getContext();
        ClujTouristDatabase.deleteDatabase(context);

        // Recreate database
        mDatabaseHelper = new ClujTouristDatabase(getContext());
    }

    /**
     * In case of a conflict when inserting the values, another update query is sent.
     *
     * @param db     Database to insert to.
     * @param uri    Content provider uri.
     * @param table  Table to insert to.
     * @param values The values to insert to.
     * @param column Column to identify the object.
     * @throws android.database.SQLException
     */
    private void insertOrUpdateById(SQLiteDatabase db, Uri uri, String table,
                                    ContentValues values, String column) throws SQLException {
        try {
            db.insertOrThrow(table, null, values);
        } catch (SQLiteConstraintException e) {
            int nrRows = update(uri, values, column + "=?", new String[]{values.getAsString(column)});
            if (nrRows == 0)
                throw e;
        }
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = mUriMatcher.match(uri);

        switch (match) {
            case PLACE:
                return builder.table(ClujTouristDatabase.Tables.PLACE);

            case PLACE_ID:
                final String storeId = ClujTouristContract.Place.getPlaceId(uri);
                return builder.table(ClujTouristDatabase.Tables.PLACE).where(ClujTouristContract.Place.NAME + "=?", storeId);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
