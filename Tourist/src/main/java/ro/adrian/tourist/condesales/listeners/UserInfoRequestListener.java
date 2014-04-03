package ro.adrian.tourist.condesales.listeners;

import ro.adrian.tourist.condesales.models.User;

public interface UserInfoRequestListener extends ErrorListener {

	public void onUserInfoFetched(User user);
}
