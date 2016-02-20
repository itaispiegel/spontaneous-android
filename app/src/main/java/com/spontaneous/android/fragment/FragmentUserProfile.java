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

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.spontaneous.android.R;
import com.spontaneous.android.model.User;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.view.UserProfileCard;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * A fragment that contains a user profile
 */
public class FragmentUserProfile extends Fragment {

    private User mUser;
    private ListView friendsList;

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

        friendsList = (ListView) layout.findViewById(R.id.friends_list);
        final ArrayAdapter<String> friendsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);

        GraphRequest request = GraphRequest.newMyFriendsRequest(
                AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray array, GraphResponse response) {
                        for (int i = 0; i < array.length(); i++) {
                            try {
                                friendsAdapter.add(array.get(i).toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        request.executeAsync();

        friendsList.setAdapter(friendsAdapter);

        return layout;
    }
}
