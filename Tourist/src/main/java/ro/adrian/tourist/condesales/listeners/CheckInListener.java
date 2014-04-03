package ro.adrian.tourist.condesales.listeners;

import ro.adrian.tourist.condesales.models.Checkin;



public interface CheckInListener extends ErrorListener {

	public void onCheckInDone(Checkin checkin);
	
}
