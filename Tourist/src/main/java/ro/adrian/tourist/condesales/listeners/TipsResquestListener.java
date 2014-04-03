package ro.adrian.tourist.condesales.listeners;

import java.util.ArrayList;

import ro.adrian.tourist.condesales.models.Tip;

public interface TipsResquestListener extends ErrorListener {
	
	public void onTipsFetched(ArrayList<Tip> tips);
	
}
