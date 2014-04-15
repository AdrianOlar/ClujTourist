package ro.adrian.tourist.asynctasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.ArrayList;

import ro.adrian.tourist.R;
import ro.adrian.tourist.interfaces.SinglePlaceCallbacks;
import ro.adrian.tourist.model.places.Place;
import ro.adrian.tourist.sentiword.english.SentiWordNet;
import ro.adrian.tourist.utils.Constants;

/**
 * Created by Adrian Olar on 13/04/2014.
 * Licence Thesis Project
 */
public class AnalyzeReviewsAsync extends AsyncTask<ArrayList<Place.Review>, String, String> {

    private final Activity activity;
    private ProgressDialog progressDialog;
    private SinglePlaceCallbacks callbacks;

    public AnalyzeReviewsAsync(Activity activity, SinglePlaceCallbacks callbacks) {
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
    protected String doInBackground(ArrayList<Place.Review>... params) {
        try {
            return parseReviews(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        String review = s == null ? "Not present" : s;
        callbacks.setReviewsTxt(review);
    }

    private String parseReviews(ArrayList<Place.Review> reviewArrayList) throws IOException {
        if(reviewArrayList!=null && reviewArrayList.size()>0) {
            String reviewOutput = "";
            for (Place.Review r : reviewArrayList) {
                reviewOutput += "<p> <font color='" + getReviewColor(SentiWordNet.getInstance().analyzeReview(r.text)) + "'>";
                reviewOutput += r.text;
                reviewOutput += "</font></p>";
            }
            return reviewOutput;
        }else{
            return null;
        }
    }

    private String getReviewColor(String review) {
        if (review.equals("very positive")) {
            //return Constants.getContext().getResources().getString(R.color.very_positive);
            return "lime";
        } else if (review.equals("positive")) {
            //return Constants.getContext().getResources().getString(R.color.positive);
            return "teal";
        } else if (review.equals("negative")) {
            //return Constants.getContext().getResources().getString(R.color.negative);
            return "maroon";
        } else if (review.equals("very negative")) {
            //return Constants.getContext().getResources().getString(R.color.very_negative);
            return "red";
        } else {
            //return Constants.getContext().getResources().getString(R.color.neutral);
            return "grey";
        }
    }

}
