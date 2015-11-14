package com.spontaneous.android.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.spontaneous.android.R;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.view.UserProfileCard;

/**
 * A fragment that contains a user profile
 */
public class FragmentUserProfile extends Fragment {

    private User mUser;

    private FrameLayout mCardContainer;
    private ScrollView mScroll;
    private View mUserProfileCard;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_user_profile, container, false);

        mScroll = (ScrollView) layout.findViewById(R.id.scroll);

        //In case we already have a user in memory
        if(null == mUser) {
            mUser = AccountUtils.getAuthenticatedUser();
        }

        mCardContainer = (FrameLayout) layout.findViewById(R.id.card);
        mUserProfileCard = new UserProfileCard(getActivity(), mUser);

        mCardContainer.addView(mUserProfileCard);
        mScroll.smoothScrollTo(0, 0); //For some reason the card starts on the bottom of the activity

        return layout;
    }
}
