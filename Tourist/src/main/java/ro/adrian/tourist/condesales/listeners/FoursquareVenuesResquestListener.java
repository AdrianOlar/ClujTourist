package ro.adrian.tourist.condesales.listeners;

import java.util.ArrayList;

import ro.adrian.tourist.condesales.models.Venue;

public interface FoursquareVenuesResquestListener extends ErrorListener {
	
	public void onVenuesFetched(ArrayList<Venue> venues);
	
}
