package ro.adrian.tourist;

import android.app.Activity;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import ro.adrian.tourist.adapters.TipsAdapter;
import ro.adrian.tourist.adapters.VenuesAdapter;
import ro.adrian.tourist.condesales.EasyFoursquareAsync;
import ro.adrian.tourist.condesales.criterias.TipsCriteria;
import ro.adrian.tourist.condesales.criterias.VenuesCriteria;
import ro.adrian.tourist.condesales.listeners.AccessTokenRequestListener;
import ro.adrian.tourist.condesales.listeners.FoursquareVenuesResquestListener;
import ro.adrian.tourist.condesales.listeners.ImageRequestListener;
import ro.adrian.tourist.condesales.listeners.TipsResquestListener;
import ro.adrian.tourist.condesales.listeners.UserInfoRequestListener;
import ro.adrian.tourist.condesales.models.Tip;
import ro.adrian.tourist.condesales.models.User;
import ro.adrian.tourist.condesales.models.Venue;
import ro.adrian.tourist.condesales.tasks.users.UserImageRequest;
import ro.adrian.tourist.utils.Constants;

/**
 * Created by Adrian-PC on 3/29/14.
 * Licence thesis project
 */
public class PlacesActivity extends Activity implements AccessTokenRequestListener, ImageRequestListener {

    private EasyFoursquareAsync async;
    private ImageView userImage;
    private ViewSwitcher viewSwitcher;
    private TextView userName;
    private double latitude, longitude;
    private ArrayList<Venue> venueList = new ArrayList<Venue>();
    private ArrayList<Tip> tipList = new ArrayList<Tip>();
    private ListView venueListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places);
        latitude = getIntent().getDoubleExtra(Constants.KEY_LAT, 0);
        longitude = getIntent().getDoubleExtra(Constants.KEY_LONG, 0);
        userImage = (ImageView) findViewById(R.id.imageView1);
        viewSwitcher = (ViewSwitcher) findViewById(R.id.viewSwitcher1);
        userName = (TextView) findViewById(R.id.textView1);
        venueListView = (ListView) findViewById(R.id.foursquare_place_list);
        //ask for access
        async = new EasyFoursquareAsync(this);
        async.requestAccess(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onAccessGrant(String accessToken) {
        // with the access token you can perform any request to foursquare.
        // example:
        async.getUserInfo(new UserInfoRequestListener() {

            @Override
            public void onError(String errorMsg) {
                // Some error getting user info
                Toast.makeText(PlacesActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUserInfoFetched(User user) {
                // OWww. did i already got user!?
                if (user.getBitmapPhoto() == null) {
                    UserImageRequest request = new UserImageRequest(PlacesActivity.this, PlacesActivity.this);
                    request.execute(user.getPhoto());
                } else {
                    userImage.setImageBitmap(user.getBitmapPhoto());
                }
                userName.setText(user.getFirstName() + " " + user.getLastName());
                viewSwitcher.showNext();
                Toast.makeText(PlacesActivity.this, "Got it!", Toast.LENGTH_LONG).show();
            }
        });

        //for another example uncomment line below:
        requestTipsNearby(accessToken);
        //requestVenuesNearby(accessToken);
    }

    @Override
    public void onImageFetched(Bitmap bmp) {
        userImage.setImageBitmap(bmp);
    }

    @Override
    public void onError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
    }

    private void requestTipsNearby(String accessToken) {
        Location loc = new Location("");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);

        TipsCriteria criteria = new TipsCriteria();
        criteria.setLocation(loc);
        criteria.setQuantity(100);
        async.getTipsNearby(new TipsResquestListener() {

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(PlacesActivity.this, "error", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onTipsFetched(ArrayList<Tip> tips) {
                printTipToConsole(tips);
            }
        }, criteria);
    }

    private void requestVenuesNearby(String accessToken) {
        Location loc = new Location("");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);

        VenuesCriteria criteria = new VenuesCriteria();
        criteria.setLocation(loc);
        async.getVenuesNearby(new FoursquareVenuesResquestListener() {
            @Override
            public void onVenuesFetched(ArrayList<Venue> venues) {
                printVenuesToConsole(venues);
            }

            @Override
            public void onError(String errorMsg) {
                Toast.makeText(PlacesActivity.this, "Error loading nearby venues", Toast.LENGTH_SHORT).show();
            }
        }, criteria);
    }

    private void printVenuesToConsole(ArrayList<Venue> venues) {
        for (Venue v : venues) {
            Log.d(PlacesActivity.class.getCanonicalName(), v.getName());
            venueList.add(v);
        }
        VenuesAdapter adapter = new VenuesAdapter(PlacesActivity.this, 0, venueList);
        venueListView.setAdapter(adapter);
    }

    private void printTipToConsole(ArrayList<Tip> tips) {
        for (Tip t : tips) {
            Log.d(PlacesActivity.class.getCanonicalName(), t.getVenue() + " review: " + t.getText());
            tipList.add(t);
        }
        TipsAdapter adapter = new TipsAdapter(PlacesActivity.this, 0, tipList);
        venueListView.setAdapter(adapter);
    }

}
