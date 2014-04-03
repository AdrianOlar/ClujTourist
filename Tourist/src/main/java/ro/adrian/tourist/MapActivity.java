package ro.adrian.tourist;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterManager;

import ro.adrian.tourist.custom.views.PlaceItem;
import ro.adrian.tourist.utils.Constants;

/**
 * Created by Adrian-PC on 3/29/14.
 * Licence thesis project
 */
public class MapActivity extends FragmentActivity {

    private GoogleMap map;
    private ClusterManager<PlaceItem> mClusterManager;
    private double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        latitude = getIntent().getDoubleExtra(Constants.KEY_LAT, 0);
        longitude = getIntent().getDoubleExtra(Constants.KEY_LONG, 0);
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        setUpClusterer();
    }

    private void setUpClusterer() {

        // Position the map.
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<PlaceItem>(this, map);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        map.setOnCameraChangeListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {

        // Set some lat/lng coordinates to start with.
        double lat = latitude;
        double lng = longitude;

        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < 10; i++) {
            double offset = i / 60d;
            lat = lat + offset;
            lng = lng + offset;
            PlaceItem offsetItem = new PlaceItem(lat, lng);
            mClusterManager.addItem(offsetItem);
        }

    }
}