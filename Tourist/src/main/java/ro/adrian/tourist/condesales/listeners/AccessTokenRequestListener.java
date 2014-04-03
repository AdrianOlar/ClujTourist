package ro.adrian.tourist.condesales.listeners;

public interface AccessTokenRequestListener extends ErrorListener {

	public void onAccessGrant(String accessToken);
	
}
