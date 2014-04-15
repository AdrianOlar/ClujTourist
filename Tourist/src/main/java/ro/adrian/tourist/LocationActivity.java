package ro.adrian.tourist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import ro.adrian.tourist.asynctasks.GetAddressTask;
import ro.adrian.tourist.utils.Constants;

/**
 * Created by Adrian-PC on 3/29/14.
 * Licence thesis project
 */
public class LocationActivity extends FragmentActivity implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    private LocationClient mLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private int errorCode;
    private boolean mUpdatesRequested;
    private SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL);

        // Open the shared preferences
        mPrefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        // Get a SharedPreferences editor
        mEditor = mPrefs.edit();

        mLocationClient = new LocationClient(this, this, this);
        setBtnClicks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationClient.connect();
    }

    @Override
    protected void onStop() {
        // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            mLocationClient.removeLocationUpdates(this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
        // Save the current setting for updates
        mEditor.putBoolean(Constants.UPDATES_ON, mUpdatesRequested);
        mEditor.commit();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
         * Get any previous setting for location updates
         * Gets "false" if an error occurs
         */
        if (mPrefs.contains(Constants.UPDATES_ON)) {
            mUpdatesRequested = mPrefs.getBoolean(Constants.UPDATES_ON, false);
            // Otherwise, turn off location updates
        } else {
            mEditor.putBoolean(Constants.UPDATES_ON, false);
            mEditor.commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                }
        }
    }

    private boolean servicesConnected() {
        errorCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return errorCode == ConnectionResult.SUCCESS;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(LocationActivity.class.getCanonicalName(), "Connected");
        // If already requested, start periodic updates
        if (mUpdatesRequested) {
            mLocationClient.requestLocationUpdates(mLocationRequest, this);
        }
        mCurrentLocation = mLocationClient.getLastLocation();
    }

    @Override
    public void onDisconnected() {
        Log.d(LocationActivity.class.getCanonicalName(), "Disconnected, please re-connect");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        Constants.CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }

    private void showCurrentLocation() {
        if (!servicesConnected()) {
            GooglePlayServicesUtil.getErrorDialog(errorCode, this, 0).show();
        } else {
            Log.d(LocationActivity.class.getCanonicalName(), "This is your current location: " + mCurrentLocation.toString());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Report to the UI that the location was updated
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setBtnClicks() {
        findViewById(R.id.location_upd_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditor.putBoolean(Constants.UPDATES_ON, !mUpdatesRequested);
            }
        });
        findViewById(R.id.show_location_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mCurrentLocation = mLocationClient.getLastLocation();
                showCurrentLocation();
            }
        });
        findViewById(R.id.get_address_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new GetAddressTask(LocationActivity.this)).execute(mCurrentLocation);
                //OntologyTest.createAndShowOntology(LocationActivity.this);
            }
        });
        findViewById(R.id.show_on_map_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(LocationActivity.this, MapActivity.class);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mapIntent.putExtra(Constants.KEY_LAT, mCurrentLocation.getLatitude());
                mapIntent.putExtra(Constants.KEY_LONG, mCurrentLocation.getLongitude());
                startActivity(mapIntent);
            }
        });
        findViewById(R.id.places_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(LocationActivity.this, PlacesActivity.class);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mapIntent.putExtra(Constants.KEY_LAT, mCurrentLocation.getLatitude());
                mapIntent.putExtra(Constants.KEY_LONG, mCurrentLocation.getLongitude());
                startActivity(mapIntent);
            }
        });
        findViewById(R.id.gplaces_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(LocationActivity.this, GPlacesActivity.class);
                mapIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                mapIntent.putExtra(Constants.KEY_LAT, mCurrentLocation.getLatitude());
                mapIntent.putExtra(Constants.KEY_LONG, mCurrentLocation.getLongitude());
                startActivity(mapIntent);
            }
        });
    }
}
