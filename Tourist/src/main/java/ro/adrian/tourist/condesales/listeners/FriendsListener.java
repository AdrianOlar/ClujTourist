package ro.adrian.tourist.condesales.listeners;

import java.util.ArrayList;

import ro.adrian.tourist.condesales.models.User;

public interface FriendsListener extends ErrorListener {

	public void onGotFriends(ArrayList<User> list);
	
}
