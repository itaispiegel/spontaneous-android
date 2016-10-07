package com.spontaneous.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.model.Guest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This an adapter for guests list view in the event card.
 */
public class GuestListAdapter extends BaseAdapter {

    /**
     * Context of the activity.
     */
    private final Context mContext;

    /**
     * Connect between adapter logic and view.
     */
    private LayoutInflater mInflater;

    /**
     * List of users to include.
     */
    private final List<Guest> mGuests;

    /**
     * Initialize a new GuestListAdapter.
     *
     * @param mContext of the activity.
     */
    public GuestListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mGuests = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mGuests.size();
    }

    @Override
    public Guest getItem(int position) {
        return mGuests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Find the {@link Guest} with the given email address.
     *
     * @param email The given email address.
     * @return The guest with the given email address.
     * @throws RuntimeException In case that there is no such guest.
     */
    public Guest getGuestByEmail(String email) throws RuntimeException {
        for (Guest guest : mGuests) {

            //Iterate every each invited user in the collection, and check if it's email equals the given email.
            if (guest.getUserProfile().getEmail().equals(email)) {
                return guest;
            }
        }

        throw new RuntimeException(String.format("No such guest with the email '%s'", email));
    }

    /**
     * Updates a guest at a given index.
     *
     * @param index Index of guest to update.
     * @param guest The updated guest details.
     */
    public void updateGuest(int index, Guest guest) {
        mGuests.set(index, guest);
        notifyDataSetChanged();
    }

    /**
     * Add a collection of guests to the adapter.
     *
     * @param guests To add to the adapter.
     */
    public void addAll(Collection<Guest> guests) {
        mGuests.addAll(guests);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_guests, parent, false);
        }

        //Initialize user details
        NetworkImageView userProfilePicture = (NetworkImageView) convertView.findViewById(R.id.guests_list_user_photo);
        TextView userNameTextView = (TextView) convertView.findViewById(R.id.guests_list_user_name);
        TextView userStatusTextView = (TextView) convertView.findViewById(R.id.guests_list_user_status);
        ImageView isUserAttendingDrawable = (ImageView) convertView.findViewById(R.id.guests_list_attending);

        //Get the guest entity.
        Guest guest = mGuests.get(position);

        //Set views data.
        userProfilePicture.setImageUrl(guest.getUserProfile().getProfilePicture(),
                SpontaneousApplication.getInstance().getImageLoader());

        userNameTextView.setText(guest.getUserProfile().getName());
        userStatusTextView.setText(guest.getStatus());

        //Set whether the user is attending the event.
        isUserAttendingDrawable.setImageDrawable(guest.isAttending()
                ? mContext.getDrawable(R.drawable.ic_done_black)
                : mContext.getDrawable(R.drawable.ic_close_black));

        return convertView;
    }
}