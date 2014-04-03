package ro.adrian.tourist.utils;

/**
 * Created by Adrian-PC on 3/29/14.
 * Licence thesis project
 */
public class Constants {
    /*Location constants*/
    public static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    // Milliseconds per second
    public static final int MILLISECONDS_PER_SECOND = 1000;
    // Update frequency in milliseconds
    public static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    // The fastest update frequency, in seconds
    public static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    // A fast frequency ceiling in milliseconds
    public static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;
    public static final String PREFS_NAME = "ClujTouristPreferences";
    public static final String UPDATES_ON = "KEY_UPDATES_ON";
    public static final String KEY_LONG = "Longitude";
    public static final String KEY_LAT = "Latitude";
    public static final String GOOGLE_PLACES_API_KEY = "AIzaSyCT5nkEQGS16O32SoDuSTNcsLCW5bEczAM";
}
