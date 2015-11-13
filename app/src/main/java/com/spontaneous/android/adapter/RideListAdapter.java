package com.spontaneous.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.spontaneous.android.R;
import com.spontaneous.android.model.Ride;
import com.spontaneous.android.model.User;

import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

/**
 * Created by eidan on 5/24/15.
 */
public class RideListAdapter extends ArrayAdapter<Ride> {

    private final Context mContext;
    private List<Ride> mRides;

    public RideListAdapter(Context context, List<Ride> rides) {
        super(context, R.layout.list_row_ride, rides);
        mRides = rides;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_row_ride, parent, false);

            viewHolder = new ViewHolder();

            viewHolder.pictureView = (SimpleDraweeView) convertView.findViewById(R.id.list_row_picture);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.list_row_fullname);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.list_row_date);
            viewHolder.txtOrigin = (TextView) convertView.findViewById(R.id.list_row_origin);
            viewHolder.txtDestination = (TextView) convertView.findViewById(R.id.list_row_destination);
            viewHolder.txtCost = (TextView) convertView.findViewById(R.id.list_row_cost);
            viewHolder.txtPassengers = (TextView) convertView.findViewById(R.id.list_row_passengers);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Ride ride = mRides.get(position);

        if(ride != null) {
            User owner = ride.getOwner();
            if(owner != null) {
                if(owner.getPictureUrl() != null) {
                    Uri uri = Uri.parse(owner.getPictureUrl());
                    viewHolder.pictureView.setImageURI(uri);
                }
                viewHolder.txtName.setText(owner.getFullName());
            }

            DateTimeFormatter dtf = DateTimeFormat.forPattern("EEEE, MMMM dd, HH:mm");
            viewHolder.txtDate.setText(dtf.print(ride.getStartTime().withZone(DateTimeZone.UTC)));
            viewHolder.txtOrigin.setText(ride.getOrigin());
            viewHolder.txtDestination.setText(ride.getDestination());
            viewHolder.txtCost.setText(ride.getCost().toPlainString() + " NIS");

            int currentGuests = ride.getGuests() != null ? ride.getGuests().size() : 0;
            viewHolder.txtPassengers.setText(currentGuests+"/"+String.valueOf(ride.getPassengers()));
        }

        return convertView;
    }

    public List<Ride> getRides() {
        return mRides;
    }

    public void setRides(List<Ride> Rides) {
        this.mRides = Rides;
    }

    private static class ViewHolder {
        SimpleDraweeView pictureView;
        TextView txtName;
        TextView txtDate;
        TextView txtOrigin;
        TextView txtDestination;
        TextView txtCost;
        TextView txtPassengers;
    }
}
