package ro.adrian.tourist.condesales;


import android.app.Activity;
import android.content.SharedPreferences;
import ro.adrian.tourist.condesales.constants.FoursquareConstants;
import ro.adrian.tourist.condesales.criterias.CheckInCriteria;
import ro.adrian.tourist.condesales.criterias.TipsCriteria;
import ro.adrian.tourist.condesales.criterias.TrendingVenuesCriteria;
import ro.adrian.tourist.condesales.criterias.VenuesCriteria;
import ro.adrian.tourist.condesales.listeners.AccessTokenRequestListener;
import ro.adrian.tourist.condesales.listeners.CheckInListener;
import ro.adrian.tourist.condesales.listeners.FoursquareTrendingVenuesResquestListener;
import ro.adrian.tourist.condesales.listeners.FoursquareVenueDetailsResquestListener;
import ro.adrian.tourist.condesales.listeners.FoursquareVenuesResquestListener;
import ro.adrian.tourist.condesales.listeners.FriendsListener;
import ro.adrian.tourist.condesales.listeners.GetCheckInsListener;
import ro.adrian.tourist.condesales.listeners.TipsResquestListener;
import ro.adrian.tourist.condesales.listeners.UserInfoRequestListener;
import ro.adrian.tourist.condesales.listeners.VenuesHistoryListener;
import ro.adrian.tourist.condesales.tasks.tips.TipsNearbyRequest;
import ro.adrian.tourist.condesales.tasks.checkins.CheckInRequest;
import ro.adrian.tourist.condesales.tasks.users.GetCheckInsRequest;
import ro.adrian.tourist.condesales.tasks.users.GetFriendsRequest;
import ro.adrian.tourist.condesales.tasks.users.GetUserVenuesHistoryRequest;
import ro.adrian.tourist.condesales.tasks.users.SelfInfoRequest;
import ro.adrian.tourist.condesales.tasks.venues.FoursquareTrendingVenuesNearbyRequest;
import ro.adrian.tourist.condesales.tasks.venues.FoursquareVenueDetailsRequest;
import ro.adrian.tourist.condesales.tasks.venues.FoursquareVenuesNearbyRequest;

/**
 * Class to handle methods used to perform requests to FoursquareAPI and respond
 * ASYNChronously.
 * 
 * @author Felipe Conde <condesales@gmail.com>
 * 
 */
public class EasyFoursquareAsync {

	private Activity mActivity;
	private FoursquareDialog mDialog;
	private String mAccessToken = "";

	public EasyFoursquareAsync(Activity activity) {
		mActivity = activity;
	}

	/**
	 * Requests the access to API
	 */
	public void requestAccess(AccessTokenRequestListener listener) {
		if (!hasAccessToken()) {
			loginDialog(listener);
		} else {
			listener.onAccessGrant(getAccessToken());
		}
	}

	/**
	 * Requests logged user information asynchronously.
	 * 
	 * @param listener
	 *            As the request is asynchronous, listener used to retrieve the
	 *            User object, containing the information.
	 */
	public void getUserInfo(UserInfoRequestListener listener) {
		SelfInfoRequest request = new SelfInfoRequest(mActivity, listener);
		request.execute(getAccessToken());
	}

	/**
	 * Requests the nearby Venues.
	 * 
	 * @param criteria
	 *            The criteria to your search request
	 * @param listener
	 *            As the request is asynchronous, listener used to retrieve the
	 *            User object, containing the information.
	 */
	public void getVenuesNearby(FoursquareVenuesResquestListener listener,
			VenuesCriteria criteria) {
		FoursquareVenuesNearbyRequest request = new FoursquareVenuesNearbyRequest(
				mActivity, listener, criteria);
		request.execute(getAccessToken());
	}
	
	/**
	 * Requests the nearby Tips.
	 * 
	 * @param criteria
	 *            The criteria to your search request
	 * @param listener
	 *            As the request is asynchronous, listener used to retrieve the
	 *            User object, containing the information.
	 */
	public void getTipsNearby(TipsResquestListener listener,
			TipsCriteria criteria) {
		TipsNearbyRequest request = new TipsNearbyRequest(
				mActivity, listener, criteria);
		request.execute(getAccessToken());
	}
	
	/**
	 * Requests the nearby Venus that are trending.
	 * @param listener
	 * 				As the request is asynchronous, listener used to retrieve the
	 *            	User object, containing the information.
	 * @param criteria
	 * 				The criteria to your search request
	 */
	public void getTrendingVenuesNearby(FoursquareTrendingVenuesResquestListener listener, TrendingVenuesCriteria criteria) {
		FoursquareTrendingVenuesNearbyRequest request = new FoursquareTrendingVenuesNearbyRequest(mActivity, listener,criteria);
		request.execute(getAccessToken());
		
	}
	
	public void getVenueDetail(String venueID ,FoursquareVenueDetailsResquestListener listener){
		FoursquareVenueDetailsRequest request = new FoursquareVenueDetailsRequest(mActivity,listener, venueID);
		request.execute(getAccessToken());
	}

	/**
	 * Checks in at a venue.
	 * 
	 * @param listener
	 *            As the request is asynchronous, listener used to retrieve the
	 *            User object, containing the information about the check in.
	 * @param criteria
	 *            The criteria to your search request
	 */
	public void checkIn(CheckInListener listener, CheckInCriteria criteria) {
		CheckInRequest request = new CheckInRequest(mActivity, listener,
				criteria);
		request.execute(getAccessToken());
	}

	public void getCheckIns(GetCheckInsListener listener) {
		GetCheckInsRequest request = new GetCheckInsRequest(mActivity, listener);
		request.execute(getAccessToken());
	}

	public void getCheckIns(GetCheckInsListener listener, String userID) {
		GetCheckInsRequest request = new GetCheckInsRequest(mActivity,
				listener, userID);
		request.execute(getAccessToken());
	}

	public void getFriends(FriendsListener listener) {
		GetFriendsRequest request = new GetFriendsRequest(mActivity, listener);
		request.execute(mAccessToken);
	}

	public void getFriends(FriendsListener listener, String userID) {
		GetFriendsRequest request = new GetFriendsRequest(mActivity, listener,
				userID);
		request.execute(getAccessToken());
	}

	public void getVenuesHistory(VenuesHistoryListener listener) {
		GetUserVenuesHistoryRequest request = new GetUserVenuesHistoryRequest(
				mActivity, listener);
		request.execute(getAccessToken());
	}

	public void getVenuesHistory(VenuesHistoryListener listener, String userID) {
		GetUserVenuesHistoryRequest request = new GetUserVenuesHistoryRequest(
				mActivity, listener, userID);
		request.execute(getAccessToken());
	}

	private boolean hasAccessToken() {
		String token = getAccessToken();
		return !token.equals("");
	}

	private String getAccessToken() {
		if (mAccessToken.equals("")) {
			SharedPreferences settings = mActivity.getSharedPreferences(
					FoursquareConstants.SHARED_PREF_FILE, 0);
			mAccessToken = settings.getString(FoursquareConstants.ACCESS_TOKEN,
					"");
		}
		return mAccessToken;
	}

	/**
	 * Requests the Foursquare login though a dialog.
	 */
	private void loginDialog(AccessTokenRequestListener listener) {
		String url = "https://foursquare.com/oauth2/authenticate"
				+ "?client_id=" + FoursquareConstants.CLIENT_ID
				+ "&response_type=code" + "&redirect_uri="
				+ FoursquareConstants.CALLBACK_URL;

		mDialog = new FoursquareDialog(mActivity, url, listener);
		mDialog.show();
	}

}
