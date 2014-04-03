package ro.adrian.tourist.model.places;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Adrian-PC on 3/31/14.
 * Licence thesis project
 */
public class PlaceList implements Serializable {
    @Key
    public String status;
    @Key
    public List<Place> results;
}
