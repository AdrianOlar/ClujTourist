package ro.adrian.tourist.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ro.adrian.tourist.R;
import ro.adrian.tourist.asynctasks.AnalyzeReview;
import ro.adrian.tourist.condesales.models.Tip;
import ro.adrian.tourist.condesales.models.Venue;
import ro.adrian.tourist.interfaces.VenuesTipCallbacks;
import ro.adrian.tourist.utils.Constants;

/**
 * Created by Adrian Olar on 14/04/2014.
 * Licence Thesis Project
 */
public class TipsAdapter extends ArrayAdapter<Tip>{

    private LayoutInflater mLayoutInflater;

    public TipsAdapter(Context context, int resource, ArrayList<Tip> objects) {
        super(context, resource, objects);
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_venue_item, parent, false);
            viewHolder = new ViewHolder();
            if (convertView != null) {
                viewHolder.venueName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.venueReview = (TextView) convertView.findViewById(R.id.details);
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Tip tip = getItem(position);

        viewHolder.venueName.setText(tip.getVenue().getName());
        viewHolder.venueReview.setText(tip.getText());
        //viewHolder.venueReview.setTextColor(getReviewColor(reviewText));
        return convertView;
    }

    private static class ViewHolder {
        TextView venueName;
        TextView venueReview;
    }

    private int getReviewColor(String review) {
        if (review.equals("very positive")) {
            return Constants.getContext().getResources().getColor(R.color.very_positive);
            //return "lime";
        } else if (review.equals("positive")) {
            return Constants.getContext().getResources().getColor(R.color.positive);
            //return "teal";
        } else if (review.equals("negative")) {
            return Constants.getContext().getResources().getColor(R.color.negative);
            //return "maroon";
        } else if (review.equals("very negative")) {
            return Constants.getContext().getResources().getColor(R.color.very_negative);
            //return "red";
        } else {
            return Constants.getContext().getResources().getColor(R.color.neutral);
            //return "grey";
        }
    }
}
