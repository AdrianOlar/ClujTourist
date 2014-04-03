package ro.adrian.tourist.custom.views;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Adrian-PC on 3/29/14.
 * Licence thesis project
 */
public class PlaceItem implements ClusterItem {

    private final LatLng mPosition;

    public PlaceItem(double lat, double lng) {
        this.mPosition = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
