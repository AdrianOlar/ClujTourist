package ro.adrian.tourist;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.*;

import java.util.List;

import ro.adrian.tourist.model.places.Place;
import ro.adrian.tourist.model.places.PlaceList;
import ro.adrian.tourist.utils.AddItemizedOverlay;

/**
 * Created by Adrian-PC on 4/3/14.
 * Licence thesis project
 */
public class PlacesMapActivity extends com.google.android.maps.MapActivity {
    PlaceList nearPlaces;
    MapView mapView;

    List<Overlay> mapOverlays;
    AddItemizedOverlay itemizedOverlay;

    GeoPoint geoPoint;
    MapController mapController;

    double latitude, longitude;
    OverlayItem overlayItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_places);

        // Getting intent data
        Intent i = getIntent();

        // Users current geo location
        String user_latitude = i.getStringExtra("user_latitude");
        String user_longitude = i.getStringExtra("user_longitude");

        // Nearplaces list
        nearPlaces = (PlaceList) i.getSerializableExtra("near_places");

        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setBuiltInZoomControls(true);

        mapOverlays = mapView.getOverlays();

        // Geopoint to place on map
        geoPoint = new GeoPoint((int) (Double.parseDouble(user_latitude) * 1E6),
                (int) (Double.parseDouble(user_longitude) * 1E6));

        // Drawable marker icon
        Drawable drawable_user = this.getResources()
                .getDrawable(R.drawable.mark_red);

        itemizedOverlay = new AddItemizedOverlay(drawable_user, this);

        // Map overlay item
        overlayItem = new OverlayItem(geoPoint, "Your Location",
                "That is you!");

        itemizedOverlay.addOverlay(overlayItem);

        mapOverlays.add(itemizedOverlay);
        itemizedOverlay.populateNow();

        // Drawable marker icon
        Drawable drawable = this.getResources()
                .getDrawable(R.drawable.mark_blue);

        itemizedOverlay = new AddItemizedOverlay(drawable, this);

        mapController = mapView.getController();

        // These values are used to get map boundary area
        // The area where you can see all the markers on screen
        int minLat = Integer.MAX_VALUE;
        int minLong = Integer.MAX_VALUE;
        int maxLat = Integer.MIN_VALUE;
        int maxLong = Integer.MIN_VALUE;

        // check for null in case it is null
        if (nearPlaces.results != null) {
            // loop through all the places
            for (Place place : nearPlaces.results) {
                latitude = place.geometry.location.lat; // latitude
                longitude = place.geometry.location.lng; // longitude

                // Geopoint to place on map
                geoPoint = new GeoPoint((int) (latitude * 1E6),
                        (int) (longitude * 1E6));

                // Map overlay item
                overlayItem = new OverlayItem(geoPoint, place.name,
                        place.vicinity);

                itemizedOverlay.addOverlay(overlayItem);


                // calculating map boundary area
                minLat = (int) Math.min(geoPoint.getLatitudeE6(), minLat);
                minLong = (int) Math.min(geoPoint.getLongitudeE6(), minLong);
                maxLat = (int) Math.max(geoPoint.getLatitudeE6(), maxLat);
                maxLong = (int) Math.max(geoPoint.getLongitudeE6(), maxLong);
            }
            mapOverlays.add(itemizedOverlay);

            // showing all overlay items
            itemizedOverlay.populateNow();
        }

        // Adjusting the zoom level so that you can see all the markers on map
        mapView.getController().zoomToSpan(Math.abs(minLat - maxLat), Math.abs(minLong - maxLong));

        // Showing the center of the map
        mapController.animateTo(new GeoPoint((maxLat + minLat) / 2, (maxLong + minLong) / 2));
        mapView.postInvalidate();
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

}
