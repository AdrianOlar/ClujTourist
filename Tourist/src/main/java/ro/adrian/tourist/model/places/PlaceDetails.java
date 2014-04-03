package ro.adrian.tourist.model.places;

import com.google.api.client.util.Key;

import java.io.Serializable;

/**
 * Created by Adrian-PC on 3/31/14.
 * Licence thesis project
 */
public class PlaceDetails implements Serializable {
    @Key
    public String status;
    @Key
    public Place result;

    @Override
    public String toString() {
        if (result != null) {
            return result.toString();
        }
        return super.toString();
    }
}
