package ro.adrian.tourist.condesales.listeners;

import java.util.ArrayList;

import ro.adrian.tourist.condesales.models.Venues;

public interface VenuesHistoryListener extends ErrorListener {

	public void onGotVenuesHistory(ArrayList<Venues> list);

}
