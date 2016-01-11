package com.spontaneous.android.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.R;
import com.spontaneous.android.adapter.ViewPagerAdapter;
import com.spontaneous.android.fragment.FragmentEvents;
import com.spontaneous.android.fragment.FragmentUserProfile;
import com.spontaneous.android.http.request.service.EventService;
import com.spontaneous.android.http.response.BaseResponse;
import com.spontaneous.android.model.Event;
import com.spontaneous.android.util.AccountUtils;
import com.spontaneous.android.util.Logger;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Main activity of the app.
 * Contains EventsFragment and UserProfileFragment.
 */
public class ActivityMain extends BaseActivity {

    /**
     * The view pager contains the fragments embedded in the main activity.
     */
    private ViewPager mViewPager;

    /**
     * The adapter for the view pager.
     */
    private ViewPagerAdapter mPagerAdapter;

    /**
     * The loading spinner image.
     */
    private ImageView mLoadingImage;

    /**
     * Loading spinner animation.
     */
    private AnimationDrawable mLoadingAnimation;

    /**
     * The floating button for creating a new event.
     */
    private FloatingActionButton mCreateEventButton;

    /**
     * A fragment holding representational data of the user.
     */
    private FragmentUserProfile mUserProfileFragment;

    /**
     * Event history fragment.
     */
    private FragmentEvents mEventsFragment;

    private void initFragments() {
        mUserProfileFragment = new FragmentUserProfile();
        mEventsFragment = new FragmentEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFragments();

        mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mEventsFragment, mUserProfileFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(mPagerAdapter);

        mLoadingImage = (ImageView) findViewById(R.id.loading_image);
        mLoadingAnimation = (AnimationDrawable) mLoadingImage.getBackground();

        mCreateEventButton = (FloatingActionButton) findViewById(R.id.create_event_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class activity = ActivityCreateEvent.class;
                Intent intent = new Intent(getApplicationContext(), ActivityCreateEvent.class);

                Logger.info("Navigating to " + activity.getName());
                startActivityForResult(intent, 1);
            }
        });

        loadUserEvents();
    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Handle options menu selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // Do nothing for now
                return true;
            case R.id.logout:
                AccountUtils.logout(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Load user events, and show the loading image meanwhile.
     */
    private void loadUserEvents() {

        //Show the loading image, and make the viewpager and the floating action button invisible.
        mLoadingImage.setVisibility(View.VISIBLE);
        mCreateEventButton.setVisibility(View.GONE);
        mViewPager.setVisibility(View.INVISIBLE);

        //Start the loading animation
        mLoadingAnimation.start();

        //Get the finish animation
        final Animation finishAnimation = getFinishAnimation(mLoadingImage, mCreateEventButton, mViewPager);

        SpontaneousApplication.getInstance().getService(EventService.class)
                .getUserEvents(AccountUtils.getAuthenticatedUser().getId(), new Callback<BaseResponse<List<Event>>>() {
                    @Override
                    public void success(BaseResponse<List<Event>> events, Response response) {
                        Logger.debug("User events retrieved");

                        FragmentEvents.getEventListAdapter()
                                .addAll(events.getBody());

                        mLoadingImage.startAnimation(finishAnimation);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Logger.debug("Events retrieval failed.");
                        mLoadingImage.startAnimation(finishAnimation);
                    }
                });
    }

    /**
     * After creating a new event, add it to the event history.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Intent bundle extra name.
        String extraName = getString(R.string.created_event_intent_extra);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Event event = (Event) data.getSerializableExtra(extraName);

                FragmentEvents.getEventListAdapter()
                        .add(event);
            }
        }
    }
}
