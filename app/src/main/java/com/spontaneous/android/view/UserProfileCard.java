package com.spontaneous.android.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;
import com.spontaneous.android.Application;
import com.spontaneous.android.R;
import com.spontaneous.android.model.User;

/**
 * Display a user
 */
public class UserProfileCard extends FrameLayout {

    private User mUser;

    private Context mContext;

    private NetworkImageView mProfilePicture;
    private TextView mUserName;
    private TextView mStatus;
    private TextView mAge;
    private ImageView mGenderPhoto;
    private TextView mEmail;


    public UserProfileCard(Context context) {
        super(context);
        this.mContext = context;
    }

    public UserProfileCard(Context context, User mUser) {
        super(context);
        init();

        this.mUser = mUser;
        this.mContext = context;
        setUser(mUser);
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.view_user_profile_card, this);

        mProfilePicture = (NetworkImageView) layout.findViewById(R.id.user_profile_picture);
        mUserName = (TextView) layout.findViewById(R.id.user_name);
        mStatus = (TextView) layout.findViewById(R.id.user_status);
        mAge = (TextView) layout.findViewById(R.id.user_age);
        mGenderPhoto = (ImageView) layout.findViewById(R.id.user_gender);
        mEmail = (TextView) layout.findViewById(R.id.user_email);
    }

    public void setUser(User mUser) {
        this.mUser = mUser;

        mProfilePicture.setImageUrl(mUser.getProfilePicture(), Application.getInstance().getImageLoader());
        mUserName.setText(mUser.getName());
        //mStatus.setText("\"" + mUser.getStatus() + "\"");
        mAge.setText(mUser.getAge() + "");

/*        Drawable genderDrawable;
        if (mUser.getGender() == User.Gender.FEMALE) {
            genderDrawable = mContext.getResources().getDrawable(R.drawable.ic_female);
        } else {
            genderDrawable = mContext.getResources().getDrawable(R.drawable.ic_male);
        }
        mGenderPhoto.setImageDrawable(genderDrawable);*/

        mEmail.setText(mUser.getEmail());
    }
}
