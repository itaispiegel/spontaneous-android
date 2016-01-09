package com.spontaneous.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.Application;
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
    private final List<InvitedUser> mInvited;

    /**
     * Intialize a new InvitedUsersListAdapter.
     * @param mContext of the activity.
     */
    public InvitedUsersListAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInvited = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return mInvited.size();
    }

    @Override
    public InvitedUser getItem(int position) {
        return mInvited.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Add a collection of invited users to the adapter.
     * @param invitedUsers To add to the adapter.
     */
    public void addAll(Collection<InvitedUser> invitedUsers) {
        mInvited.addAll(invitedUsers);
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
        InvitedUser currUser = mInvited.get(position);

        //Set views data
        userProfilePicture.setImageUrl(
                currUser.getUser().getProfilePicture(),
                Application.getInstance().getImageLoader()
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