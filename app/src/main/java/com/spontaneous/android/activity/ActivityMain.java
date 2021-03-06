package com.spontaneous.android.activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.spontaneous.android.R;
import com.spontaneous.android.SpontaneousApplication;
import com.spontaneous.android.adapter.ViewPagerAdapter;
import com.spontaneous.android.fragment.FragmentEvents;
import com.spontaneous.android.fragment.FragmentUserProfile;
import com.spontaneous.android.gcm.RegistrationIntentService;
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

    private ImageButton mEventsButton;
    private ImageButton mUserProfileButton;

    private ViewPager mViewPager;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ImageView mLoadingImage;
    private AnimationDrawable mLoadingAnimation;

    private FloatingActionButton mCreateEventButton;

    private FragmentUserProfile mUserProfileFragment;
    private FragmentEvents mEventsFragment;

    private void initFragments() {
        mUserProfileFragment = new FragmentUserProfile();
        mEventsFragment = new FragmentEvents();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initFragments();

        //Initialize view pager and sync it with the swipe refresh layout.
        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mEventsFragment, mUserProfileFragment);

        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        //Avoid NullPointerException
        assert mViewPager != null;

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSwipeRefreshLayout.setEnabled(event.getAction() == MotionEvent.ACTION_UP);
                return false;
            }
        });

        //On swipe, load user events.
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadUserEvents();
            }
        });

        //Initialize the loading image and the create event action button.
        mLoadingImage = (ImageView) findViewById(R.id.loading_image);

        //Avoid NullPointerException
        assert mLoadingImage != null;

        mLoadingAnimation = (AnimationDrawable) mLoadingImage.getBackground();

        mCreateEventButton = (FloatingActionButton) findViewById(R.id.create_event_button);
        mCreateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class activity = ActivityCreateEvent.class;
                Intent intent = new Intent(getApplicationContext(), activity);

                Logger.info("Navigating to " + activity.getName());
                startActivityForResult(intent, 1);
            }
        });

        //Initialize the buttons in the toolbar.
        mEventsButton = (ImageButton) findViewById(R.id.events_button);
        mUserProfileButton = (ImageButton) findViewById(R.id.user_profile_button);

        //On click listener for the tab buttons.
        View.OnClickListener tabBarClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTab(v.getId());
            }
        };

        mUserProfileButton.setOnClickListener(tabBarClickListener);
        mEventsButton.setOnClickListener(tabBarClickListener);

        final ImageButton[] tabButtons = {mEventsButton, mUserProfileButton};
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabButtons[position].performClick();
                hideKeyboard();
            }
        });

        //Select the events page.
        mEventsButton.setSelected(true);

        // Start IntentService to register this application with GCM.
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

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
            case R.id.logout:
                AccountUtils.logout(this, true);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Select a tab - change view pager to the tab's page, and change the image to be selected.
     *
     * @param tabId Resource id of the tab.
     */
    private void selectTab(int tabId) {
        if (tabId == R.id.events_button) {
            mViewPager.setCurrentItem(0);
        } else if (tabId == R.id.user_profile_button) {
            mViewPager.setCurrentItem(1);
        }

        mEventsButton.setSelected(tabId == R.id.events_button);
        mUserProfileButton.setSelected(tabId == R.id.user_profile_button);
    }

    public void setRefreshEnabled(boolean enabled) {
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    /**
     * Load user events, and show the loading image meanwhile.
     */
    private void loadUserEvents() {

        //Disable refreshing - just in case.
        mSwipeRefreshLayout.setRefreshing(false);

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
                        mLoadingImage.startAnimation(finishAnimation);

                        setToolbarMessage(getString(R.string.app_name_caps));

                        //Clear the adapter just in case.
                        mEventsFragment.getEventListAdapter()
                                .clear();

                        mEventsFragment.getEventListAdapter()
                                .addAll(events.getBody());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Logger.debug("Events retrieval failed.");
                        mLoadingImage.startAnimation(finishAnimation);

                        setToolbarMessage(getString(R.string.app_name_caps) + " - OFFLINE");

                        Toast.makeText(getApplicationContext(), "Connection Error", Toast.LENGTH_SHORT)
                                .show();
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

                //Insert the created event to the list.
                Event event = (Event) data.getSerializableExtra(extraName);
                mEventsFragment.getEventListAdapter().enqueue(event);
            }
        }
    }
}
