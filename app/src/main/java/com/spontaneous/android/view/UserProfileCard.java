package com.spontaneous.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.model.User;

/**
 * The user card contains representational data about the user.
 */
public class UserProfileCard extends FrameLayout {

    private final Context mContext;

    private NetworkImageView mProfilePicture;
    private TextView mUserName;
    private ImageView mUserGender;
    private TextView mUserEmail;
    private TextView mUserAge;

    public UserProfileCard(Context context) {
        super(context);
        this.mContext = context;
    }

    public UserProfileCard(Context context, User user) {
        this(context);

        init();
        setUser(user);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_user_profile_card, this);

        mProfilePicture = (NetworkImageView) layout.findViewById(R.id.user_profile_picture);
        mUserName = (TextView) layout.findViewById(R.id.user_name);
        mUserGender = (ImageView) layout.findViewById(R.id.user_gender);
        mUserEmail = (TextView) layout.findViewById(R.id.user_email);
        mUserAge = (TextView) layout.findViewById(R.id.user_age);
    }

    private void setUser(User user) {

        mProfilePicture.setImageUrl(user.getProfilePicture(), SpontaneousApplication.getInstance().getImageLoader());
        mUserName.setText(user.getName());

        mUserGender.setImageDrawable(user.getGender() == User.Gender.Male
                ? mContext.getDrawable(R.drawable.ic_male)
                : mContext.getDrawable(R.drawable.ic_female));

        mUserEmail.setText(user.getEmail());
        mUserAge.setText(String.valueOf(user.getAge()));
    }
}
