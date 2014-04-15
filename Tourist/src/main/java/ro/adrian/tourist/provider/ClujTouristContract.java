package ro.adrian.tourist.provider;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Adrian Olar on 15/04/2014.
 * Licence Thesis Project
 */
public class ClujTouristContract {
    public static final String CONTENT_AUTHORITY = "ro.adrian.tourist";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    protected static final String PATH_PLACE = "place";

    interface PlaceColumns {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String NAME = "placeName";
    }

    public static class Place implements PlaceColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLACE).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.tourist.place";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.tourist.place";
        public static final String DEFAULT_SORT = ClujTouristDatabase.Tables.PLACE + "." + BaseColumns._ID + " ASC";

        public static final String getPlaceId(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildPlaceUri(String storeId) {
            return CONTENT_URI.buildUpon().appendPath(storeId).build();
        }
    }
}
