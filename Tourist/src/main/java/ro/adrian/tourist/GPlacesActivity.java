package ro.adrian.tourist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import ro.adrian.tourist.model.places.Place;
import ro.adrian.tourist.model.places.PlaceList;
import ro.adrian.tourist.utils.Constants;
import ro.adrian.tourist.utils.GooglePlaces;
import ro.adrian.tourist.utils.InternetUtils;

/**
 * Created by Adrian-PC on 3/31/14.
 * Licence thesis project
 */
public class GPlacesActivity extends Activity {

    // KEY Strings
    public static String KEY_REFERENCE = "reference"; // id of the place
    public static String KEY_NAME = "name"; // name of the place
    public static String KEY_VICINITY = "vicinity"; // Place area name

    // ListItems data
    ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String, String>>();

    private boolean isInternetPresent = false;
    private ListView placesListView;
    private Button btnShowOnMap;
    private ProgressDialog pDialog;
    private GooglePlaces googlePlaces;
    private double latitude, longitude;
    private PlaceList nearPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gplaces);

        latitude = getIntent().getDoubleExtra(Constants.KEY_LAT, 0);
        longitude = getIntent().getDoubleExtra(Constants.KEY_LONG, 0);

        // Check if Internet present
        isInternetPresent = InternetUtils.isConnectingToInternet(GPlacesActivity.this);
        if (!isInternetPresent) {
            // Internet Connection is not present
            InternetUtils.showAlertDialog(GPlacesActivity.this, "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        // Getting listview
        placesListView = (ListView) findViewById(R.id.list);

        // button show on map
        btnShowOnMap = (Button) findViewById(R.id.btn_show_map);

        // calling background Async task to load Google Places
        // After getting places from Google all the data is shown in listview
        new LoadPlacesTask().execute();

        /** Button click event for shown on map */
        btnShowOnMap.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent i = new Intent(getApplicationContext(),
                        PlacesMapActivity.class);
                // Sending user current geo location
                i.putExtra("user_latitude", Double.toString(latitude));
                i.putExtra("user_longitude", Double.toString(longitude));

                // passing near places to map activity
                i.putExtra("near_places", nearPlaces);
                // staring activity
                startActivity(i);
            }
        });

        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // getting values from selected ListItem
                String reference = ((TextView) view.findViewById(R.id.reference)).getText().toString();

                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        SinglePlaceActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                // Sending place refrence id to single place activity
                // place refrence id used to get "Place full details"
                in.putExtra(KEY_REFERENCE, reference);
                startActivity(in);
            }
        });
    }

    private class LoadPlacesTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(GPlacesActivity.this);
            pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Places JSON
         */
        protected String doInBackground(String... args) {
            // creating Places class object
            googlePlaces = new GooglePlaces();

            try {
                // Separeate your place types by PIPE symbol "|"
                // If you want all types places make it as null
                // Check list of types supported by google
                //
                String types = "cafe|restaurant"; // Listing places only cafes, restaurants

                // Radius in meters - increase this value if you don't find any places
                double radius = 2000; // 1000 meters

                // get nearest places
                nearPlaces = googlePlaces.search(latitude,
                        longitude, radius, types);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * and show the data in UI
         * Always use runOnUiThread(new Runnable()) to update UI from background
         * thread, otherwise you will get error
         * *
         */
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed Places into LISTVIEW
                     * */
                    // Get json response status
                    String status = nearPlaces.status;

                    // Check for all possible status
                    if (status.equals("OK")) {
                        // Successfully got places details
                        if (nearPlaces.results != null) {
                            // loop through each place
                            for (Place p : nearPlaces.results) {
                                HashMap<String, String> map = new HashMap<String, String>();

                                // Place reference won't display in listview - it will be hidden
                                // Place reference is used to get "place full details"
                                map.put(KEY_REFERENCE, p.reference);

                                // Place name
                                map.put(KEY_NAME, p.name);


                                // adding HashMap to ArrayList
                                placesListItems.add(map);
                            }
                            // list adapter
                            ListAdapter adapter = new SimpleAdapter(GPlacesActivity.this, placesListItems,
                                    R.layout.list_item,
                                    new String[]{KEY_REFERENCE, KEY_NAME}, new int[]{
                                    R.id.reference, R.id.name});

                            // Adding data into listview
                            placesListView.setAdapter(adapter);
                        }
                    } else if (status.equals("ZERO_RESULTS")) {
                        // Zero results found
                        InternetUtils.showAlertDialog(GPlacesActivity.this, "Near Places",
                                "Sorry no places found. Try to change the types of places",
                                false);
                    } else if (status.equals("UNKNOWN_ERROR")) {
                        InternetUtils.showAlertDialog(GPlacesActivity.this, "Places Error",
                                "Sorry unknown error occured.",
                                false);
                    } else if (status.equals("OVER_QUERY_LIMIT")) {
                        InternetUtils.showAlertDialog(GPlacesActivity.this, "Places Error",
                                "Sorry query limit to google places is reached",
                                false);
                    } else if (status.equals("REQUEST_DENIED")) {
                        InternetUtils.showAlertDialog(GPlacesActivity.this, "Places Error",
                                "Sorry error occured. Request is denied",
                                false);
                    } else if (status.equals("INVALID_REQUEST")) {
                        InternetUtils.showAlertDialog(GPlacesActivity.this, "Places Error",
                                "Sorry error occured. Invalid Request",
                                false);
                    } else {
                        InternetUtils.showAlertDialog(GPlacesActivity.this, "Places Error",
                                "Sorry error occured.",
                                false);
                    }
                }
            });

        }

    }
}
