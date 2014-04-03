package ro.adrian.tourist.condesales.listeners;

import ro.adrian.tourist.condesales.models.Venue;

public interface FoursquareVenueDetailsResquestListener extends ErrorListener {
	
	public void onVenueDetailFetched(Venue venues);
	
}
