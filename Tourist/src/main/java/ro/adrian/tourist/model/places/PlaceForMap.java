package ro.adrian.tourist.model.places;

/**
 * Created by Adrian Olar on 15/04/2014.
 * Licence Thesis Project
 */
public class PlaceForMap {

    private double latitude;
    private double longitude;
    private String name;

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
