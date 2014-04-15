package ro.adrian.tourist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ro.adrian.tourist.R;
import ro.adrian.tourist.condesales.models.Venue;

/**
 * Created by Adrian Olar on 4/5/14.
 * License Thesis Project
 */
public class VenuesAdapter extends ArrayAdapter<Venue> {

    private LayoutInflater mLayoutInflater;

    public VenuesAdapter(Context context, int resource, ArrayList<Venue> objects) {
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
                viewHolder.venueDetails = (TextView) convertView.findViewById(R.id.details);
                convertView.setTag(viewHolder);
            }
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Venue venue = getItem(position);

        viewHolder.venueName.setText(venue.getName());
        viewHolder.venueDetails.setText(venue.getLocation().getAddress() + " " + venue.getLocation().getCity() + " " + venue.getLocation().getCountry());

        return convertView;
    }

    private static class ViewHolder {
        TextView venueName;
        TextView venueDetails;
    }
}
