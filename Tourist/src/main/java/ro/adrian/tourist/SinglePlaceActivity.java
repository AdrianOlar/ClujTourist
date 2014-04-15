package ro.adrian.tourist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import java.util.ArrayList;

import ro.adrian.tourist.asynctasks.AnalyzeReviewsAsync;
import ro.adrian.tourist.interfaces.SinglePlaceCallbacks;
import ro.adrian.tourist.model.places.Place;
import ro.adrian.tourist.model.places.PlaceDetails;
import ro.adrian.tourist.utils.GooglePlaces;
import ro.adrian.tourist.utils.InternetUtils;

/**
 * Created by Adrian-PC on 4/3/14.
 * Licence thesis project
 */
public class SinglePlaceActivity extends Activity implements SinglePlaceCallbacks{
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Google Places
    GooglePlaces googlePlaces;
    // Place Details
    PlaceDetails placeDetails;
    // Progress dialog
    ProgressDialog pDialog;
    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    private TextView lbl_reviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_place);
        Intent i = getIntent();
        String reference = i.getStringExtra(KEY_REFERENCE);
        new LoadSinglPlaceDetails().execute(reference);
    }

    @Override
    public void setReviewsTxt(String reviewsTxt) {
        lbl_reviews.setText(Html.fromHtml("<b>Reviews:</b><br>" + reviewsTxt),TextView.BufferType.SPANNABLE);
    }

    private class LoadSinglPlaceDetails extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SinglePlaceActivity.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String reference = params[0];

            // creating Places class object
            googlePlaces = new GooglePlaces();

            // Check if used is connected to Internet
            try {
                placeDetails = googlePlaces.getPlaceDetails(reference);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    if (placeDetails != null) {
                        String status = placeDetails.status;

                        // check place deatils status
                        // Check for all possible status
                        if (status.equals("OK")) {
                            if (placeDetails.result != null) {
                                String name = placeDetails.result.name;
                                String address = placeDetails.result.formatted_address;
                                String phone = placeDetails.result.formatted_phone_number;
                                String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                                String longitude = Double.toString(placeDetails.result.geometry.location.lng);
                                new AnalyzeReviewsAsync(SinglePlaceActivity.this, SinglePlaceActivity.this).execute((ArrayList<Place.Review>) placeDetails.result.reviews);
                                //String review = "";
                                //Log.d("Place ", name + address + phone + latitude + longitude + "  Review: " + review);

                                // Displaying all the details in the view
                                // single_place.xml
                                TextView lbl_name = (TextView) findViewById(R.id.name);
                                TextView lbl_address = (TextView) findViewById(R.id.address);
                                TextView lbl_phone = (TextView) findViewById(R.id.phone);
                                TextView lbl_location = (TextView) findViewById(R.id.location);
                                lbl_reviews = (TextView) findViewById(R.id.reviews);

                                // Check for null data from google
                                // Sometimes place details might missing
                                name = name == null ? "Not present" : name; // if name is null display as "Not present"
                                address = address == null ? "Not present" : address;
                                phone = phone == null ? "Not present" : phone;
                                latitude = latitude == null ? "Not present" : latitude;
                                longitude = longitude == null ? "Not present" : longitude;
                                //review = review == null ? "Not present" : review;

                                lbl_name.setText(name);
                                lbl_address.setText(address);
                                lbl_phone.setText(Html.fromHtml("<b>Phone:</b> " + phone));
                                lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));
                                //lbl_reviews.setText(Html.fromHtml("<b>Reviews:</b><br>" + review));
                                //Log.d("Formatted review", review);
                            }
                        } else if (status.equals("ZERO_RESULTS")) {
                            InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Near Places",
                                    "Sorry no place found.",
                                    false);
                        } else if (status.equals("UNKNOWN_ERROR")) {
                            InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry unknown error occured.",
                                    false);
                        } else if (status.equals("OVER_QUERY_LIMIT")) {
                            InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry query limit to google places is reached",
                                    false);
                        } else if (status.equals("REQUEST_DENIED")) {
                            InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Request is denied",
                                    false);
                        } else if (status.equals("INVALID_REQUEST")) {
                            InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured. Invalid Request",
                                    false);
                        } else {
                            InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                    "Sorry error occured.",
                                    false);
                        }
                    } else {
                        InternetUtils.showAlertDialog(SinglePlaceActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });
        }
    }
}
