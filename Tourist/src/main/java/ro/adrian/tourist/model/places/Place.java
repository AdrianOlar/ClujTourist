package ro.adrian.tourist.model.places;

import com.google.api.client.util.Key;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Adrian-PC on 3/31/14.
 * Licence thesis project
 */
public class Place implements Serializable {
    @Key
    public String id;
    @Key
    public String name;
    @Key
    public String reference;
    @Key
    public String icon;
    @Key
    public String vicinity;
    @Key
    public Geometry geometry;
    @Key
    public String formatted_address;
    @Key
    public String formatted_phone_number;
    @Key
    public List<Review> reviews;

    @Override
    public String toString() {
        return name + " - " + id + " - " + reference;
    }

    public static class Geometry implements Serializable {
        @Key
        public Location location;
    }

    public static class Location implements Serializable {
        @Key
        public double lat;
        @Key
        public double lng;
    }

    public static class Review implements Serializable {
        @Key
        public String language;
        @Key
        public double rating;
        @Key
        public String text;
        @Key
        public long time;
    }
}
