package ro.adrian.tourist.condesales.listeners;

import java.util.ArrayList;

import ro.adrian.tourist.condesales.models.Venue;

public interface FoursquareTrendingVenuesResquestListener extends ErrorListener {
	
	public void onTrendedVenuesFetched(ArrayList<Venue> venues);
	
}
