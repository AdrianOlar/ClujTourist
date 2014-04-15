package ro.adrian.tourist.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import ro.adrian.tourist.interfaces.VenuesTipCallbacks;
import ro.adrian.tourist.sentiword.english.SentiWordNet;

/**
 * Created by Adrian Olar on 14/04/2014.
 * Licence Thesis Project
 */
public class AnalyzeReview extends AsyncTask<String, Void, String>{

    private final Activity activity;
    private ProgressDialog progressDialog;
    private VenuesTipCallbacks callbacks;

    public AnalyzeReview(Activity activity, VenuesTipCallbacks callbacks) {
        this.activity = activity;
        this.callbacks = callbacks;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Analyzing...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            return SentiWordNet.getInstance().analyzeReview(params[0]);
        } catch (IOException e) {
            Log.d(AnalyzeReview.class.getCanonicalName(),"Error analyzing text review");
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        String review = s == null ? "Not present" : s;
        callbacks.setReviewText(review);
    }
}
