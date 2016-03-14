package com.spontaneous.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.spontaneous.android.R;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.UserInterfaceUtils;
import com.spontaneous.android.view.UserProfileCard;

import java.util.Collection;

/**
 * A fragment that contains a user profile
 */
public class FragmentUserProfile extends Fragment {

    private User mUser;
    private ArrayAdapter<String> mFriendsAdapter;

    private TextView mFriendsTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ScrollView mScroll = (ScrollView) layout.findViewById(R.id.scroll);

        //In case we already have a user in memory
        if (mUser == null) {
            mUser = AccountUtils.getAuthenticatedUser();
        }

        FrameLayout mCardContainer = (FrameLayout) layout.findViewById(R.id.card);
        View mUserProfileCard = new UserProfileCard(getActivity(), mUser);

        mCardContainer.addView(mUserProfileCard);
        mScroll.smoothScrollTo(0, 0); //For some reason the card starts on the bottom of the activity

        ListView friendsListView = (ListView) layout.findViewById(R.id.friends_list);
        friendsListView.setOnScrollListener(UserInterfaceUtils.listViewScrollListener(friendsListView));
        mFriendsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        friendsListView.setAdapter(mFriendsAdapter);

        mFriendsTextView = (TextView) layout.findViewById(R.id.friends_text);

        //Add friends
        addFriends(AccountUtils.getAuthenticatedUser().getFriends());

        return layout;
    }

    /**
     * Add a collection of friends to the friends listview.
     *
     * @param friendsList The collection of friends to add.
     */
    public void addFriends(Collection<User> friendsList) {

        //Return void if friends list is empty.
        if (friendsList == null || friendsList.isEmpty()) {
            return;
        }

        //Clear friends.
        mFriendsAdapter.clear();

        //Set the text visible.
        mFriendsTextView.setVisibility(View.VISIBLE);

        //Add the each friend to the list view.
        for (User userProfile : friendsList) {
            mFriendsAdapter.add(userProfile.getEmail());
        }
    }
}
