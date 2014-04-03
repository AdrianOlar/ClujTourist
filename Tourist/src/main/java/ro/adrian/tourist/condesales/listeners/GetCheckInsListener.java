package ro.adrian.tourist.condesales.listeners;

import java.util.ArrayList;

import ro.adrian.tourist.condesales.models.Checkin;



public interface GetCheckInsListener extends ErrorListener {

	public void onGotCheckIns(ArrayList<Checkin> list);
	
}
