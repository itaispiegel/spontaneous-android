package com.spontaneous.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.R;
import com.spontaneous.android.model.InvitedUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This an adapter for invited users list view in the event card.
 */
public class InvitedUsersListAdapter extends BaseAdapter {

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
    private final List<InvitedUser> mInvitedUsers;

    /**
     * Initialize a new InvitedUsersListAdapter.
     * @param mContext of the activity.
     */
    public InvitedUsersListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInvitedUsers = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mInvitedUsers.size();
    }

    @Override
    public InvitedUser getItem(int position) {
        return mInvitedUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Find the {@link InvitedUser} with the given email address.
     * @param email The given email address.
     * @return The invited user with the given email address.
     * @throws NullPointerException In case that there is no such invited user.
     */
    public InvitedUser getInvitedUserByEmail(String email) throws NullPointerException {
        for(InvitedUser invitedUser : mInvitedUsers) {

            //Iterate every each invited user in the collection, and check if it's email equals the given email.
            if(invitedUser.getUser().getEmail()
                    .equals(email)) {
                return invitedUser;
            }
        }

        throw new NullPointerException(String.format("No such invited user with the email '%s'", email));
    }

    /**
     * Add a collection of invited users to the adapter.
     * @param invitedUsers To add to the adapter.
     */
    public void addAll(Collection<InvitedUser> invitedUsers) {
        mInvitedUsers.addAll(invitedUsers);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (mInflater == null) {
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_invited, parent, false);
        }

        //Initialize user details
        NetworkImageView userProfilePicture = (NetworkImageView) convertView.findViewById(R.id.photo);
        TextView userNameTextView = (TextView) convertView.findViewById(R.id.title);
        TextView userStatusTextView = (TextView) convertView.findViewById(R.id.description);
        ImageView isUserComingImage = (ImageView) convertView.findViewById(R.id.is_attending);

        //Get invited user entity
        InvitedUser currUser = mInvitedUsers.get(position);

        //Set views data
        userProfilePicture.setImageUrl(
                currUser.getUser().getProfilePicture(),
                SpontaneousApplication.getInstance().getImageLoader()
        );

        userNameTextView.setText(currUser.getUser().getName());
        userStatusTextView.setText(currUser.getStatus());

        //Set whether the user is coming to the event
        isUserComingImage.setImageDrawable(currUser.isAttending()
                        ? mContext.getDrawable(R.drawable.ic_cab_done_holo_light)
                        : mContext.getDrawable(R.drawable.ic_close_black)
        );

        return convertView;
    }
}